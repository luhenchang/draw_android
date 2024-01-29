package com.example.draw_android.section05_canvas.g_clip

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.Path
import android.util.AttributeSet
import androidx.appcompat.widget.*
import kotlin.math.min

/**
 * Created by wang fei on 2022/12/27.
 */
class CircleImageView :
    AppCompatImageView {
    val paint = Paint().apply {
        color = Color.GRAY
        style = Paint.Style.FILL
        isAntiAlias = true
        textSize = 24f
    }

    val clipPath = Path()
    constructor(context: Context) : super(context)

    constructor(context: Context, attrs: AttributeSet?) : super(context, attrs)

    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int) : super(
        context,
        attrs,
        defStyleAttr
    )

    override fun onDraw(canvas: Canvas) {
        val radius = (min(width.toDouble(), height.toDouble()) / 2.0f).toFloat()
        clipPath.reset()
        clipPath.addCircle(width / 2.0f, height / 2.0f, radius, Path.Direction.CW)
        canvas.clipPath(clipPath)
        super.onDraw(canvas)
    }
}