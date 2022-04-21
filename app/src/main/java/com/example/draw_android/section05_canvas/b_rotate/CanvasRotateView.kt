///Users/wangfeiwangfei/wangfei/GitHub/draw_android/app/src/main/java/com/example/draw_android/section05_canvas/b_rotate/CanvasRotateView.kt
package com.example.draw_android.section05_canvas.b_rotate

import android.content.Context
import android.graphics.*
import android.util.AttributeSet
import android.view.View

/**
 * Created by wangfei44 on 2022/1/26.
 */
class CanvasRotateView constructor(context: Context, attributeSet: AttributeSet) :
    View(context, attributeSet) {
    private var mWidth = 0
    private var mHeight = 0
    val paint = Paint().apply {
        color = Color.parseColor("#E5E7EB")
        strokeWidth = 10f
        style = Paint.Style.FILL
        isAntiAlias = true
    }
    private val paintShadowLayerBottom = Paint().apply {
        color = Color.parseColor("#E5E7EB")
        strokeWidth = 10f
        style = Paint.Style.FILL
        setShadowLayer(20f, 20f, 20f, Color.parseColor("#F5F7F6"))
        isAntiAlias = true
    }
    private val paintShadowLayerTop = Paint().apply {
        color = Color.parseColor("#E5E7EB")
        strokeWidth = 10f
        style = Paint.Style.FILL
        setShadowLayer(20f, -20f, -20f, Color.parseColor("#F5F7F6"))
        isAntiAlias = true
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
        paint.shader = RadialGradient(
            0f,
            0f,
            mWidth / 3f,
            Color.parseColor("#EFF1F1"),
            Color.parseColor("#E5E7EB"),
            Shader.TileMode.CLAMP
        )
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        canvas.translate(mWidth / 2f, mHeight / 2f)
        //绘制表盘背景
        drawBg(canvas)
        //绘制秒针刻度
        drawSecondScale(canvas)
        drawHourScale(canvas)
        drawScaleText(canvas)


    }

    private fun drawScaleText(canvas: Canvas) {
        for (index in 0 until 12) {
            val R = width / 3f
            val startX = 0f
            val startY = -R + 30f
            val endX = 0f
            val endY = -R + 30f + 35f
            canvas.drawLine(startX, startY, endX, endY, hourPaint)
            canvas.rotate(30f)
            //刻度绘制文字
            val text = (index + 1).toString()
            //val textWidth = textPaint.measureText(text)
            val rect = Rect()
            textPaint.getTextBounds(text, 0, text.length, rect)
            canvas.save()
            canvas.translate(startX ,endY + 20f)
            if (scaleIndex.contains(index)) {
                canvas.rotate(
                    -(index + 1) * 30f,
                    + 8f,
                     - 10f
                )
                canvas.drawLine(0f,0f,100f,0f,xLinePaint)
                canvas.drawLine(0f,0f,0f,100f,yLinePaint)

            } else if (scaleBottomIndex.contains(index)) {
                canvas.rotate(
                    -(index + 1) * 30f,
                    10f,
                    0f
                )
                canvas.drawLine(0f,0f,100f,0f,xLinePaint)
                canvas.drawLine(0f,0f,0f,100f,yLinePaint)
            } else {
                canvas.rotate(
                    -(index + 1) * 30f,
                    0f,
                    0f
                )
                canvas.drawLine(0f,0f,100f,0f,xLinePaint)
                canvas.drawLine(0f,0f,0f,100f,yLinePaint)
            }
            canvas.drawText(
                (index + 1).toString(),
                0f,//手动画一下，startX为12点刻度减去文字的一半就是文字起始的X
                0f,//文字在刻度下面，至少文字高度的地方，且给了20像素的空间
                textPaint
            )
            canvas.restore()
        }

    }

    private val secondPaint = Paint().apply {
        color = Color.parseColor("#B3B6C9")
        strokeWidth = 5f
        style = Paint.Style.FILL
        strokeCap = Paint.Cap.ROUND
        isAntiAlias = true
    }

    private val xLinePaint = Paint().apply {
        color = Color.parseColor("#EEC1B3")
        strokeWidth = 5f
        style = Paint.Style.STROKE
        strokeCap = Paint.Cap.ROUND
        isAntiAlias = true
    }
    private val yLinePaint = Paint().apply {
        color = Color.parseColor("#986802")
        strokeWidth = 5f
        style = Paint.Style.STROKE
        strokeCap = Paint.Cap.ROUND
        isAntiAlias = true
    }

    //绘制秒刻度
    private fun drawSecondScale(canvas: Canvas) {
        //1.(x1,y1)= (0，-R+30)，终点坐标为（0，-R+30+25）
        for (index in 0 until 60) {
            val R = width / 3f
            val startX = 0f
            val startY = -R + 30f
            val endX = 0f
            val endY = -R + 30f + 25f
            canvas.drawLine(startX, startY, endX, endY, secondPaint)
            canvas.rotate(6f)
        }
    }

    private val hourPaint = Paint().apply {
        color = Color.parseColor("#B3B6C9")
        strokeWidth = 8f
        style = Paint.Style.FILL
        strokeCap = Paint.Cap.ROUND
        isAntiAlias = true
    }

    private val textPaint = Paint().apply {
        color = Color.BLACK
        textSize = 30f
        style = Paint.Style.FILL
        isAntiAlias = true
    }
    val scaleIndex = arrayOf(3, 4, 5, 6)
    val scaleBottomIndex = arrayOf(8, 9, 10, 11)

    //绘制小时刻度
    private fun drawHourScale(canvas: Canvas) {
        for (index in 0 until 12) {
            val R = width / 3f
            val startX = 0f
            val startY = -R + 30f
            val endX = 0f
            val endY = -R + 30f + 35f
            canvas.drawLine(startX, startY, endX, endY, hourPaint)
            canvas.rotate(30f)
        }
    }

    private fun drawBg(canvas: Canvas) {
        //为了好看，绘制一个同样大小的表盘设置左上部分的边缘阴影
        canvas.drawCircle(0f, 0f, width / 3f, paintShadowLayerTop)
        //为了好看，绘制一个同样大小的表盘设置右下部分的边缘阴影
        canvas.drawCircle(0f, 0f, width / 3f, paintShadowLayerBottom)
        //绘制最顶层的表盘
        canvas.drawCircle(0f, 0f, width / 3f, paint)
    }
}