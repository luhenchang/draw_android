package com.example.draw_android

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.CornerPathEffect
import android.graphics.LightingColorFilter
import android.graphics.Paint
import android.graphics.Path
import android.graphics.PathEffect
import android.graphics.PathMeasure
import android.graphics.RectF
import android.util.AttributeSet
import android.util.Log
import android.view.View

class ArcView : View {
    val paint = Paint().apply {
        style = android.graphics.Paint.Style.STROKE
        color = android.graphics.Color.BLUE
        colorFilter = LightingColorFilter(0xffffff,0x0000f0)
        strokeWidth = 11f
        isAntiAlias = true
    }
    val paintShadow = Paint().apply {
        style = android.graphics.Paint.Style.FILL
        color = android.graphics.Color.BLACK

        setShadowLayer(2f, 2f, 2f, Color.GRAY)
        strokeWidth = 1f
        isAntiAlias = true
    }

    val innerOnlyPaint = Paint().apply {
        style = android.graphics.Paint.Style.STROKE
        color = android.graphics.Color.BLACK
        strokeCap = android.graphics.Paint.Cap.SQUARE
        setShadowLayer(2f, -1f, -1f, Color.BLACK)
        strokeWidth = 1f
        isAntiAlias = true
    }

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

    private var sweepAngle = 0f
    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        canvas.translate(width / 2f, height / 2f)
        val rectF = RectF(-200f, -100f, 200f, 100f)
        //canvas.drawRect(rectF, paint)
        if (sweepAngle < 359) {
            //canvas.drawArc(rectF, 0f, sweepAngle, true, paint)
            sweepAngle += 0.5f
            invalidate()
        }
        val path = Path()
        path.moveTo(0f,0f)
        path.arcTo(rectF,0f,90f,false)
        //path.addArc(rectF,0f,90f)
        path.close()
        canvas.drawPath(path,paint)
    }

    fun drawRect(canvas: Canvas, rectF: RectF) {
        return
        val innerF = RectF(-80f, -80f, 80f, 80f)
        val path = Path().apply {
            moveTo(100f, 0f)
            addArc(rectF, 0f, 80f)
            lineTo(0f, 100f)
        }
        val innerPath = Path()
        //innerPath.addCircle(0f, 0f, 100f, Path.Direction.CW)
        innerPath.addArc(innerF, 0f, 90f)
        //测量
        val measurePath = PathMeasure(innerPath, false)
        path.lineTo(0f, 80f)
        var pathLength = measurePath.length - 5
        val pos1 = FloatArray(2)
        val tan1 = FloatArray(2)
        measurePath.getPosTan(pathLength, pos1, tan1)
        path.lineTo(pos1[0], pos1[1])
        Log.e("pathLength=", pathLength.toString())
        val innerOnlyPath = Path()
        innerOnlyPath.moveTo(pos1[0], pos1[1])
        while (pathLength > 0) {
            val pos = FloatArray(2)
            val tan = FloatArray(2)
            Log.e("pathLengthMeasure=", pathLength.toString())
            measurePath.getPosTan(pathLength, pos, tan)
            if (pos.isNotEmpty()) {
                path.lineTo(pos[0], pos[1])
                innerOnlyPath.lineTo(pos[0], pos[1])
            }
            pathLength--
        }
        path.lineTo(100f, 0f)
        path.close()

        canvas.drawPath(path, paint)
        //canvas.drawPath(path, paintShadow)


        //canvas.drawPath(innerOnlyPath,innerOnlyPaint)

        val rectFLarger = RectF(-120f, -120f, 120f, 120f)

        val rectPath = Path().apply {
            addRect(rectFLarger, Path.Direction.CW)
        }
        canvas.drawPath(rectPath, paint)
    }
}