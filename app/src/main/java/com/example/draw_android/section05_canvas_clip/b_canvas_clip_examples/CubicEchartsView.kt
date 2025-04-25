package com.example.draw_android.section05_canvas_clip.b_canvas_clip_examples

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.Path
import android.graphics.Rect
import android.util.AttributeSet
import android.view.View
import androidx.core.graphics.toColorInt
import com.example.draw_android.R

class CubicEchartsView  constructor(context: Context, attributeSet: AttributeSet) :
    View(context, attributeSet) {
    private var dataList = arrayListOf(50f, 360f, 150f, 500f, 161f, 250f, 30f, 200f, 130f, 300f,200f,250f)
    private var marginLeft = 100f //距离屏幕左边距离
    private var marginBottom = 100f//距离屏幕下边距离
    private var marginTopAndBottom = 100f
    private val scaleWidth: Float = 250f //刻度宽度
    private val textMarginX: Float = 20f //月份距离X轴高度
    val paint = Paint().apply {
        color = "#80FBBC".toColorInt()
        style = Paint.Style.FILL
        strokeWidth = 1f
        strokeCap = Paint.Cap.ROUND
        isAntiAlias = true
    }
    private var outPathLine = Paint()
    private var fillPathPaint = Paint()
    private var textPaint = Paint()
    private var outPathShadowPaint = Paint()
    //Y轴顶端的箭头路径
    private var arrowheadYPath = Path()
    private var unitH: Float = 0f
    private val bitmap: Bitmap = BitmapFactory.decodeResource(resources, R.drawable.img_jinshu_svg)

    init {

        outPathLine.apply {
            color = "#80FBBC".toColorInt()
            style = Paint.Style.STROKE
            strokeWidth = 6f
            setShadowLayer(
                6f,
                3f,
                2f,
                Color.BLACK
            )
            strokeCap = Paint.Cap.ROUND
            isAntiAlias = true
        }
        fillPathPaint.apply {
            color = "#80FBBC".toColorInt()
            style = Paint.Style.FILL
            strokeWidth = 6f
            setShadowLayer(
                6f,
                0f,
                -6f,
                Color.BLACK
            )
            strokeCap = Paint.Cap.ROUND
            isAntiAlias = true
        }
        textPaint.apply {
            textSize = 30f
            setShadowLayer(
                6f,
                0f,
                6f,
                Color.BLACK
            )
            color = Color.parseColor("#80FBBC")
            style = Paint.Style.FILL
        }
    }
    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
        super.onSizeChanged(w, h, oldw, oldh)
        unitH = (height - marginTopAndBottom * 2) / dataList.max()
    }
    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        canvas.drawBitmap(bitmap,0f,0f,paint)
        canvas.scale(1f,-1f)
        canvas.translate(marginLeft,-height.toFloat()+marginBottom)
        canvas.drawCircle(0f,0f,20f, paint)
        //1、绘制X对称轴上面的刻度
        dataList.forEachIndexed { index, value ->
            if (index < dataList.size -1) {
                canvas.save()
                canvas.translate(0f, outPathLine.strokeWidth / 2f)
                val centerX = index * scaleWidth + scaleWidth
                canvas.drawLine(centerX, 0f, centerX, 20f, outPathLine)
                canvas.restore()
            }
        }

        //绘制Y对称轴
        canvas.drawLine(0f, 0f, 0f, dataList.max() * unitH, fillPathPaint)
        //Y轴顶部的箭头
        canvas.save()
        canvas.translate(0f, outPathLine.strokeWidth)
        arrowheadYPath.reset()
        arrowheadYPath.moveTo(-10f, dataList.max() * unitH - 15f)
        arrowheadYPath.lineTo(0f, dataList.max() * unitH)
        arrowheadYPath.lineTo(10f, dataList.max() * unitH - 15f)
        canvas.drawPath(arrowheadYPath, outPathLine)
        canvas.restore()

        //2、绘制横坐标
        canvas.drawLine(0f, 0f, scaleWidth * (dataList.size-1), 0f, fillPathPaint)
        //3、绘制月份
        dataList.forEachIndexed { index, value ->
            canvas.save()//先将之前的画布坐标系状态进行保存，好接下来进行随意变换画布状态，最后通过canvas.restore恢复当前状态。
            canvas.scale(1f, -1f)
            val centerX = index * scaleWidth
            val contextText = "${index + 1}月"
            val textRect = Rect()
            textPaint.getTextBounds(contextText, 0, contextText.length, textRect)
            val textWidth = textRect.width()
            val textHeight = textRect.height()
            canvas.drawText(
                contextText,
                centerX - textWidth / 2f,
                textHeight.toFloat() + textMarginX,
                textPaint
            )
            //恢复上次存储的画布状态
            canvas.restore()
        }
    }
}