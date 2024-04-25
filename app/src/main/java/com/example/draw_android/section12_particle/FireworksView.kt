package com.example.draw_android.section12_particle

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.Path
import android.graphics.RectF
import android.os.SystemClock
import android.text.TextPaint
import android.util.AttributeSet
import android.util.DisplayMetrics
import android.util.TypedValue
import android.view.View
import androidx.core.graphics.blue
import androidx.core.graphics.green
import androidx.core.graphics.red
import kotlin.math.abs
import kotlin.math.asin
import kotlin.math.cos
import kotlin.math.min
import kotlin.math.sin
import kotlin.math.sqrt
import kotlin.random.Random


class FireworksView @JvmOverloads constructor(
    context: Context?,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) :
    View(context, attrs, defStyleAttr), Runnable {
    private val mDM: DisplayMetrics
    private var mArcPaint: TextPaint? = null
    private val displayTime = 500L //控制时间，防止逃出边界
    private var clockTime: Long = 0
    private var isNextDrawingTimeScheduled = false
    private var mDrawerPaint: TextPaint? = null
    private var random: Random? = null
    val maxStartNum = 50
    var stars = arrayOfNulls<Star>(maxStartNum)
    private var isRefresh = true

    init {
        mDM = resources.displayMetrics
        initPaint()
        setOnClickListener { startPlay() }
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec)
        val widthMode = MeasureSpec.getMode(widthMeasureSpec)
        var widthSize = MeasureSpec.getSize(widthMeasureSpec)
        if (widthMode != MeasureSpec.EXACTLY) {
            widthSize = mDM.widthPixels / 2
        }
        val heightMode = MeasureSpec.getMode(heightMeasureSpec)
        var heightSize = MeasureSpec.getSize(heightMeasureSpec)
        if (heightMode != MeasureSpec.EXACTLY) {
            heightSize = widthSize / 2
        }
        random = Random(SystemClock.uptimeMillis())
        setMeasuredDimension(widthSize, heightSize)
    }


    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        val width = width
        val height = height
        if (width <= 10 || height <= 10) {
            return
        }
        val saveCount = canvas.save()
        val maxRadius = (min(width.toDouble(), height.toDouble()) / 2).toInt()
        canvas.translate((width / 2).toFloat(), (height / 2).toFloat())
        val clockTime = clockTime
        if (isRefresh) {
            val dt = 1000f
            val r = 5f
            for (i in 0 until maxStartNum) {
                val t = i % 12
                val degree =
                    (random!!.nextFloat() * 30 + t * 30).toDouble() // 12等分圆
                val minRadius = maxRadius * 1f / 2f
                val radians = Math.toRadians(degree)
                val radius = (random!!.nextFloat() * maxRadius / 2f).toInt()
                val x = (cos(radians) * (radius + minRadius)).toFloat()
                val y = (sin(radians) * (radius + minRadius)).toFloat()
                val speedX = (x - 0) / dt
                val speedY = (y - 0) / dt
                val color = argb(random!!.nextFloat(), random!!.nextFloat(), random!!.nextFloat())
                stars[i] = Star(speedX, speedY, clockTime, r, radians, color, false, TYPE_QUAD)
            }
            isRefresh = false
        }
        for (i in 0 until maxStartNum) {
            val star = stars[i]
            star!!.draw(canvas, mDrawerPaint, clockTime)
        }
        if (!isNextDrawingTimeScheduled) {
            isNextDrawingTimeScheduled = true
            postDelayed(this, V_SYNC_TIME)
        }
        canvas.restoreToCount(saveCount)
    }

    override fun run() {
        isNextDrawingTimeScheduled = false
        clockTime += 32
        if (clockTime > displayTime) {
            clockTime = displayTime
        }
        postInvalidate()
    }

    fun startPlay() {
        clockTime = 0
        isRefresh = true
        removeCallbacks(this)
        run()
    }

    private fun initPaint() {
        // 实例化画笔并打开抗锯齿
        mArcPaint = TextPaint(Paint.ANTI_ALIAS_FLAG)
        mArcPaint!!.isAntiAlias = true
        mArcPaint!!.style = Paint.Style.STROKE
        mArcPaint!!.strokeCap = Paint.Cap.ROUND
        mDrawerPaint = TextPaint(Paint.ANTI_ALIAS_FLAG)
        mDrawerPaint!!.isAntiAlias = true
        mDrawerPaint!!.style = Paint.Style.FILL
        mDrawerPaint!!.strokeCap = Paint.Cap.ROUND
    }

    class Star(
        var speedX: Float,
        var speedY: Float,
        var startTime: Long,
        private val r: Float,
        private val radians: Double,
        private val color: Int,
        private val fromCenter: Boolean,
        type: Int
    ) {
        var path: Path = Path()
        var type = TYPE_QUAD

        init {
            this.type = type
        }

        fun draw(canvas: Canvas, paint: Paint?, clockTime: Long) {
            when (type) {
                TYPE_BASE -> drawBase(canvas, paint, clockTime)
                TYPE_RECT -> drawRect(canvas, paint, clockTime)
                TYPE_CIRCLE_CCW -> drawCircleCCW(canvas, paint, clockTime)
                TYPE_QUAD -> drawQuad(canvas, paint, clockTime)
            }
        }

        fun drawQuad(canvas: Canvas, paint: Paint?, clockTime: Long) {
            val costTime = clockTime - startTime
            val dx = speedX * costTime
            val dy = speedY * costTime
            val currentRadius = sqrt((dx * dx + dy * dy).toDouble())
            path.reset()
            if (currentRadius > 0) {
                if (fromCenter) {
                    path.moveTo(0f, 0f)
                } else {
                    path.moveTo(dx / 3, dy / 3)
                }

                //1、利用反三角函数计算出小圆切线与所有小圆原点与（0，0）点的夹角
                val asin = asin(r / currentRadius)

                //2、计算出切线长度
                val aspectRadius = abs(cos(asin) * currentRadius)
                val axLeft = (aspectRadius * cos(radians - asin)).toFloat()
                val ayLeft = (aspectRadius * sin(radians - asin)).toFloat()
                path.lineTo(axLeft, ayLeft)
                val axRight = (aspectRadius * cos(radians + asin)).toFloat()
                val ayRight = (aspectRadius * sin(radians + asin)).toFloat()
                val cx = (cos(radians) * (currentRadius + 2 * r)).toFloat()
                val cy = (sin(radians) * (currentRadius + 2 * r)).toFloat()
                //如果使用三角函数计算切线可能很复杂，这里使用贝塞尔曲线简化逻辑
                path.quadTo(cx, cy, axRight, ayRight)
                path.lineTo(axRight, ayRight)
            }
            path.close()
            paint!!.setColor(Color.argb((255*(clockTime/500f)).toInt(),color.red,color.green,color.blue))
            canvas.drawPath(path, paint)
        }

        fun drawCircleCCW(canvas: Canvas, paint: Paint?, clockTime: Long) {
            val costTime = clockTime - startTime
            val dx = speedX * costTime
            val dy = speedY * costTime
            val currentRadius = sqrt((dx * dx + dy * dy).toDouble())
            path.reset()
            if (currentRadius > 0) {
                if (fromCenter) {
                    path.moveTo(0f, 0f)
                } else {
                    path.moveTo(dx / 3, dy / 3)
                }

                //1、利用反三角函数计算出小圆切线与所有小圆原点与（0，0）点的夹角
                val asin = asin(r / currentRadius)

                //2、计算出切线长度
                val aspectRadius = abs(cos(asin) * currentRadius)
                val axLeft = (aspectRadius * cos(radians - asin)).toFloat()
                val ayLeft = (aspectRadius * sin(radians - asin)).toFloat()
                path.lineTo(axLeft, ayLeft)
                val axRight = (aspectRadius * cos(radians + asin)).toFloat()
                val ayRight = (aspectRadius * sin(radians + asin)).toFloat()
                path.lineTo(axRight, ayRight)
                path.addCircle(dx, dy, r, Path.Direction.CCW)
            }
            path.close()
            paint!!.setColor(color)
            canvas.drawPath(path, paint)
        }

        fun drawBase(canvas: Canvas, paint: Paint?, clockTime: Long) {
            val costTime = clockTime - startTime
            val dx = speedX * costTime
            val dy = speedY * costTime
            val currentRadius = sqrt((dx * dx + dy * dy).toDouble())
            paint!!.setColor(color)
            if (currentRadius > 0) {
                val asin = asin(r / currentRadius)
                //利用反三角函数计算出切线与圆的夹角
                var t = 1
                for (i in 0..1) {
                    val aspectRadius = abs(cos(asin) * currentRadius) //切线长度
                    val ax = (aspectRadius * cos(radians + asin * t)).toFloat()
                    val ay = (aspectRadius * sin(radians + asin * t)).toFloat()
                    if (fromCenter) {
                        canvas.drawLine(0f, 0f, ax, ay, paint)
                    } else {
                        canvas.drawLine(dx / 3, dy / 3, ax, ay, paint)
                    }
                    t = -1
                }
            }
            canvas.drawCircle(dx, dy, r, paint)
        }

        fun drawRect(canvas: Canvas, paint: Paint?, clockTime: Long) {
            val costTime = clockTime - startTime
            val dx = speedX * costTime
            val dy = speedY * costTime
            paint!!.setColor(color)
            val rectF = RectF(dx - r, dy - r, dx + r, dy + r)
            canvas.drawRect(rectF, paint)
            //   canvas.drawCircle(dx,dy,r,paint);
        }
    }

    companion object {
        private const val V_SYNC_TIME: Long = 30
        const val TYPE_BASE = 1
        const val TYPE_QUAD = 2
        const val TYPE_RECT = 3
        const val TYPE_CIRCLE_CCW = 4
        fun argb(red: Float, green: Float, blue: Float): Int {
            return (1 * 255.0f + 0.5f).toInt() shl 24 or
                    ((red * 255.0f + 0.5f).toInt() shl 16) or
                    ((green * 255.0f + 0.5f).toInt() shl 8) or (blue * 255.0f + 0.5f).toInt()
        }
    }
}

