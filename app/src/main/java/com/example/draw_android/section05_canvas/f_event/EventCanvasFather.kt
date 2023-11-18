package com.example.draw_android.section05_canvas.f_event

import android.content.Context
import android.util.AttributeSet
import android.view.MotionEvent
import android.widget.LinearLayout
import android.widget.Toast

class EventCanvasFather constructor(context: Context, att: AttributeSet) :
    LinearLayout(context, att) {
    override fun onInterceptTouchEvent(ev: MotionEvent?): Boolean {
        return false
    }
    override fun onTouchEvent(event: MotionEvent): Boolean {
        when (event.action) {
            MotionEvent.ACTION_DOWN -> {
                // 按下时的处理
                Toast.makeText(context, "Touch Down Father", Toast.LENGTH_SHORT).show()
            }

            MotionEvent.ACTION_UP -> {
                // 抬起时的处理
                Toast.makeText(context, "Touch Up Father", Toast.LENGTH_SHORT).show()
            }
        }
        return true
    }
}