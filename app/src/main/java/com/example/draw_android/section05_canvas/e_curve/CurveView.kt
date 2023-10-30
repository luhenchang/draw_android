package com.example.draw_android.section05_canvas.e_curve

import android.content.Context
import android.graphics.*
import android.util.AttributeSet
import android.util.Log
import android.view.View
import kotlin.math.pow
import kotlin.math.sqrt

/**
 * Created by wang fei on 2022/4/21.
 */
class CurveView constructor(context: Context, attributeSet: AttributeSet) :
    View(context, attributeSet) {

    //网格的宽度
    private var mWidth = 0
    private var mHeight = 0
    private var sizeChange = false
    private lateinit var rect: RectF
    val paint = Paint().apply {
        color = Color.GRAY
        style = Paint.Style.FILL
        isAntiAlias = true
        textSize = 24f
    }

    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
        super.onSizeChanged(w, h, oldw, oldh)
        if (mWidth != w) {
            sizeChange = true
        }
        mWidth = w
        mHeight = h
        rect = RectF(0f, 0f, width.toFloat(), width.toFloat())
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        if (sizeChange) {
            Log.e("onDraw=","001")
            BaseCanvasView.drawBaseBg(canvas, mWidth, mHeight)
            sizeChange = false
        }
        drawLine(canvas)
        drawCircle(canvas)
    }
    //圆方程式:(x-a)^2+(y-b)^2=r^2
    private fun drawCircle(canvas: Canvas) {
        val gPaint = Paint().apply {
            color = Color.BLACK
            style = Paint.Style.STROKE
            strokeWidth = 2f
            isAntiAlias = true
        }
        //绘制圆圈
        number.forEach { t ->
            val point = PointF()
            val pointDown = PointF()
            //(x-80)2+（y-80）2=1602
            point.x = t.toFloat()
            pointDown.x = t.toFloat()
            //y计算应该不用我说吧。
            point.y =
                sqrt(160.0.pow(2.0).toFloat() - ((point.x - 80).toDouble()).pow(2.0)).toFloat() + 80
            pointDown.y = -sqrt(
                160.0.pow(2.0).toFloat() - ((pointDown.x - 80).toDouble()).pow(2.0)
            ).toFloat() + 80
            canvas.drawPoint(point.x, point.y, gPaint)
            canvas.drawPoint(pointDown.x, pointDown.y, gPaint)
        }
    }

    private lateinit var pointList: ArrayList<PointF>
    private var number = -420..420
    //直线方程y=2x-80
    private fun drawLine(canvas: Canvas) {
        pointList = ArrayList()
        //绘制方程式y=10x+20
        val gPaint = Paint().apply {
            color = Color.BLACK
            style = Paint.Style.STROKE
            strokeWidth = 2f
            isAntiAlias = true
        }
        number.forEach { t ->
            val point = PointF()
            point.x = t.toFloat()
            point.y = 2f * t - 80
            pointList.add(point)
            canvas.drawPoint(point.x, point.y, gPaint)
        }
    }


}