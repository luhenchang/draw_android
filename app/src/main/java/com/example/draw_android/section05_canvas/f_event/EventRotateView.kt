package com.example.draw_android.section05_canvas.f_event

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.util.AttributeSet
import android.view.MotionEvent
import android.view.View
import kotlin.math.atan2

class EventRotateView constructor(context: Context, attributeSet: AttributeSet) :
    View(context, attributeSet) {
    private val paint = Paint()
    private var rotateAngle = 0f
    private var rotateDetector: RotateRotateDetector? = null

    init {
        paint.color = Color.RED
        paint.style = Paint.Style.FILL
        rotateDetector =
            RotateRotateDetector(context, object : RotateRotateDetector.OnRotateGestureListener {


                override fun onRotateBegin(detector: RotateRotateDetector): Boolean {
                    return true
                }

                override fun onRotate(detector: RotateRotateDetector): Boolean {
                    rotateAngle += detector.getRotateFactor()
                    invalidate()
                    return true
                }

                override fun onRotateEnd(detector: RotateRotateDetector) {

                }

            })
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        val centerX = width / 2f
        val centerY = height / 2f
        val rectSize = 200f
        canvas.rotate(rotateAngle, width / 2f, height / 2f)
        canvas.drawRect(
            centerX - rectSize / 2,
            centerY - rectSize / 2,
            centerX + rectSize / 2,
            centerY + rectSize / 2,
            paint
        )
    }

    override fun performClick(): Boolean {
        return super.performClick()
    }

    private var eventModeType: EventModeType = EventModeType.DefaultEvent
    private var startAngle = 0f
    override fun onTouchEvent(event: MotionEvent): Boolean {
        rotateDetector?.onTouchEvent(event)
        return true
//        when (event.action and MotionEvent.ACTION_MASK) {
//            MotionEvent.ACTION_DOWN -> {
//                performClick()
//                eventModeType = EventModeType.DownEvent
//            }
//
//            MotionEvent.ACTION_POINTER_DOWN -> {
//                //获取与X轴的夹角
//                startAngle = getRotateAndXAxis(event)
//                eventModeType = EventModeType.PointerDownEvent
//            }
//
//            MotionEvent.ACTION_MOVE -> {
//                if (eventModeType == EventModeType.PointerDownEvent || eventModeType == EventModeType.MoveEvent) {
//                    eventModeType = EventModeType.MoveEvent
//                    val currentAngle = getRotateAndXAxis(event)
//                    val angleDiff = currentAngle - startAngle
//                    rotateAngle += angleDiff
//                    invalidate()
//                    startAngle = currentAngle
//                }
//            }
//
//            MotionEvent.ACTION_UP -> {
//                eventModeType = EventModeType.EndEvent
//            }
//
//            MotionEvent.ACTION_POINTER_UP -> {
//                eventModeType = EventModeType.EndEvent
//            }
//        }
//
//        return true
    }

    private fun getRotateAndXAxis(event: MotionEvent): Float {
        //region 为了方便理解
        //val oneX = event.getX(0)
        //val oneY = event.getY(0)
        //val twoX = event.getX(1)
        //val twoY = event.getY(1)
        //val distanceX = abs(twoX - oneX)
        //val distanceY = abs(twoY - oneY)
        //val tan0 = tan((distanceY / distanceX).toDouble())
        //val angle = atan(tan0)
        //return Math.toDegrees(angle).toFloat()
        //endregion
        val deltaX = event.getX(1) - event.getX(0)
        val deltaY = event.getY(1) - event.getY(0)
        val angle = atan2(deltaY.toDouble(), deltaX.toDouble())
        return Math.toDegrees(angle).toFloat()
    }
}

sealed class EventModeType {
    object DefaultEvent : EventModeType()
    object DownEvent : EventModeType()
    object PointerDownEvent : EventModeType()
    object MoveEvent : EventModeType()
    object EndEvent : EventModeType()
}