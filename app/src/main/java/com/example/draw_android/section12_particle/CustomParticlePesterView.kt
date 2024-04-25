package com.example.draw_android.section12_particle

import android.animation.ValueAnimator
import android.content.Context
import android.graphics.BlurMaskFilter
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.Path
import android.graphics.PathMeasure
import android.os.Handler
import android.os.Looper
import android.util.AttributeSet
import android.util.Log
import android.view.MotionEvent
import android.view.View
import android.view.animation.LinearInterpolator
import androidx.core.graphics.blue
import androidx.core.graphics.green
import androidx.core.graphics.red
import kotlin.math.min
import kotlin.random.Random

class CustomParticlePesterView : View {
    val paintPoint = Paint().apply {
        color = Color.parseColor("#4BBB86FC")
        style = Paint.Style.STROKE
        strokeWidth = 2f
        isAntiAlias = true
        maskFilter = BlurMaskFilter(1f, BlurMaskFilter.Blur.NORMAL)
    }

    val paintPointFill = Paint().apply {
        color = Color.parseColor("#4BBB86FC")
        style = Paint.Style.FILL
        strokeWidth = 11f
        isAntiAlias = true
        maskFilter = BlurMaskFilter(6f, BlurMaskFilter.Blur.NORMAL)
    }

    val paint = Paint().apply {
        color = Color.parseColor("#FFBB86FC")
        style = Paint.Style.STROKE
        strokeWidth = 13f
        isAntiAlias = true
        maskFilter = BlurMaskFilter(6f, BlurMaskFilter.Blur.NORMAL)
    }
    private val paintBole = Paint().apply {
        color = Color.parseColor("#4BBB86FC")
        style = Paint.Style.STROKE
        strokeWidth = 20f
        isAntiAlias = true
        maskFilter = BlurMaskFilter(11f, BlurMaskFilter.Blur.NORMAL)
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

    private var animator = ValueAnimator.ofFloat(0f, 1f)//时间是变化的
    fun init() {
        animator.duration = 2000
        animator.repeatCount = -1
        animator.interpolator = LinearInterpolator()
        animator.addUpdateListener {

            //postInvalidate()
        }
    }
    private var handler: Handler = Handler(Looper.getMainLooper())
    private val refreshDelay: Long = 200 // 刷新延迟时间
    private fun updateGestureParticle() {
        newCirclePath.reset()
        val posStart = FloatArray(2)
        val posTan = FloatArray(2)
        pathMeasure.getPosTan(0f, posStart, posTan)
        newCirclePath.moveTo(posStart[0], posStart[1])
        repeat(100) {
            val pos = FloatArray(2)
            val tan = FloatArray(2)
            pathMeasure.getPosTan(pathMeasure.length * (it / 100f), pos, tan)
            newCirclePath.lineTo(pos[0] + Random.nextInt(10), pos[1] + Random.nextInt(10))
        }
        newCirclePath.close()
    }


    private lateinit var pathMeasure: PathMeasure
    private var particleList = ArrayList<GestureParticle>()
    private val circlePath = Path()

    private var newCirclePath = Path()
    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
        super.onSizeChanged(w, h, oldw, oldh)
        pathMeasure = PathMeasure()
        circlePath.reset()
        circlePath.addCircle(0f, 0f, 160f, Path.Direction.CW)
        pathMeasure.setPath(circlePath, false)
        val posStart = FloatArray(2)
        val posTan = FloatArray(2)
        pathMeasure.getPosTan(0f, posStart, posTan)
        newCirclePath.moveTo(posStart[0], posStart[1])
        repeat(100) {
            val pos = FloatArray(2)
            val tan = FloatArray(2)
            pathMeasure.getPosTan(pathMeasure.length * (it / 100f), pos, tan)
            newCirclePath.lineTo(pos[0] + Random.nextInt(10), pos[1] + Random.nextInt(10))
        }
        pathMeasure.setPath(newCirclePath, false)


        repeat(pathMeasure.length.toInt()) {
                val pos = FloatArray(2)
                val tan = FloatArray(2)
                pathMeasure.getPosTan(it.toFloat(), pos, tan)
                val x = pos[0]
                val y = pos[1]
            if(it<pathMeasure.length) {
                repeat(Random.nextInt(20)) {
                    val xx = if (x > 0) {
                        x + it
                    } else {
                        x - it
                    }
                    val yy = if (x > 0) {
                        (x + it) * y / x
                    } else {
                        (x - it) * y / x
                    }
                    val particle = GestureParticle(
                        radius = Random.nextInt(0, 4).toFloat(),
                        pointX = xx,
                        pointY = yy,
                        pointXX = xx,
                        pointYY = yy,
                        velocityX = Random.nextDouble(-6.0, 6.0).toFloat(),
                        velocityY = Random.nextDouble(-4.0, 4.0).toFloat(),
                        color = Color.parseColor("#4BBB86FC")
                    )
                    particleList.add(particle)
                }

            }
        }
        newCirclePath.close()
        //animator.start()
        handler.postDelayed(updateDegree(),refreshDelay)
    }

    private fun updateDegree(): () -> Unit = {
        degrees += 30f
        if (degrees >= 360) {
            degrees = 0f
        }
        handler.postDelayed(updateDegree(),refreshDelay)
    }

    var degrees = 0f
    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        canvas.translate(width / 2f, height / 2f)
        canvas.save()
        canvas.rotate(degrees)
        canvas.drawPath(newCirclePath, paintBole)
        canvas.drawPath(newCirclePath, paint)
        canvas.restore()
        particleList.forEach {
            canvas.drawCircle(it.pointX,it.pointY,it.radius,paintPointFill.apply {
                color = it.color
            })
        }
        updatePoints()
        postInvalidate()
    }

    override fun performClick(): Boolean {
        return super.performClick()
    }
    private fun updatePoints() {
        particleList.forEach {
            Log.e("it.pointX",it.pointX.toString())
            Log.e("it.pointXX",it.pointXX.toString())

            if ((it.pointX-it.pointXX)>160||(it.pointX-it.pointXX)<-160){
                it.pointX = it.pointXX
                it.pointY = it.pointYY
            }
            if ((it.pointY-it.pointYY)>160||(it.pointY-it.pointYY)<-160){
                it.pointX = it.pointXX
                it.pointY = it.pointYY
            }
            it.pointX += it.velocityX
            it.pointY += it.velocityY
            it.color = Color.argb((255-(min((it.pointX-it.pointXX),30f) /30)*255).toInt(),it.color.red,it.color.green,it.color.blue)
        }
    }
    override fun onTouchEvent(event: MotionEvent): Boolean {
        when (event.action) {
            MotionEvent.ACTION_UP -> {
                invalidate()
            }

            MotionEvent.ACTION_MOVE -> {

            }

            MotionEvent.ACTION_DOWN -> {
                // 启动定时任务
                //animator.start()
                performClick()
                return true
            }

        }
        return true
    }

}