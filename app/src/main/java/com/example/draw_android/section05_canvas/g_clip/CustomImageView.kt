package com.example.draw_android.section05_canvas.g_clip

import android.R.attr
import android.R.attr.bitmap
import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Matrix
import android.graphics.Paint
import android.graphics.Path
import android.graphics.Rect
import android.util.AttributeSet
import android.view.View
import com.example.draw_android.R
import kotlin.math.min


class CustomImageView :
    View {
    private var dtBitmap: Bitmap? = null
    val paint = Paint().apply {
        color = Color.GRAY
        style = Paint.Style.FILL
        isAntiAlias = true
        textSize = 24f
    }

    private val clipPath = Path()

    constructor(context: Context) : super(context)

    constructor(context: Context, attrs: AttributeSet?) : super(context, attrs)

    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int) : super(
        context,
        attrs,
        defStyleAttr
    )
    private var srcRect:Rect? = null
    private lateinit var dstRect:Rect
    init {
        dtBitmap = BitmapFactory.decodeResource(resources, R.drawable.img_length)
        dtBitmap?.let {
            srcRect = Rect(0,0,it.width,it.height)
        }
    }

    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
        super.onSizeChanged(w, h, oldw, oldh)
        dstRect = Rect(0,0,width,height)
    }
    override fun onDraw(canvas: Canvas) {
        val radius = (min(width.toDouble(), height.toDouble()) / 2.0f).toFloat()
        clipPath.reset()
        clipPath.addCircle(width / 2.0f, height / 2.0f, radius, Path.Direction.CW)
        //将画布裁剪为圆形
        canvas.clipPath(clipPath)


        // 将图片按比例缩放到指定大小并居中显示
        val scaleX: Float = width.toFloat() / dtBitmap!!.width
        val scaleY: Float = height.toFloat() / dtBitmap!!.height
        val matrix = Matrix()
        matrix.postScale(scaleX, scaleY)
        matrix.postTranslate(
            -dtBitmap!!.getWidth() / 2f + attr.width / 2f,
            -dtBitmap!!.getHeight() / 2f + attr.height / 2f
        )
        val paint = Paint()
        paint.isAntiAlias = true
        canvas.drawBitmap(dtBitmap!!, matrix, paint)
//        dtBitmap?.let {bitMap->
//            //不对素材图片缩放到控件自身区域。
//            //canvas.drawBitmap(bitMap,0f,0f,paint)
//            srcRect.let {
//                //不管素材图片多大多小，将其所有像素都或大或小的缩放到dstRect即控件大小的区域里面。进行显示
//                canvas.drawBitmap(bitMap,srcRect,dstRect,paint)
//            }
//        }

    }
}