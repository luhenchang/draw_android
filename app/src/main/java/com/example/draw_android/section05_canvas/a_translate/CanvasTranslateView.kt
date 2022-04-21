///Users/wangfeiwangfei/wangfei/GitHub/draw_android/app/src/main/java/com/example/draw_android/section05_canvas/a_translate/CanvasTranslateView.kt
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
class CanvasTranslateView constructor(context: Context, attributeSet: AttributeSet) :
    View(context, attributeSet) {
    init {

    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        val hSize = height / 100
        val wSize = width / 100
        //画网格
        drawScale(canvas, hSize, wSize)
        //绘制点
        canvas.drawCircle(100f, 100f, 20f, Paint().apply {
            color = Color.BLACK
            style = Paint.Style.FILL
            isAntiAlias = true
        })
        //创建矩阵
        val matrix = Matrix()
        matrix.setValues(
            floatArrayOf(
                1f, 0f, 100f,
                0f, 1f, 100f,
                0f, 0f, 1f
            )
        )
        canvas.setMatrix(matrix)
        canvas.translate(100f, 100f)
        canvas.drawCircle(100f, 100f, 20f, Paint().apply {
            color = Color.RED
            style = Paint.Style.FILL
            isAntiAlias = true
        })
    }

    private fun drawScale(canvas: Canvas, hSize: Int, wSize: Int) {
        //画竖线
        canvas.save()
        for (index in 0 until hSize) {
            canvas.drawLine(0f, 0f, width.toFloat(), 0f, Paint().apply {
                color = Color.GRAY
                strokeWidth = 5f
            })
            canvas.translate(0f, 100f)
        }
        canvas.restore()
        //画横线
        canvas.save()
        for (index in 0 until wSize) {
            canvas.drawLine(0f, 0f, 0f, height.toFloat(), Paint().apply {
                color = Color.GRAY
                strokeWidth = 5f
            })
            canvas.translate(100f, 0f)
        }
        canvas.restore()
    }
}