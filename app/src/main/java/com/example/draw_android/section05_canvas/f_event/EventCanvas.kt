package com.example.draw_android.section05_canvas.f_event

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.util.AttributeSet
import android.view.MotionEvent
import android.view.View

class EventCanvas : View,EventDisallowInterceptListener {
    val paint = Paint().apply {
        color = Color.BLACK
        textSize = 46f
    }
    var content = "没有事件输入"

    constructor(context: Context) : super(context) {
    }

    constructor(context: Context, attrs: AttributeSet?) : super(context, attrs) {
    }

    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int) : super(
        context,
        attrs,
        defStyleAttr
    ) {
    }

    override fun performClick(): Boolean {
        return super.performClick()
    }

    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
        super.onSizeChanged(w, h, oldw, oldh)
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        val textWidth = paint.measureText(content)
        canvas.drawText(content, width / 2f - textWidth / 2, height / 2f, paint)
    }

    override fun onTouchEvent(event: MotionEvent): Boolean {
        // 处理触摸事件
        when (event.action) {
            MotionEvent.ACTION_DOWN -> {
                performClick()
                parent?.requestDisallowInterceptTouchEvent(disallowIntercept)
                // 按下时的处理
                content = "ACTION_DOWN::event(x=${event.x},y=${event.y}}"
            }

            MotionEvent.ACTION_MOVE -> {
                content = "ACTION_MOVE::event(x=${event.x},y=${event.y}}"
                return false
            }

            MotionEvent.ACTION_UP -> {
                // 抬起时的处理
                content = "ACTION_UP::event(x=${event.x},y=${event.y}}"
                return false
            }

            MotionEvent.ACTION_CANCEL -> {
                content = "ACTION_CANCEL::event(x=${event.x},y=${event.y}}"
            }

            MotionEvent.ACTION_OUTSIDE -> {
                // 抬起时的处理
                content = "ACTION_OUTSIDE::event(x=${event.x},y=${event.y}}"
            }
        }
        invalidate()
        // 返回 false，表示不消费事件，让事件继续传递
        return true
    }

    private var disallowIntercept: Boolean = false
    override fun requestDisallowInterceptTouchEvent(disallowIntercept: Boolean) {
        this.disallowIntercept = disallowIntercept
    }
}