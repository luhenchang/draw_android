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


class ColorMatrixView(context: Context?, attrs: AttributeSet?) :
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
            colorMatrix.setScale(i * .1f, i * .1f, i * .1f, i * .1f)
            matrixColorFilter[i] = ColorMatrixColorFilter(colorMatrix)
        }
    }

    @SuppressLint("DrawAllocation")
    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)

        mBitmap?.let { bitmap ->
            val srcRect = Rect(0, 0, bitmap.width, bitmap.height)
            for (index in 0..23) {
                mPaint?.setColorFilter(matrixColorFilter[index])
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