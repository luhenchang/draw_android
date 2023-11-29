package com.example.draw_android.section05_canvas.f_event

import android.content.Context
import android.view.View

sealed class ViewName(val value: Int) {
    object EventCanvas : ViewName(0)
    object EventXYCanvas : ViewName(1)
    object EventScaleCanvas : ViewName(2)
    object EventRotateView : ViewName(3)
    object EventExample : ViewName(4)
}

fun getVisibleView(viewName: Int, context: Context): View {
    return when (viewName) {
        ViewName.EventCanvas.value -> EventCanvas(context)
        ViewName.EventXYCanvas.value -> EventXYCanvas(context)
        ViewName.EventScaleCanvas.value -> EventScaleCanvas(context)
        ViewName.EventRotateView.value -> EventRotateView(context)
        ViewName.EventExample.value -> EventExampleView(context)
        else -> {
            View(context)
        }
    }
}