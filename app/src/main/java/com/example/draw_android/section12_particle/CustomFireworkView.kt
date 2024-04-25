package com.example.draw_android.section12_particle

import android.animation.ValueAnimator
import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.util.AttributeSet
import android.util.Log
import android.view.MotionEvent
import android.view.View
import android.view.animation.LinearInterpolator
import androidx.core.graphics.blue
import androidx.core.graphics.green
import androidx.core.graphics.red
import kotlin.math.cos
import kotlin.math.sin
import kotlin.random.Random

class CustomFireworkView  : View {
    val paint = Paint().apply {
        color = Color.parseColor("#FFBB86FC")
        style = Paint.Style.FILL
        strokeWidth = 1f
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
        animator.repeatCount = 0
        animator.interpolator = LinearInterpolator()
        animator.addUpdateListener {
            updateAllExplodeParticle()
            invalidate()
        }
    }

    //更新所有的粒子位置
    private fun updateAllExplodeParticle() {
        particleList.forEach {
            //初速度和加速度都是各自方向上的，并非水平或者垂直方向
            it.t += 1//控制时间叠加过快。
            val s = it.velocity * it.t + (1 / 2) * it.acceleration * (it.t * it.t)//s=1/2*a*t2
            if (s>it.recycleDistance) {
                it.pointX = 0f
                it.pointY = 0f
                it.t = 0
                it.velocity = Random.nextInt(10).toFloat() + 3
                //根据限制距离和逐渐增大的位移距离做比，让透明度从1到0
                it.color = Color.WHITE
            }else{
                it.pointX = s * cos(it.angle)
                it.pointY = s * sin(it.angle)
                it.velocity +=it.acceleration
                it.color = Color.argb((255*(s/it.recycleDistance)).toInt(),
                    it.color.red,
                    it.color.green,
                    it.color.blue)
            }
        }
    }

    private var particleList = ArrayList<ExplodeParticle>()
    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
        super.onSizeChanged(w, h, oldw, oldh)
        repeat(21){angleIndex->
            repeat(500) {
                val particle = ExplodeParticle(
                    radius = 1f,
                    pointX = 0f,
                    pointY = 0f,
                    angle = angleIndex*18f,//烟花
                    velocity = java.util.Random().nextInt(10).toFloat() + 3,
                    acceleration = 0.02f,
                    color = Color.WHITE,
                    recycleDistance = 300,
                    t = 0
                )
                particleList.add(particle)
            }
        }

    }


    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        canvas.translate(width/2f,height/2f)
        particleList.forEach {
            Log.e("it.pointX=",it.pointX.toString())
            Log.e("it.pointY=",it.pointY.toString())
            canvas.drawCircle(
                it.pointX,
                it.pointY,
                it.radius,
                paint.apply { color = it.color })
        }

    }

    override fun performClick(): Boolean {
        return super.performClick()
    }

    override fun onTouchEvent(event: MotionEvent): Boolean {
        when (event.action) {
            MotionEvent.ACTION_UP -> {

            }

            MotionEvent.ACTION_MOVE -> {

            }

            MotionEvent.ACTION_DOWN -> {
                performClick()
                animator.start()
                return true
            }

        }
        return true
    }

}