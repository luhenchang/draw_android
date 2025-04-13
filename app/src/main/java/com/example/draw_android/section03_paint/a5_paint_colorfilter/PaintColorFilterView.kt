package com.example.draw_android.section03_paint.a5_paint_colorfilter

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.ColorFilter
import android.graphics.LightingColorFilter
import android.graphics.Paint
import android.graphics.PorterDuff
import android.graphics.PorterDuffColorFilter
import android.util.AttributeSet
import android.view.View

class PaintColorFilterView(context: Context?, attrs: AttributeSet?) :
    View(context, attrs) {
    private val mPaint = Paint()
    init {
        val length = PorterDuff.Mode.values().size
        mPaint.color = Color.RED
        mPaint.style = Paint.Style.FILL
    }

    @SuppressLint("DrawAllocation")
    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        //去掉了红色，红色本来是没有绿和蓝的，所以结果是黑色。
        val lightingFilter: ColorFilter = LightingColorFilter(0x00ffff, 0x000000)
        mPaint.setColorFilter(lightingFilter)
        canvas.drawCircle(200f, 100f, 50f, mPaint)
        //去掉红色但是后来有加了红色和绿色，结果为黄色
        val lightingFilter2: ColorFilter = LightingColorFilter(0x00ffff, 0xFFFF00)
        mPaint.setColorFilter(lightingFilter2)
        canvas.drawCircle(500f, 100f,50f, mPaint)
        //去掉红色但是后来增加了红色和蓝色所以结果为蓝红色
        val lightingFilter3: ColorFilter = LightingColorFilter(0x00ffff, 0xFF00FF)
        mPaint.setColorFilter(lightingFilter3)
        canvas.drawCircle(800f, 100f,50f, mPaint)
    }

}