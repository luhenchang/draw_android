package com.example.clip_img

import android.content.Context
import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Matrix
import android.graphics.Paint
import android.graphics.PorterDuff
import android.graphics.PorterDuffXfermode
import android.graphics.Rect
import android.graphics.RectF
import android.graphics.drawable.BitmapDrawable
import android.graphics.drawable.Drawable
import android.net.Uri
import android.util.AttributeSet
import android.view.GestureDetector
import android.view.GestureDetector.SimpleOnGestureListener
import android.view.MotionEvent
import android.view.ScaleGestureDetector
import android.view.ScaleGestureDetector.OnScaleGestureListener
import android.view.View
import android.view.View.OnTouchListener
import androidx.appcompat.widget.AppCompatImageView
import kotlin.math.sqrt
class ClipImageView @JvmOverloads constructor(context: Context, attrs: AttributeSet? = null) :
    AppCompatImageView(context, attrs), OnScaleGestureListener, OnTouchListener {
    private val mPaint: Paint
    private val mMaskColor: Int
    private var mAspectX: Int
    private var mAspectY: Int
    private var mTipText: String?
    private val mClipPadding: Int
    private var mScaleMax = 4.0f
    private var mScaleMin = 2.0f

    /**
     * 初始化时的缩放比例
     */
    private var mInitScale = 1.0f

    /**
     * 用于存放矩阵
     */
    private val mMatrixValues = FloatArray(9)

    /**
     * 缩放的手势检查
     */
    private var mScaleGestureDetector: ScaleGestureDetector? = null
    private val mScaleMatrix = Matrix()

    /**
     * 用于双击
     */
    private val mGestureDetector: GestureDetector
    private var isAutoScale = false
    private var mLastX = 0f
    private var mLastY = 0f
    private var isCanDrag = false
    private var lastPointerCount = 0
    val clipBorder = Rect()
    private var mMaxOutputWidth = 0
    private val mDrawCircleFlag: Boolean
    private val mRoundCorner: Float

    init {
        setScaleType(ScaleType.MATRIX)
        mGestureDetector = GestureDetector(context,
            object : SimpleOnGestureListener() {
                override fun onDoubleTap(e: MotionEvent): Boolean {
                    if (isAutoScale) return true
                    val x = e.x
                    val y = e.y
                    if (scale < mScaleMin) {
                        postDelayed(AutoScaleRunnable(mScaleMin, x, y), 16)
                    } else {
                        postDelayed(AutoScaleRunnable(mInitScale, x, y), 16)
                    }
                    isAutoScale = true
                    return true
                }
            })
        mScaleGestureDetector = ScaleGestureDetector(context, this)
        setOnTouchListener(this)
        mPaint = Paint(Paint.ANTI_ALIAS_FLAG)
        mPaint.setColor(Color.WHITE)
        val ta = context.obtainStyledAttributes(attrs, R.styleable.ClipImageView)
        mAspectX = ta.getInteger(R.styleable.ClipImageView_civWidth, 1)
        mAspectY = ta.getInteger(R.styleable.ClipImageView_civHeight, 1)
        mClipPadding = ta.getDimensionPixelSize(R.styleable.ClipImageView_civClipPadding, 0)
        mTipText = ta.getString(R.styleable.ClipImageView_civTipText)
        mMaskColor = ta.getColor(R.styleable.ClipImageView_civMaskColor, -0x4e000000)
        mDrawCircleFlag = ta.getBoolean(R.styleable.ClipImageView_civClipCircle, false)
        mRoundCorner = ta.getDimension(R.styleable.ClipImageView_civClipRoundCorner, 0f)
        val textSize = ta.getDimensionPixelSize(R.styleable.ClipImageView_civTipTextSize, 24)
        mPaint.textSize = textSize.toFloat()
        ta.recycle()
        mPaint.isDither = true
    }

    /**
     * 自动缩放的任务
     */
    private inner class AutoScaleRunnable(
        private val mTargetScale: Float,
        /**
         * 缩放的中心
         */
        private val x: Float, private val y: Float
    ) : Runnable {
        private var tmpScale = 0f

        /**
         * 传入目标缩放值，根据目标值与当前值，判断应该放大还是缩小
         *
         * @param targetScale
         */
        init {
            tmpScale = if (scale < mTargetScale) {
                Companion.BIGGER
            } else {
                Companion.SMALLER
            }
        }

        override fun run() {
            // 进行缩放
            mScaleMatrix.postScale(tmpScale, tmpScale, x, y)
            checkBorder()
            setImageMatrix(mScaleMatrix)
            val currentScale: Float = scale
            // 如果值在合法范围内，继续缩放
            if (tmpScale > 1f && currentScale < mTargetScale || tmpScale < 1f && mTargetScale < currentScale) {
                postDelayed(this, 16)
            } else {
                // 设置为目标的缩放比例
                val deltaScale = mTargetScale / currentScale
                mScaleMatrix.postScale(deltaScale, deltaScale, x, y)
                checkBorder()
                setImageMatrix(mScaleMatrix)
                isAutoScale = false
            }
        }


    }
    companion object {
        const val BIGGER = 1.07f
        const val SMALLER = 0.93f
    }
    override fun onScale(detector: ScaleGestureDetector): Boolean {
        val scale = scale
        var scaleFactor = detector.getScaleFactor()
        if (getDrawable() == null) return true
        /**
         * 缩放的范围控制
         */
        if (scale < mScaleMax && scaleFactor > 1.0f || scale > mInitScale && scaleFactor < 1.0f) {
            /**
             * 缩放阙值最小值判断
             */
            if (scaleFactor * scale < mInitScale) {
                scaleFactor = mInitScale / scale
            }
            if (scaleFactor * scale > mScaleMax) {
                scaleFactor = mScaleMax / scale
            }
            /**
             * 设置缩放比例
             */
            mScaleMatrix.postScale(
                scaleFactor, scaleFactor,
                detector.focusX, detector.focusY
            )
            checkBorder()
            setImageMatrix(mScaleMatrix)
        }
        return true
    }

    private val matrixRectF: RectF
        /**
         * 根据当前图片的Matrix获得图片的范围
         *
         * @return
         */
        private get() {
            val matrix = mScaleMatrix
            val rect = RectF()
            val d = getDrawable()
            if (null != d) {
                rect[0f, 0f, d.intrinsicWidth.toFloat()] = d.intrinsicHeight.toFloat()
                matrix.mapRect(rect)
            }
            return rect
        }

    override fun onScaleBegin(detector: ScaleGestureDetector): Boolean {
        return true
    }

    override fun onScaleEnd(detector: ScaleGestureDetector) {}
    override fun onTouch(v: View, event: MotionEvent): Boolean {
        if (mGestureDetector.onTouchEvent(event)) return true
        mScaleGestureDetector!!.onTouchEvent(event)
        var x = 0f
        var y = 0f
        // 拿到触摸点的个数
        val pointerCount = event.pointerCount

        // 得到多个触摸点的x与y均值
        for (i in 0 until pointerCount) {
            x += event.getX(i)
            y += event.getY(i)
        }
        x /= pointerCount.toFloat()
        y /= pointerCount.toFloat()
        /**
         * 每当触摸点发生变化时，重置mLasX , mLastY
         */
        if (pointerCount != lastPointerCount) {
            isCanDrag = false
            mLastX = x
            mLastY = y
        }
        lastPointerCount = pointerCount
        when (event.action) {
            MotionEvent.ACTION_MOVE -> {
                var dx = x - mLastX
                var dy = y - mLastY
                if (!isCanDrag) {
                    isCanDrag = isCanDrag(dx, dy)
                }
                if (isCanDrag) {
                    if (getDrawable() != null) {
                        val rectF = matrixRectF
                        // 如果宽度小于屏幕宽度，则禁止左右移动
                        if (rectF.width() <= clipBorder.width()) {
                            dx = 0f
                        }

                        // 如果高度小雨屏幕高度，则禁止上下移动
                        if (rectF.height() <= clipBorder.height()) {
                            dy = 0f
                        }
                        mScaleMatrix.postTranslate(dx, dy)
                        checkBorder()
                        setImageMatrix(mScaleMatrix)
                    }
                }
                mLastX = x
                mLastY = y
            }

            MotionEvent.ACTION_UP, MotionEvent.ACTION_CANCEL -> lastPointerCount = 0
        }
        return true
    }

    val scale: Float
        /**
         * 获得当前的缩放比例
         *
         * @return
         */
        get() {
            mScaleMatrix.getValues(mMatrixValues)
            return mMatrixValues[Matrix.MSCALE_X]
        }

    override fun onLayout(changed: Boolean, left: Int, top: Int, right: Int, bottom: Int) {
        super.onLayout(changed, left, top, right, bottom)
        updateBorder()
    }

    private fun updateBorder() {
        val width = width
        val height = height
        clipBorder.left = mClipPadding
        clipBorder.right = width - mClipPadding
        val borderHeight = clipBorder.width() * mAspectY / mAspectX
        if (mDrawCircleFlag == true) { // 如果是圆形,宽高比例是1:1
            val borderTempHeight = clipBorder.width() * 1 / 1
            clipBorder.top = (height - borderTempHeight) / 2
            clipBorder.bottom = clipBorder.top + borderTempHeight
        } else { // 如果不是圆形,根据宽高比例
            clipBorder.top = (height - borderHeight) / 2
            clipBorder.bottom = clipBorder.top + borderHeight
        }
    }

    fun setAspect(aspectX: Int, aspectY: Int) {
        mAspectX = aspectX
        mAspectY = aspectY
    }

    fun setTip(tip: String?) {
        mTipText = tip
    }

    override fun setImageDrawable(drawable: Drawable?) {
        super.setImageDrawable(drawable)
        postResetImageMatrix()
    }

    override fun setImageResource(resId: Int) {
        super.setImageResource(resId)
        postResetImageMatrix()
    }

    override fun setImageURI(uri: Uri?) {
        super.setImageURI(uri)
        postResetImageMatrix()
    }

    /**
     * 这里没有使用post方式,因为图片会有明显的从初始位置移动到需要缩放的位置
     */
    private fun postResetImageMatrix() {
        if (width != 0) {
            resetImageMatrix()
        } else {
            post { resetImageMatrix() }
        }
    }

    /**
     * 垂直方向与View的边矩
     */
    fun resetImageMatrix() {
        val d = getDrawable() ?: return
        val dWidth = d.intrinsicWidth
        val dHeight = d.intrinsicHeight
        val cWidth = clipBorder.width()
        val cHeight = clipBorder.height()
        val vWidth = width
        val vHeight = height
        val scale: Float
        val dx: Float
        val dy: Float
        scale = if (dWidth * cHeight > cWidth * dHeight) {
            cHeight / dHeight.toFloat()
        } else {
            cWidth / dWidth.toFloat()
        }
        dx = (vWidth - dWidth * scale) * 0.5f
        dy = (vHeight - dHeight * scale) * 0.5f
        mScaleMatrix.setScale(scale, scale)
        mScaleMatrix.postTranslate((dx + 0.5f).toInt().toFloat(), (dy + 0.5f).toInt().toFloat())
        setImageMatrix(mScaleMatrix)
        mInitScale = scale
        mScaleMin = mInitScale * 2
        mScaleMax = mInitScale * 4
    }

    /**
     * 剪切图片
     *
     * @return 返回剪切后的bitmap对象
     */
    fun clip(): Bitmap {
        val drawable = getDrawable()
        val originalBitmap = (drawable as BitmapDrawable).bitmap
        val matrixValues = FloatArray(9)
        mScaleMatrix.getValues(matrixValues)
        val scale =
            matrixValues[Matrix.MSCALE_X] * drawable.getIntrinsicWidth() / originalBitmap.getWidth()
        val transX = matrixValues[Matrix.MTRANS_X]
        val transY = matrixValues[Matrix.MTRANS_Y]
        val cropX = (-transX + clipBorder.left) / scale
        val cropY = (-transY + clipBorder.top) / scale
        val cropWidth = clipBorder.width() / scale
        val cropHeight = clipBorder.height() / scale
        var outputMatrix: Matrix? = null
        if (mMaxOutputWidth > 0 && cropWidth > mMaxOutputWidth) {
            val outputScale = mMaxOutputWidth / cropWidth
            outputMatrix = Matrix()
            outputMatrix.setScale(outputScale, outputScale)
        }
        return Bitmap.createBitmap(
            originalBitmap, cropX.toInt(), cropY.toInt(), cropWidth.toInt(), cropHeight.toInt(),
            outputMatrix, false
        )
    }

    /**
     * 边界检查
     */
    private fun checkBorder() {
        val rect = matrixRectF
        var deltaX = 0f
        var deltaY = 0f

        // 如果宽或高大于屏幕，则控制范围
        if (rect.width() >= clipBorder.width()) {
            if (rect.left > clipBorder.left) {
                deltaX = -rect.left + clipBorder.left
            }
            if (rect.right < clipBorder.right) {
                deltaX = clipBorder.right - rect.right
            }
        }
        if (rect.height() >= clipBorder.height()) {
            if (rect.top > clipBorder.top) {
                deltaY = -rect.top + clipBorder.top
            }
            if (rect.bottom < clipBorder.bottom) {
                deltaY = clipBorder.bottom - rect.bottom
            }
        }
        mScaleMatrix.postTranslate(deltaX, deltaY)
    }

    /**
     * 是否是拖动行为
     *
     * @param dx
     * @param dy
     * @return
     */
    private fun isCanDrag(dx: Float, dy: Float): Boolean {
        return sqrt((dx * dx + dy * dy).toDouble()) >= 0
    }

    fun setMaxOutputWidth(maxOutputWidth: Int) {
        mMaxOutputWidth = maxOutputWidth
    }

    val clipMatrixValues: FloatArray
        get() {
            val matrixValues = FloatArray(9)
            mScaleMatrix.getValues(matrixValues)
            return matrixValues
        }

    /**
     * 参考showtipsview的做法
     */
    fun drawRectangleOrCircle(canvas: Canvas) {
        val bitmap = Bitmap.createBitmap(canvas.width, canvas.height, Bitmap.Config.ARGB_8888)
        val temp = Canvas(bitmap)
        val transparentPaint = Paint(Paint.ANTI_ALIAS_FLAG)
        val porterDuffXfermode = PorterDuffXfermode(PorterDuff.Mode.CLEAR)
        transparentPaint.setColor(Color.TRANSPARENT)
        temp.drawRect(0f, 0f, temp.width.toFloat(), temp.height.toFloat(), mPaint)
        transparentPaint.setXfermode(porterDuffXfermode)
        if (mDrawCircleFlag) { // 画圆
            val cx = clipBorder.left + clipBorder.width() / 2f
            val cy = clipBorder.top + clipBorder.height() / 2f
            val radius = clipBorder.height() / 2f
            temp.drawCircle(cx, cy, radius, transparentPaint)
        } else { // 画矩形(可以设置矩形的圆角)
            val rectF = RectF(
                clipBorder.left.toFloat(),
                clipBorder.top.toFloat(),
                clipBorder.right.toFloat(),
                clipBorder.bottom.toFloat()
            )
            temp.drawRoundRect(rectF, mRoundCorner, mRoundCorner, transparentPaint)
        }
        canvas.drawBitmap(bitmap, 0f, 0f, null)
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        val width = width
        val height = height
        mPaint.setColor(mMaskColor)
        mPaint.style = Paint.Style.FILL
        //        canvas.drawRect(0, 0, width, mClipBorder.top, mPaint);
//        canvas.drawRect(0, mClipBorder.bottom, width, height, mPaint);
//        canvas.drawRect(0, mClipBorder.top, mClipBorder.left, mClipBorder.bottom, mPaint);
//        canvas.drawRect(mClipBorder.right, mClipBorder.top, width, mClipBorder.bottom, mPaint);

//        mPaint.setColor(Color.WHITE);
        mPaint.strokeWidth = 1f
        drawRectangleOrCircle(canvas)
        //        mPaint.setStyle(Paint.Style.STROKE);
//
//        canvas.drawRect(mClipBorder.left, mClipBorder.top, mClipBorder.right, mClipBorder.bottom, mPaint);
        if (mTipText != null) {
            val textWidth = mPaint.measureText(mTipText)
            val startX = (width - textWidth) / 2
            val fm = mPaint.getFontMetrics()
            val startY = clipBorder.bottom + clipBorder.top / 2 - (fm.descent - fm.ascent) / 2
            mPaint.style = Paint.Style.FILL
            canvas.drawText(mTipText!!, startX, startY, mPaint)
        }
    }
}