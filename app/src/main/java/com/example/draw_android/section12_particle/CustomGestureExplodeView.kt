package com.example.draw_android.section12_particle

import android.animation.ValueAnimator
import android.content.Context
import android.graphics.BlurMaskFilter
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.CornerPathEffect
import android.graphics.LinearGradient
import android.graphics.Paint
import android.graphics.Path
import android.graphics.PathMeasure
import android.graphics.Rect
import android.graphics.Shader
import android.util.AttributeSet
import android.util.Log
import android.view.MotionEvent
import android.view.View
import android.view.animation.LinearInterpolator
import androidx.core.graphics.blue
import androidx.core.graphics.green
import androidx.core.graphics.red
import java.util.Random
import kotlin.math.min

//粒子纠缠pester
data class GestureParticle(
    var radius: Float = 0f,//圆形粒子，大小
    var pointX: Float = 0f,//坐标x
    var pointY: Float = 0f,//坐标y
    var pointXX: Float = 0f,//坐标x
    var pointYY: Float = 0f,//坐标y
    var angle: Float = 0f,//角度
    var velocityX: Float = 0f,//x轴方向速度
    var velocityY: Float = 0f,//y方向速度
    var acceleration: Float = 0f,//加速度
    var color: Int = Color.RED,//纹理、颜色
    var recycleDistance: Int = 0,//超出最大距离回收。
    var t: Int = 0//t记录运动的时间单位  秒相对于屏幕刷新很大了
)

class CustomGestureExplodeView : View {
    private var stopLoading: Boolean = false
    private var mProgress: Int = 0
    private var textColor: Int = Color.parseColor("#FFBB86FC")
    val paint = Paint().apply {
        color = Color.parseColor("#FFBB86FC")
        style = Paint.Style.FILL
        strokeWidth = 1f
        isAntiAlias = true
        maskFilter = BlurMaskFilter(1f, BlurMaskFilter.Blur.NORMAL)
    }
    val textPaint = Paint().apply {
        color = Color.parseColor("#FFBB86FC")
        style = Paint.Style.FILL
        strokeWidth = 2f
        textSize = 40f
        isAntiAlias = true
        pathEffect = CornerPathEffect(5f)
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
            updateGestureParticle()
            invalidate()
        }
    }

    private fun updateGestureParticle() {
        val iterator = particleList.iterator()
        if (!iterator.hasNext()) {
            Log.e("粒子消失？", "yes")
            animator.cancel()
        }
        while (iterator.hasNext()) {
            val particle = iterator.next()
            particle.pointX += particle.velocityX
            particle.pointY += particle.velocityY
            particle.t += 1
            val alpha = (255 * (1 - min(particle.t, 50) / 50f))
            particle.color = Color.argb(
                alpha.toInt(),
                particle.color.red,
                particle.color.green,
                particle.color.blue
            )
            if (particle.t > 50) {
                iterator.remove()
            }
        }
    }

    //更新所有的粒子位置
    private fun createGestureParticle(x: Float, y: Float) {
        repeat(10) {
            val particle = GestureParticle(
                radius = Random().nextInt(10).toFloat() + 3,
                pointX = x,
                pointY = y,
                angle = Random().nextInt(360).toFloat(),
                velocityX = Random().nextFloat() * 2 - 1,
                velocityY = Random().nextFloat() * 2 - 1,
                acceleration = 0.02f,
                color = Color.argb(
                    255,
                    Random().nextInt(255),
                    Random().nextInt(255),
                    Random().nextInt(255)
                ),
                recycleDistance = 500,
                t = 0
            )
            particleList.add(particle)
        }
        animator.start()
    }

    private lateinit var pathMeasure: PathMeasure
    private var particleList = ArrayList<GestureParticle>()
    private val circlePath = Path()
    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
        super.onSizeChanged(w, h, oldw, oldh)
        pathMeasure = PathMeasure()
        circlePath.reset()
        circlePath.addCircle(w / 2f, h / 2f, 100f, Path.Direction.CW)
        pathMeasure.setPath(circlePath, false)
    }

    var distance = 0f
    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        particleList.forEach {
            Log.e("it.pointX=", it.pointX.toString())
            Log.e("it.pointY=", it.pointY.toString())
            canvas.drawCircle(
                it.pointX,
                it.pointY,
                it.radius,
                paint.apply { color = it.color })
        }

        drawProgress(canvas)
        if (stopLoading) {
            animator.cancel()
            return
        }
        //根据距离获取其坐标点
        if (distance <= pathMeasure.length) {
            val pos = FloatArray(2)
            val tan = FloatArray(2)
            pathMeasure.getPosTan(distance, pos, tan)
            createGestureParticle(pos[0], pos[1])
            distance += 30
        } else {
            distance = 0f
        }

        if (mProgress >= 100) {
            stopLoading = true
        }
    }

    //绘制进度条
    private fun drawProgress(canvas: Canvas) {
        val context = "${mProgress}%"
        val rect = Rect()
        textPaint.getTextBounds(context, 0, context.length, rect)
        canvas.drawText(
            context,
            width / 2f - rect.width() / 2f,
            height / 2f + rect.height() / 2f,
            textPaint.apply { color = textColor })
    }

    fun setProgress(progress: Int) {
        if (progress % 10 == 0) {
            textColor = Color.argb(
                255,
                Random().nextInt(255),
                Random().nextInt(255),
                Random().nextInt(255)
            )
            textPaint.apply {
                shader = LinearGradient(
                    width / 2f - 100, 0f, width / 2f + 100,
                    0f,
                    intArrayOf(
                        Color.argb(
                            255,
                            Random().nextInt(255),
                            Random().nextInt(255),
                            Random().nextInt(255)
                        ),
                        Color.argb(
                            255,
                            Random().nextInt(255),
                            Random().nextInt(255),
                            Random().nextInt(255)
                        ),
                        Color.argb(
                            255,
                            Random().nextInt(255),
                            Random().nextInt(255),
                            Random().nextInt(255)
                        )
                    ),
                    floatArrayOf(0f, 0.4f, 0.8f),
                    Shader.TileMode.CLAMP
                )
                setShadowLayer(
                    10f, Random().nextInt(5) + 3f, Random().nextInt(5) + 3f, Color.argb(
                        255,
                        Random().nextInt(255),
                        Random().nextInt(255),
                        Random().nextInt(255)
                    )
                )
            }
        }
        mProgress = progress
    }

    override fun performClick(): Boolean {
        return super.performClick()
    }

    override fun onTouchEvent(event: MotionEvent): Boolean {
        when (event.action) {
            MotionEvent.ACTION_UP -> {
                particleList
                invalidate()
            }

            MotionEvent.ACTION_MOVE -> {
                createGestureParticle(event.x, event.y)
            }

            MotionEvent.ACTION_DOWN -> {
                performClick()
                return true
            }

        }
        return true
    }

}