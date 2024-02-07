package com.example.draw_android.section05_canvas_clip.b_canvas_clip_examples

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.util.AttributeSet
import android.view.MotionEvent
import android.view.View
import android.widget.Scroller

class BallView(context: Context, attrs: AttributeSet) : View(context, attrs) {
    private val radius = 50f
    private val paint = Paint().apply {
        color = Color.BLUE
        isAntiAlias = true
    }

    //提供了滚动操作
    private val scroller = Scroller(context)
    override fun performClick(): Boolean {
        return super.performClick()
    }
    private var initContentPositionX = 0
    private var startX = 0f

    override fun onTouchEvent(event: MotionEvent): Boolean {
        when (event.action) {
            MotionEvent.ACTION_DOWN -> {
                performClick()
                startX = event.x
            }

            MotionEvent.ACTION_MOVE -> {
               initContentPositionX = (event.x-startX).toInt()
               scrollTo(-initContentPositionX,0)
            }

            MotionEvent.ACTION_UP -> {
                startX = 0f
                return true
            }
        }
        return true
    }


    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        canvas.drawLine(0f, 0f, width.toFloat(), height.toFloat(), paint)
        canvas.drawCircle((width / 2).toFloat(), (height / 2).toFloat(), radius, paint)
    }
}
