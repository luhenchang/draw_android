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
import java.util.Random

class CustomParticleView : View {
    private lateinit var particle: Particle
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
        animator.repeatCount = -1
        animator.interpolator = LinearInterpolator()
        animator.addUpdateListener {
            //updateParticle(it.animatedValue as Float)
            //updateParticleAcceleration(it.animatedValue as Float)
            updateAllParticle()
            //刷新绘制画布内容
            invalidate()
        }
    }

    private fun updateParticle(t: Float) {
        val pointX = particle.pointX + particle.velocity
        particle = particle.copy(pointX = pointX)
        Log.e("结果=", particle.pointX.toString())
    }

    //加速度
    private fun updateParticleAcceleration(t: Float) {
        val newPointY = particle.pointY + particle.velocity
        val newVelocity = particle.velocity + particle.acceleration
        particle = particle.copy(pointY = newPointY, velocity = newVelocity)
        Log.e("结果=", particle.pointX.toString())
    }

    //更新所有的粒子
    private fun updateAllParticle() {
        particleList.forEach {
            it.velocity += it.acceleration
            if (it.pointY + it.velocity - height/2f >= it.recycleDistance) {
                it.pointX = Random().nextInt(width).toFloat() //随机设置X值
                it.pointY = height / 2f
                it.velocity = Random().nextInt(10).toFloat() + 3
            }else{
                it.pointY += it.velocity
            }

        }
    }

    private var particleList = ArrayList<Particle>()
    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
        super.onSizeChanged(w, h, oldw, oldh)
        repeat(500) {
            val particle = Particle(
                radius = Random().nextInt(10).toFloat() + 3,
                pointX = Random().nextInt(width).toFloat(),//横轴方向随机分布在画布水平线上
                pointY = height / 2f,
                velocity = Random().nextInt(10).toFloat() + 3,
                acceleration = 0.2f,
                color = Color.RED,
                recycleDistance = 500
            )
            particleList.add(particle)
        }
    }


    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        particleList.forEach {
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