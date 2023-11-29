package com.example.draw_android.section05_canvas.f_event

import android.content.Context
import android.view.MotionEvent
import kotlin.math.atan2


class RotateRotateDetector constructor(
    val context: Context,
    private val listener: OnRotateGestureListener
) {
    interface OnRotateGestureListener {
        /**
         * Responds to scaling events for a gesture in progress.
         * Reported by pointer motion.
         *
         * @param detector The detector reporting the event - use this to
         * retrieve extended info about event state.
         * @return Whether or not the detector should consider this event
         * as handled. If an event was not handled, the detector
         * will continue to accumulate movement until an event is
         * handled. This can be useful if an application, for example,
         * only wants to update scaling factors if the change is
         * greater than 0.01.
         */
        fun onRotate(detector: RotateRotateDetector): Boolean

        /**
         * Responds to the beginning of a scaling gesture. Reported by
         * new pointers going down.
         *
         * @param detector The detector reporting the event - use this to
         * retrieve extended info about event state.
         * @return Whether or not the detector should continue recognizing
         * this gesture. For example, if a gesture is beginning
         * with a focal point outside of a region where it makes
         * sense, onRotateBegin() may return false to ignore the
         * rest of the gesture.
         */
        fun onRotateBegin(detector: RotateRotateDetector): Boolean

        /**
         * Responds to the end of a scale gesture. Reported by existing
         * pointers going up.
         *
         * Once a scale has ended, [RotateGestureDetector.getFocusX]
         * and [RotateGestureDetector.getFocusY] will return focal point
         * of the pointers remaining on the screen.
         *
         * @param detector The detector reporting the event - use this to
         * retrieve extended info about event state.
         */
        fun onRotateEnd(detector: RotateRotateDetector)
    }

    private var eventModeType: EventModeType = EventModeType.DefaultEvent
    private var startAngle = 0f
    private var rotateFactor = 0f
    fun getRotateFactor(): Float {
        return rotateFactor
    }

    fun onTouchEvent(event: MotionEvent): Boolean {
        when (event.action and MotionEvent.ACTION_MASK) {
            MotionEvent.ACTION_DOWN -> {
                eventModeType = EventModeType.DownEvent
            }

            MotionEvent.ACTION_POINTER_DOWN -> {
                //获取与X轴的夹角
                startAngle = getRotateAndXAxis(event)
                listener.onRotateBegin(this)
                eventModeType = EventModeType.PointerDownEvent
            }

            MotionEvent.ACTION_MOVE -> {
                if (eventModeType == EventModeType.PointerDownEvent || eventModeType == EventModeType.MoveEvent) {
                    eventModeType = EventModeType.MoveEvent
                    val currentAngle = getRotateAndXAxis(event)
                    //计算旋转因子
                    rotateFactor = currentAngle - startAngle
                    listener.onRotate(this)
                    startAngle = currentAngle
                }
            }

            MotionEvent.ACTION_UP,
            MotionEvent.ACTION_CANCEL,
            MotionEvent.ACTION_POINTER_UP -> {
                listener.onRotateEnd(this)
                eventModeType = EventModeType.EndEvent
            }
        }
        return true
    }

    private fun getRotateAndXAxis(event: MotionEvent): Float {
        val deltaX = event.getX(1) - event.getX(0)
        val deltaY = event.getY(1) - event.getY(0)
        val angle = atan2(deltaY.toDouble(), deltaX.toDouble())
        return Math.toDegrees(angle).toFloat()
    }

    sealed class EventModeType {
        object DefaultEvent : EventModeType()
        object DownEvent : EventModeType()
        object PointerDownEvent : EventModeType()
        object MoveEvent : EventModeType()
        object EndEvent : EventModeType()
    }
}