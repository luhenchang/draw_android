package com.example.draw_android.section03_paint.a5_paint_colorfilter

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.ColorMatrixColorFilter
import android.graphics.Paint
import android.util.AttributeSet
import android.view.View
import com.example.draw_android.R

class PaintColorFilter3View(context: Context?, attrs: AttributeSet?) :
    View(context, attrs) {
    private val mBitmap: Bitmap
    private val mPaint = Paint()
    private var colorMatrix: FloatArray = floatArrayOf(
        1f, 0f, 0f, 0f, 0f,  //red
        0f, 0f, 0f, 0f, 0f,  //green
        0f, 0f, 0f, 0f, 0f,  //blue
        0f, 0f, 0f, 1f, 0f //alpha
    )
    //把红色留下，绿蓝色移除，透明度不变。
    private val mLightingColorFilter: ColorMatrixColorFilter = ColorMatrixColorFilter(colorMatrix)

    init {
        mPaint.color = Color.RED
        mPaint.style = Paint.Style.FILL
        mBitmap = BitmapFactory.decodeResource(resources, R.drawable.wuman);
    }

    @SuppressLint("DrawAllocation")
    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        mPaint.setColorFilter(mLightingColorFilter);
        canvas.drawBitmap(mBitmap, 0f, 0f, mPaint);
    }

}