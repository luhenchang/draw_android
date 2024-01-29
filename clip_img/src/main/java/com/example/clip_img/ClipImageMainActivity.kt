package com.example.clip_img

import android.annotation.SuppressLint
import android.app.Activity
import android.app.ProgressDialog
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.BitmapRegionDecoder
import android.graphics.Matrix
import android.graphics.Rect
import android.graphics.RectF
import android.media.ExifInterface
import android.os.AsyncTask
import android.os.Bundle
import android.text.TextUtils
import android.view.View
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import java.io.FileOutputStream
import java.io.IOException

class ClipImageMainActivity : AppCompatActivity() , View.OnClickListener {
    private var mClipImageView: ClipImageView? = null
    private var mCancel: TextView? = null
    private var mClip: TextView? = null
    private var mOutput: String? = null
    private var mInput: String? = null
    private var mMaxWidth = 0

    // 图片被旋转的角度
    private var mDegree = 0

    // 大图被设置之前的缩放比例
    private var mSampleSize = 0
    private var mSourceWidth = 0
    private var mSourceHeight = 0
    private var mDialog: ProgressDialog? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        this.setContentView(R.layout.activity_clip_image_main)
        mClipImageView = findViewById<View>(R.id.clip_image_view) as ClipImageView
        mCancel = findViewById<View>(R.id.cancel) as TextView
        mClip = findViewById<View>(R.id.clip) as TextView
        mCancel!!.setOnClickListener(this)
        mClip!!.setOnClickListener(this)

        val clipOptions = ClipOptions.createFromBundle(intent)
        mOutput = clipOptions.outputPath
        mInput = clipOptions.inputPath
        mMaxWidth = clipOptions.maxWidth
        mClipImageView!!.setAspect(clipOptions.aspectX, clipOptions.aspectY)
        mClipImageView!!.setTip(clipOptions.tip)
        mClipImageView!!.setMaxOutputWidth(mMaxWidth)
        setImageAndClipParams() //大图裁剪
        //        mClipImageView.setImageURI(Uri.fromFile(new File(mInput)));
        mDialog = ProgressDialog(this)
        mDialog!!.setMessage(getString(R.string.msg_clipping_image))
    }

    private fun setImageAndClipParams() {
        mClipImageView!!.post {
            mClipImageView!!.setMaxOutputWidth(mMaxWidth)
            mDegree = readPictureDegree(mInput)
            val isRotate = mDegree == 90 || mDegree == 270
            val options = BitmapFactory.Options()
            options.inJustDecodeBounds = true
            BitmapFactory.decodeFile(mInput, options)
            mSourceWidth = options.outWidth
            mSourceHeight = options.outHeight

            // 如果图片被旋转，则宽高度置换
            val w = if (isRotate) options.outHeight else options.outWidth

            // 裁剪是宽高比例3:2，只考虑宽度情况，这里按border宽度的两倍来计算缩放。
            mSampleSize = findBestSample(w, mClipImageView!!.clipBorder.width())
            options.inJustDecodeBounds = false
            options.inSampleSize = mSampleSize
            options.inPreferredConfig = Bitmap.Config.RGB_565
            val source = BitmapFactory.decodeFile(mInput, options)

            // 解决图片被旋转的问题
            val target: Bitmap
            if (mDegree == 0) {
                target = source
            } else {
                val matrix = Matrix()
                matrix.postRotate(mDegree.toFloat())
                target = Bitmap.createBitmap(
                    source,
                    0,
                    0,
                    source.getWidth(),
                    source.getHeight(),
                    matrix,
                    false
                )
                if (target != source && !source.isRecycled) {
                    source.recycle()
                }
            }
            mClipImageView!!.setImageBitmap(target)
        }
    }

    override fun onClick(v: View) {
        val id = v.id
        if (id == R.id.cancel) {
            onBackPressed()
        }
        if (id == R.id.clip) {
            clipImage()
        }
    }

    private fun clipImage() {
        if (mOutput != null) {
            mDialog!!.show()
            val task: AsyncTask<Void?, Void?, Void?> = @SuppressLint("StaticFieldLeak")
            object : AsyncTask<Void?, Void?, Void?>() {
                override fun doInBackground(vararg params: Void?): Void? {
                    var fos: FileOutputStream? = null
                    try {
                        fos = FileOutputStream(mOutput)
                        val bitmap = createClippedBitmap()
                        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, fos)
                        if (!bitmap.isRecycled) {
                            bitmap.recycle()
                        }
                        setResult(RESULT_OK, intent)
                    } catch (e: Exception) {
                        Toast.makeText(
                            this@ClipImageMainActivity,
                            R.string.msg_could_not_save_photo,
                            Toast.LENGTH_SHORT
                        ).show()
                    } finally {
                        IOUtils.close(fos)
                    }
                    return null
                }

                override fun onPostExecute(aVoid: Void?) {
                    mDialog!!.dismiss()
                    finish()
                }
            }
            task.execute()
        } else {
            finish()
        }
    }

    private fun createClippedBitmap(): Bitmap {
        if (mSampleSize <= 1) {
            return mClipImageView!!.clip()
        }

        // 获取缩放位移后的矩阵值
        val matrixValues = mClipImageView!!.clipMatrixValues
        val scale = matrixValues[Matrix.MSCALE_X]
        val transX = matrixValues[Matrix.MTRANS_X]
        val transY = matrixValues[Matrix.MTRANS_Y]

        // 获取在显示的图片中裁剪的位置
        val border = mClipImageView!!.clipBorder
        val cropX = (-transX + border.left) / scale * mSampleSize
        val cropY = (-transY + border.top) / scale * mSampleSize
        val cropWidth = border.width() / scale * mSampleSize
        val cropHeight = border.height() / scale * mSampleSize

        // 获取在旋转之前的裁剪位置
        val srcRect = RectF(cropX, cropY, cropX + cropWidth, cropY + cropHeight)
        val clipRect = getRealRect(srcRect)
        val ops = BitmapFactory.Options()
        val outputMatrix = Matrix()
        outputMatrix.setRotate(mDegree.toFloat())
        // 如果裁剪之后的图片宽高仍然太大,则进行缩小
        if (mMaxWidth > 0 && cropWidth > mMaxWidth) {
            ops.inSampleSize = findBestSample(cropWidth.toInt(), mMaxWidth)
            val outputScale = mMaxWidth / (cropWidth / ops.inSampleSize)
            outputMatrix.postScale(outputScale, outputScale)
        }

        // 裁剪
        var decoder: BitmapRegionDecoder? = null
        return try {
            decoder = BitmapRegionDecoder.newInstance(mInput!!, false)
            val source = decoder.decodeRegion(clipRect, ops)
            recycleImageViewBitmap()
            Bitmap.createBitmap(
                source,
                0,
                0,
                source.getWidth(),
                source.getHeight(),
                outputMatrix,
                false
            )
        } catch (e: Exception) {
            mClipImageView!!.clip()
        } finally {
            if (decoder != null && !decoder.isRecycled) {
                decoder.recycle()
            }
        }
    }

    private fun getRealRect(srcRect: RectF): Rect {
        return when (mDegree) {
            90 -> Rect(
                srcRect.top.toInt(),
                (mSourceHeight - srcRect.right).toInt(),
                srcRect.bottom.toInt(),
                (mSourceHeight - srcRect.left).toInt()
            )

            180 -> Rect(
                (mSourceWidth - srcRect.right).toInt(),
                (mSourceHeight - srcRect.bottom).toInt(),
                (mSourceWidth - srcRect.left).toInt(),
                (mSourceHeight - srcRect.top).toInt()
            )

            270 -> Rect(
                (mSourceWidth - srcRect.bottom).toInt(),
                srcRect.left.toInt(),
                (mSourceWidth - srcRect.top).toInt(),
                srcRect.right.toInt()
            )

            else -> Rect(
                srcRect.left.toInt(),
                srcRect.top.toInt(),
                srcRect.right.toInt(),
                srcRect.bottom.toInt()
            )
        }
    }

    private fun recycleImageViewBitmap() {
        mClipImageView!!.post { mClipImageView!!.setImageBitmap(null) }
    }

    override fun onBackPressed() {
        setResult(RESULT_CANCELED, intent)
        super.onBackPressed()
    }

    class ClipOptions {
        var aspectX = 0
            private set
        var aspectY = 0
            private set
        var maxWidth = 0
            private set
        var tip: String? = null
            private set
        var inputPath: String? = null
            private set
        var outputPath: String? = null
            private set

        fun aspectX(aspectX: Int): ClipOptions {
            this.aspectX = aspectX
            return this
        }

        fun aspectY(aspectY: Int): ClipOptions {
            this.aspectY = aspectY
            return this
        }

        fun maxWidth(maxWidth: Int): ClipOptions {
            this.maxWidth = maxWidth
            return this
        }

        fun tip(tip: String?): ClipOptions {
            this.tip = tip
            return this
        }

        fun inputPath(path: String?): ClipOptions {
            inputPath = path
            return this
        }

        fun outputPath(path: String?): ClipOptions {
            outputPath = path
            return this
        }

        fun startForResult(activity: Activity, requestCode: Int) {
            checkValues()
            val intent = Intent(activity, ClipImageMainActivity::class.java)
            intent.putExtra("aspectX", aspectX)
            intent.putExtra("aspectY", aspectY)
            intent.putExtra("maxWidth", maxWidth)
            intent.putExtra("tip", tip)
            intent.putExtra("inputPath", inputPath)
            intent.putExtra("outputPath", outputPath)
            activity.startActivityForResult(intent, requestCode)
        }

        private fun checkValues() {
            require(!TextUtils.isEmpty(inputPath)) { "The input path could not be empty" }
            require(!TextUtils.isEmpty(outputPath)) { "The output path could not be empty" }
        }

        companion object {
            fun createFromBundle(intent: Intent): ClipOptions {
                return ClipOptions()
                    .aspectX(intent.getIntExtra("aspectX", 1))
                    .aspectY(intent.getIntExtra("aspectY", 1))
                    .maxWidth(intent.getIntExtra("maxWidth", 0))
                    .tip(intent.getStringExtra("tip"))
                    .inputPath(intent.getStringExtra("inputPath"))
                    .outputPath(intent.getStringExtra("outputPath"))
            }
        }
    }

    companion object {
        /**
         * 计算最好的采样大小。
         *
         * @param origin 当前宽度
         * @param target 限定宽度
         * @return sampleSize
         */
        private fun findBestSample(origin: Int, target: Int): Int {
            var sample = 1
            var out = origin / 2
            while (out > target) {
                sample *= 2
                out /= 2
            }
            return sample
        }

        /**
         * 读取图片属性：旋转的角度
         *
         * @param path 图片绝对路径
         * @return degree旋转的角度
         */
        fun readPictureDegree(path: String?): Int {
            var degree = 0
            try {
                val exifInterface = ExifInterface(path!!)
                val orientation = exifInterface.getAttributeInt(
                    ExifInterface.TAG_ORIENTATION,
                    ExifInterface.ORIENTATION_NORMAL
                )
                when (orientation) {
                    ExifInterface.ORIENTATION_ROTATE_90 -> degree = 90
                    ExifInterface.ORIENTATION_ROTATE_180 -> degree = 180
                    ExifInterface.ORIENTATION_ROTATE_270 -> degree = 270
                }
            } catch (e: IOException) {
                e.printStackTrace()
            }
            return degree
        }

        fun prepare(): ClipOptions {
            return ClipOptions()
        }
    }
}