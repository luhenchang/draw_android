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
            Color.parseColor("#E9EBEF"),
            Color.parseColor("#EEFFFF"),
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

    private val arrayNumber = arrayOf(12, 1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11)
    private fun drawScaleText(canvas: Canvas) {
        for (index in 0 until 12) {
            val R = width / 3f
            val startX = 0f
            val startY = -R + 30f
            val endX = 0f
            val endY = -R + 30f + 35f
            canvas.drawLine(startX, startY, endX, endY, hourPaint)
            canvas.rotate(30f)
        }

        for (index in arrayNumber.indices) {
            val R = width / 3f
            val startX = 0f
            val endY = -R + 30f + 35f
            //刻度绘制文字
            val text = arrayNumber[index].toString()

            val rect = Rect()
            textPaint.getTextBounds(text, 0, text.length, rect)
            //进行状态储存
            canvas.save()
            //将坐标系位置移动到绘制文字的底部。
            canvas.translate(startX - rect.width() / 2f, endY + 20f + rect.height())
            //因为整体会做旋转，为了让坐标系摆正，我们好一个个调整文字间距，那么需要逆时针进行旋转。
            canvas.rotate(-30f * (index))
            //画个X,Y参考坐标系。
            //canvas.drawLine(0f, 0f, 100f, 0f, textPaint)
            //canvas.drawLine(0f, 0f, 0f, 100f, textPaint)
            when (index) {
                3 -> {
                    canvas.translate(0f, rect.height() / 2f)
                }
                4, 5, 6 -> {
                    canvas.translate(-rect.width().toFloat(), rect.height().toFloat())
                }
                7, 8 -> {
                    canvas.translate(-rect.width().toFloat(), rect.height().toFloat() / 2f)
                }
                9 -> {
                    canvas.translate(-rect.width().toFloat(), 0f)
                }
                10 -> {
                    canvas.translate(-rect.width().toFloat() / 2f, -rect.height().toFloat() / 2f)
                }
            }
            //绘制文字。
            canvas.drawText(
                arrayNumber[index].toString(),
                0f,//手动画一下，startX为12点刻度减去文字的一半就是文字起始的X
                0f,//文字在刻度下面，至少文字高度的地方，且给了20像素的空间
                textPaint
            )
            canvas.restore()
            canvas.rotate(30f)
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
        textSize = 36f
        strokeWidth = 2f
        style = Paint.Style.FILL_AND_STROKE
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