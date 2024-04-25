package com.example.draw_android.section03_paint.a4_paint_effect

import android.content.Context
import android.graphics.Canvas
import android.util.AttributeSet
import android.view.View

class PaintEffectView : View {
    constructor(context: Context) : super(context) {
        init()
    }


    constructor(context: Context, attrs: AttributeSet?) : super(context, attrs) {
        init()
    }

    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int) : super(
        context,
        attrs,
        defStyleAttr
    ) {
        init()
    }

    private fun init() {

    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
    }
}