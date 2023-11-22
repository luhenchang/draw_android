package com.example.draw_android.section05_canvas.f_event

import android.content.Context
import android.util.AttributeSet
import android.view.MotionEvent
import android.widget.RelativeLayout
import kotlin.math.atan2
import kotlin.math.sqrt


class EventZoomView @JvmOverloads constructor(
    context: Context?,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) :
    RelativeLayout(context, attrs, defStyleAttr) {
    // 属性变量
    private var translationX = 0f // 移动X
    private var translationY = 0f // 移动Y
    private var scale = 1f // 伸缩比例
    private var rotation = 0f // 旋转角度

    // 移动过程中临时变量
    private var actionX = 0f
    private var actionY = 0f
    private var spacing = 0f
    private var degree = 0f
    private var moveType = 0 // 0=未选择，1=拖动，2=缩放

    init {
        isClickable = true
    }

    override fun onInterceptTouchEvent(ev: MotionEvent): Boolean {
        parent.requestDisallowInterceptTouchEvent(true)
        return super.onInterceptTouchEvent(ev)
    }

    override fun onTouchEvent(event: MotionEvent): Boolean {
        when (event.action and MotionEvent.ACTION_MASK) {
            MotionEvent.ACTION_DOWN -> {
                moveType = 1
                actionX = event.rawX
                actionY = event.rawY
            }

            MotionEvent.ACTION_POINTER_DOWN -> {
                moveType = 2
                spacing = getSpacing(event)
                degree = getDegree(event)
            }

            MotionEvent.ACTION_MOVE -> if (moveType == 1) {
                translationX = translationX + event.rawX - actionX
                translationY = translationY + event.rawY - actionY
                setTranslationX(translationX)
                setTranslationY(translationY)
                actionX = event.rawX
                actionY = event.rawY
            } else if (moveType == 2) {
                scale = scale * getSpacing(event) / spacing
                scaleX = scale
                scaleY = scale
                rotation = rotation + getDegree(event) - degree
                if (rotation > 360) {
                    rotation = rotation - 360
                }
                if (rotation < -360) {
                    rotation = rotation + 360
                }
                setRotation(rotation)
            }

            MotionEvent.ACTION_UP, MotionEvent.ACTION_POINTER_UP -> moveType = 0
        }
        return super.onTouchEvent(event)
    }

    // 触碰两点间距离
    private fun getSpacing(event: MotionEvent): Float {
        //通过三角函数得到两点间的距离
        val x = event.getX(0) - event.getX(1)
        val y = event.getY(0) - event.getY(1)
        return sqrt((x * x + y * y).toDouble()).toFloat()
    }

    // 取旋转角度
    private fun getDegree(event: MotionEvent): Float {
        //得到两个手指间的旋转角度
        val delta_x = (event.getX(0) - event.getX(1)).toDouble()
        val delta_y = (event.getY(0) - event.getY(1)).toDouble()
        val radians = atan2(delta_y, delta_x)
        return Math.toDegrees(radians).toFloat()
    }
}

