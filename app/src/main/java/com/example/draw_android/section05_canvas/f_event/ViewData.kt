package com.example.draw_android.section05_canvas.f_event

import android.content.Context
import android.view.View
import com.example.draw_android.section05_canvas.e_curve.CurveExampleView
import com.example.draw_android.section05_canvas.e_curve.CurveView
import com.example.draw_android.section05_canvas.g_clip.CircleImageView

sealed class ViewName(val value: Int) {
    object EventCanvas : ViewName(0)
    object EventXYCanvas : ViewName(1)
    object EventScaleCanvas : ViewName(2)
    object EventRotateView : ViewName(3)
    object PorterDuffDstOutView : ViewName(4)
    object PageTurnView : ViewName(5)

    object EventExample : ViewName(6)
    object EventBitmapExample : ViewName(7)
    object CurveView:ViewName(8)
    object CurveExampleView:ViewName(9)
    object ClipAPIStudyView:ViewName(10)

}

fun getVisibleView(viewName: Int, context: Context): View {
    return when (viewName) {
        ViewName.EventCanvas.value -> EventCanvas(context)
        ViewName.EventXYCanvas.value -> EventXYCanvas(context)
        ViewName.EventScaleCanvas.value -> EventScaleCanvas(context)
        ViewName.EventRotateView.value -> EventRotateView(context)
        ViewName.PorterDuffDstOutView.value -> PorterDuffDstOutView(context)
        ViewName.PageTurnView.value -> PageTurnView(context)
        ViewName.EventExample.value -> EventExampleView(context)
        ViewName.EventBitmapExample.value -> BitmapClippingView(context)
        ViewName.CurveView.value->CurveView(context)
        ViewName.CurveExampleView.value->CurveExampleView(context)
        ViewName.ClipAPIStudyView.value->CircleImageView(context)
        else -> {
            View(context)
        }
    }
}