package com.example.draw_android.section03_paint.a5_paint_colorfilter

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Canvas
import android.graphics.ColorMatrix
import android.graphics.ColorMatrixColorFilter
import android.graphics.Paint
import android.graphics.Rect
import android.util.AttributeSet
import android.view.View
import com.example.draw_android.R


class ColorMatrixSaturationView(context: Context?, attrs: AttributeSet?) :
    View(context, attrs) {
    private var mPaint: Paint? = null
    private var mBitmap: Bitmap? = null
    private val colorMatrix: ColorMatrix = ColorMatrix()
    private val matrixColorFilter = arrayOfNulls<ColorMatrixColorFilter>(24)
    private val padding = 5
    private val rectWidth = 300

    init {
        init()
    }

    private fun init() {
        mPaint = Paint(Paint.ANTI_ALIAS_FLAG)
        mBitmap = BitmapFactory.decodeResource(resources, R.drawable.wuman)
        for (i in 0..23) {
            if (i<4){
                colorMatrix.setSaturation(i*0.2f)
            }else if (i < 8){
                colorMatrix.setSaturation(i*0.5f)
            }else if (i < 12){
                colorMatrix.setSaturation(i*1f)
            }else if (i < 16){
                colorMatrix.setSaturation(i*1.5f)
            }else {
                colorMatrix.setSaturation(i*2f)
            }
            matrixColorFilter[i] = ColorMatrixColorFilter(colorMatrix)
        }
    }

    @SuppressLint("DrawAllocation")
    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)

        mBitmap?.let { bitmap ->
            val srcRect = Rect(0, 0, bitmap.width, bitmap.height)
            for (index in 0..23) {
                mPaint?.colorFilter = matrixColorFilter[index]
                val dstRect = Rect(
                    (index % 4) * (rectWidth + padding),
                    (index / 4) * (rectWidth + padding),
                    (index % 4) * (rectWidth + padding) + rectWidth,
                    (index / 4) * (rectWidth + padding) + rectWidth
                )
                canvas.drawBitmap(
                    bitmap,
                    srcRect, dstRect, mPaint
                )
            }
        }

    }
}