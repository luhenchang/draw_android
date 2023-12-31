package com.example.draw_android.section05_canvas.e_curve

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.Path
import android.graphics.PointF
import android.graphics.Rect
import android.graphics.RectF
import android.util.AttributeSet
import android.util.Log
import android.view.MotionEvent
import android.view.View
import com.example.draw_android.R
import kotlin.math.pow
import kotlin.math.sqrt

/**
 * Created by wang fei on 2022/12/27.
 */
class CurveExampleView :
    View {
    var bitmap: Bitmap = BitmapFactory.decodeResource(resources, R.drawable.laopo)
    val paint = Paint().apply {
        color = Color.GRAY
        style = Paint.Style.FILL
        isAntiAlias = true
        textSize = 24f
    }

    val path = Path()
    val pathClip = Path()


    constructor(context: Context) : super(context)

    constructor(context: Context, attrs: AttributeSet?) : super(context, attrs)

    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int) : super(
        context,
        attrs,
        defStyleAttr
    )

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        //quadToStudy(canvas)
        quadClipPath(canvas)
    }

    private fun quadClipPath(canvas: Canvas) {
        pathClip.moveTo(0f, 0f)
        //path的结束点，即曲线的起始点，但是并非是控制点。
        pathClip.lineTo(0f, height.toFloat()/4*2)
        //设置控制点和结束点，控制点为屏幕中央，距离起始点150像素的地方。结束点在右侧距离顶部200像素的位置
        //和左侧曲线起始点关于屏幕中心左右对称。
        pathClip.quadTo(width / 2f, height.toFloat(), width.toFloat(), height.toFloat()/4*2)
        //去闭合曲线
        pathClip.lineTo(width.toFloat(), 0f)
        pathClip.close()
        canvas.clipPath(pathClip)
        canvas.drawBitmap(bitmap,Rect(0,0,bitmap.width,bitmap.height),Rect(0,0,width,height),paint)

    }

    private fun quadToStudy(canvas: Canvas) {
        path.moveTo(0f, 0f)
        //path的结束点，即曲线的起始点，但是并非是控制点。
        path.lineTo(0f, 200f)
        //设置控制点和结束点，控制点为屏幕中央，距离起始点150像素的地方。结束点在右侧距离顶部200像素的位置
        //和左侧曲线起始点关于屏幕中心左右对称。
        path.quadTo(width / 2f, 500f, width.toFloat(), 200f)
        //去闭合曲线
        path.lineTo(width.toFloat(), 0f)
        path.close()
        //绘制
        canvas.drawPath(path, paint)

        //控制点位置绘制一个小圆
        canvas.drawCircle(width / 2f, 500f, 15f, paint)
    }

}