package com.example.draw_android.section05_canvas.f_event

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.RectF
import android.util.AttributeSet
import android.util.Log
import android.view.MotionEvent
import android.view.View

class EventXYCanvas :
    View, EventDisallowInterceptListener {
    private val titleBox = RectF(100f, 100f, 300f, 200f) // 初始巨型框的位置和大小
    private val boxPaint = Paint().apply {
        color = Color.BLUE
        style = Paint.Style.STROKE
        strokeWidth = 5f
    }
    private var isDragging = false
    private var lastX = 0f
    private var lastY = 0f

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

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        canvas.drawRect(titleBox, boxPaint)
    }

    private var disallowIntercept: Boolean = false
    override fun requestDisallowInterceptTouchEvent(disallowIntercept: Boolean) {
        this.disallowIntercept = disallowIntercept
    }

    override fun performClick(): Boolean {
        return super.performClick()
    }

    override fun onTouchEvent(event: MotionEvent): Boolean {
        val x = event.x
        val y = event.y
        when (event.action) {
            MotionEvent.ACTION_DOWN -> {
                performClick()
                parent?.requestDisallowInterceptTouchEvent(disallowIntercept)
                if (titleBox.contains(x, y)) {
                    // 按下位置在巨型框内
                    isDragging = true
                    lastX = x
                    lastY = y
                }
            }

            MotionEvent.ACTION_MOVE -> {
                if (isDragging) {
                    // 移动巨型框
                    val dx = x - lastX
                    val dy = y - lastY
                    titleBox.offset(dx, dy)
                    lastX = x
                    lastY = y
                    invalidate()
                }
            }

            MotionEvent.ACTION_UP -> {
                isDragging = false
            }
        }

        return true
    }

}