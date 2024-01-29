package com.example.draw_android.section05_canvas_clip.a_canvas_clip

import android.animation.ValueAnimator
import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.Path
import android.graphics.Rect
import android.graphics.RectF
import android.util.AttributeSet
import android.view.MotionEvent
import android.view.View
import android.view.animation.DecelerateInterpolator
import androidx.compose.runtime.produceState
import androidx.core.animation.addListener
import com.example.draw_android.PointIsInPath
import com.example.draw_android.R


class ClipHeadView : View {
    private var scale: Float = 1f
    private var radius = 80f
    private lateinit var bitmap: Bitmap
    val paint = Paint().apply {
        color = Color.parseColor("#FFBB86FC")
        style = Paint.Style.STROKE
        strokeWidth = 11f
    }

    private val paintFill = Paint().apply {
        color = Color.parseColor("#F3E5F5")
        style = Paint.Style.FILL
        strokeWidth = 11f
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
        bitmap = BitmapFactory.decodeResource(resources, R.drawable.clip_head_syr)
    }

    private val clipPath = Path()
    private val bgPath = Path()
    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
        super.onSizeChanged(w, h, oldw, oldh)
        radius = minOf(w, h) /2f - paint.strokeWidth
    }

    override fun draw(canvas: Canvas) {
        super.draw(canvas)
        //1、绘制底层背景圆圈和背景
        val srcRect = Rect(0, 0, bitmap.width, bitmap.height)
        val circleScale = if (scale > 1.25f) 1.25f else scale
        val circleScaleValue = (radius * (2f - circleScale))
        bgPath.reset()
        bgPath.addCircle(
            width / 2f,
            height / 2f,
            circleScaleValue - paint.strokeWidth / 2f,
            Path.Direction.CCW
        )
        canvas.drawCircle(width / 2f, height / 2f, circleScaleValue, paint)
        canvas.drawCircle(
            width / 2f,
            height / 2f,
            circleScaleValue - paint.strokeWidth / 2f,
            paintFill
        )
        //2、裁剪画布
        clipPath.reset()
        clipPath.addCircle(
            width / 2f,
            height / 2f,
            circleScaleValue - paint.strokeWidth / 2f,
            Path.Direction.CCW
        )

        clipPath.addRect(
            RectF(
                (width / 2 - radius), height / 2 - radius * 1.25f,
                (width / 2 + radius), height / 2f),
            Path.Direction.CCW
        )
        canvas.clipPath(clipPath)

        val scaleValue = (radius * scale).toInt()
        //3、绘制图片
        val dstRect = Rect(
            width / 2 - scaleValue,
            height / 2 - scaleValue,
            width / 2 + scaleValue,
            height / 2 + scaleValue
        )
        canvas.drawBitmap(bitmap, srcRect, dstRect, paint)
    }

    override fun performClick(): Boolean {
        return super.performClick()
    }

    private var animatorProgress = false
    private var scaleLager = false
    override fun onTouchEvent(event: MotionEvent): Boolean {
        performClick()
        val x = event.x
        val y = event.y
        if (animatorProgress) {
            return false
        }
        when (event.action) {
            MotionEvent.ACTION_DOWN -> {             // 按下时检查是否在目标区域内
                if (PointIsInPath(x, y, bgPath)) {
                    // 手指按下在目标区域内
                    // 在这里可以进行相应的处理
                    if (!scaleLager) {
                        startElasticAnimationToRight(1f)
                    }
                }
            }

            MotionEvent.ACTION_MOVE ->                 // 移动时检查是否进入或离开目标区域
                if (PointIsInPath(x, y, bgPath)) {
                    // 手指移动到目标区域内
                    // 在这里可以进行相应的处理
                    if (!scaleLager) {
                        startElasticAnimationToRight(1f)
                    }
                } else {
                    // 手指离开目标区域
                    // 在这里可以进行相应的处理
                    if (scaleLager) {
                        endElasticAnimationToRight(1f)
                    }
                }

            MotionEvent.ACTION_UP -> {
                if (scaleLager) {
                    endElasticAnimationToRight(1f)
                }
            }
        }
        invalidate()
        return true
    }

    private var animator: ValueAnimator? = null
    private var endAnimator: ValueAnimator? = null
    private fun startElasticAnimationToRight(starAnimal: Float) {
        animatorProgress = true
        animator?.cancel()
        // 设置弹性动画
        animator = ValueAnimator.ofFloat(
            starAnimal,
            1.25f
        )
        animator?.apply {
            duration = 600
            interpolator = DecelerateInterpolator()
            addUpdateListener { animation ->
                scale = animation.animatedValue as Float
                invalidate()
            }
            addListener(onEnd = {
                // 手指抬起时，移除最后一个图层和对应的图片
                animatorProgress = false
                scaleLager = true
            })
            start()
        }
    }

    private fun endElasticAnimationToRight(endAnimal: Float) {
        animatorProgress = true
        endAnimator?.cancel()
        // 设置弹性动画
        endAnimator = ValueAnimator.ofFloat(
            1.25f,
            endAnimal
        )
        endAnimator?.apply {
            duration = 600
            interpolator = DecelerateInterpolator()
            addUpdateListener { animation ->
                scale = animation.animatedValue as Float
                invalidate()
            }
            addListener(onEnd = {
                // 手指抬起时，移除最后一个图层和对应的图片
                animatorProgress = false
                scaleLager = false
            })
            start()
        }
    }
}