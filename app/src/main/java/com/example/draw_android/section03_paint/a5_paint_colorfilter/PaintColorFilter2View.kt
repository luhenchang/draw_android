package com.example.draw_android.section03_paint.a5_paint_colorfilter

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.PorterDuff
import android.graphics.PorterDuffColorFilter
import android.graphics.Rect
import android.util.AttributeSet
import android.view.View
import com.example.draw_android.R

class PaintColorFilter2View(context: Context?, attrs: AttributeSet?) :
    View(context, attrs) {
    private val mBitmap: Bitmap
    private val mPaint = Paint()
    private val mLightingColorFilter: Array<PorterDuffColorFilter?>

    private val length = PorterDuff.Mode.values().size
    private val padding = 5
    private val rectWidth = 300

    init {
        mPaint.color = Color.RED
        mPaint.style = Paint.Style.FILL
        mBitmap = BitmapFactory.decodeResource(resources, R.drawable.wuman);
        mLightingColorFilter = arrayOfNulls(length)
        var index = 0
        PorterDuff.Mode.values().forEach { mode ->
            mLightingColorFilter[index++] = PorterDuffColorFilter(Color.RED, mode)
        }
    }

    @SuppressLint("DrawAllocation")
    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        val srcRect = Rect(0, 0, mBitmap.width, mBitmap.height)
        for (index in 0 until length) {
            mPaint.setColorFilter(mLightingColorFilter[index]);
            val dstRect = Rect(
                (index % 4) * (rectWidth + padding),
                (index / 4) * (rectWidth + padding),
                (index % 4) * (rectWidth + padding) + rectWidth,
                (index / 4) * (rectWidth + padding) + rectWidth
            )
            canvas.drawBitmap(mBitmap, srcRect, dstRect, mPaint)
        }
    }

}