///Users/wangfeiwangfei/wangfei/GitHub/draw_android/app/src/main/java/com/example/draw_android/section05_canvas/a_translate/CanvasTranslateView2.kt
package com.example.draw_android.section05_canvas.a_translate

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Matrix
import android.graphics.Paint
import android.util.AttributeSet
import android.view.View

/**
 * Created by wangfei44 on 2022/1/26.
 */
class CanvasTranslateView2 constructor(context: Context, attributeSet: AttributeSet) :
    View(context, attributeSet) {
    var mWidth = 0
    var mHeight = 0
    val mMargin = 50f
    val paint = Paint().apply {
        color = Color.BLACK
        strokeWidth = 10f
        style = Paint.Style.STROKE
    }

    init {

    }

    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
        super.onSizeChanged(w, h, oldw, oldh)
        //这里目前作为了解:
        //当View大小发生变化或者初始化首次也会调用。这个调用在onDraw之前所以只要测量绘制过程中没有
        //发生大小改变那么在onDraw里面直接拿width和height也可以用，所以有很多开发场景中直接
        //在onDraw里面使用 height 和 width 应该杜绝。
        mWidth = w
        mHeight = h
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        drawDegree(canvas)
        //region 1.我们明白坐标圆点默认在左上角。接下来我们计算横线的起点和终点坐标。
        val startX = mMargin
        val startY = mHeight / 2f
        val stopX = width - 50f
        val stopY = mHeight / 2f
        canvas.drawLine(startX, startY, stopX, stopY, paint)
        //endregion

        //region 2.计算第一个刻度
        val scaleStartX = 50f + paint.strokeWidth / 2f
        val scaleStartY = height / 2f - 20f
        val scaleStopX = 50f + paint.strokeWidth / 2f
        val scaleStopY = height / 2f
        canvas.drawLine(scaleStartX, scaleStartY, scaleStopX, scaleStopY, paint)
        //endregion

        //region 3.第二个刻度
/*        //3.计算第二个刻度
        val scaleWidth = (mWidth - mMargin * 2) / 6
        val scaleStartX1 = 50f + paint.strokeWidth / 2f + scaleWidth
        val scaleStartY1 = height / 2f - 20f
        val scaleStopX1 = 50f + paint.strokeWidth / 2f + scaleWidth
        val scaleStopY1 = height / 2f
        canvas.drawLine(scaleStartX1, scaleStartY1, scaleStopX1, scaleStopY1, paint)*/
        //endregion
        drawTranslateLine(canvas)
    }

    private fun drawDegree(canvas: Canvas) {
        canvas.drawLine(100f,100f,200f,200f,paint)
        canvas.rotate(15f)
        canvas.drawLine(100f,100f,200f,200f,paint)
    }

    private fun drawTranslateLine(canvas: Canvas) {
        val startX = mMargin
        val startY = mHeight / 2f
        val stopX = width - 50f
        val stopY = mHeight / 2f
        //1,绘制横线
        canvas.drawLine(startX, startY, stopX, stopY, paint)
        //2,绘制刻度线
        drawScaleLine(canvas)
        //3,变换画布
        canvas.translate(0f,200f)
        //4,绘制新内容
        canvas.drawLine(startX, startY, stopX, stopY, paint)
        drawScaleLine(canvas)
    }

    private fun drawScaleLine(canvas: Canvas) {
        val scaleWidth = (mWidth - mMargin * 2 - paint.strokeWidth) / 6
        for (index in 0 until 7) {
            val scaleStartX1 = 50f + paint.strokeWidth / 2f + scaleWidth * index
            val scaleStartY1 = height / 2f - 20f
            val scaleStopX1 = 50f + paint.strokeWidth / 2f + scaleWidth * index
            val scaleStopY1 = height / 2f
            canvas.drawLine(scaleStartX1, scaleStartY1, scaleStopX1, scaleStopY1, paint)
        }
    }

}