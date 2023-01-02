package com.example.draw_android.section05_canvas_clip.a_canvas_clip

import android.content.Context
import android.graphics.*
import android.os.Build
import android.util.AttributeSet
import android.view.View

/**
 * Created by wangfei44 on 2022/4/24.
 */
class ClipCanvasView constructor(context: Context, attributeSet: AttributeSet) :
    View(context, attributeSet) {
    val paint = Paint().apply {
        color = Color.BLACK
        strokeWidth = 1f
        style = Paint.Style.STROKE
        isAntiAlias = true
        textSize = 30f
    }
    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        //drawCircle(canvas)
        //drawColor(canvas)
        //drawText(canvas)
        drawLine(canvas)

    }


    //1.绘制圆圈
    private fun drawCircle(canvas: Canvas) {
        canvas.drawCircle(width / 2f, height / 2f, 200f, Paint().apply {
            color = Color.GRAY
            strokeWidth = 10f
            style = Paint.Style.FILL
            setShadowLayer(20f, 20f, 20f, Color.parseColor("#CCCCCC"))
        })
    }
    //2.给画布绘制颜色
    private fun drawColor(canvas: Canvas) {
        canvas.drawColor(Color.parseColor("#6897BB"))
        canvas.drawRGB(111,111,111)
    }
    //3.绘制文字
    private fun drawText(canvas: Canvas){
        canvas.translate(200f,height/2f)
        //绘制文字
        canvas.drawText("drawText绘制文字2", 0f, 0f, paint)
        //绘制文字在路径上
        val pathx=Path()
        pathx.moveTo(0f,0f)
        pathx.lineTo(width.toFloat(),0f)
        canvas.drawPath(pathx,paint)


        val pathLine=Path()
        pathLine.moveTo(0f,0f)
        pathLine.quadTo(200f,-100f,400f,0f)
        canvas.drawPath(pathLine,paint)
        //绘制文字为了明显Fill
        paint.style=Paint.Style.FILL
        paint.color=Color.RED

        canvas.drawTextOnPath("绘制文字到路径上", pathLine, 0f, 0f, paint)


        val verticalPathLine=Path()
        verticalPathLine.moveTo(100f,100f)
        verticalPathLine.lineTo(100f,400f)

    }
    //绘制线段
    private fun drawLine(canvas: Canvas) {
        val path = Path()
        //设置线（路径）的起点
        path.moveTo(100f,100f)
        //设置线（路径）的终点，如果没有设置路径的起点moveTo那么默认起点是(0,0)
        path.lineTo(400f,400f)
        canvas.drawPath(path,paint)


        path.lineTo(400f,411f)
        //关闭线(路径)，如果此时点不是起点那么默认会链接到起点，形成闭环路径。
        path.close()
    }

}














