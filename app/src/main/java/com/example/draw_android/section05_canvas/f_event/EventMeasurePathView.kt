package com.example.draw_android.section05_canvas.f_event

import android.animation.ValueAnimator
import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Canvas
import android.graphics.Paint
import android.graphics.PorterDuff
import android.graphics.PorterDuffXfermode
import android.util.AttributeSet
import android.view.MotionEvent
import android.view.View
import android.view.animation.DecelerateInterpolator
import androidx.core.animation.addListener
import com.example.draw_android.R

class EventMeasurePathView : View {


    private var lastX = 0f
    private val bitmapPaint = Paint(Paint.ANTI_ALIAS_FLAG)
    private val xfModePaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
        xfermode = PorterDuffXfermode(PorterDuff.Mode.DST_OUT)
    }
    private val imageList = mutableListOf<Int>()
    private val bitmapList = mutableListOf<Bitmap>()

    private val layerIdList = mutableListOf<Int>()

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
        // 初始化图片资源
        imageList.add(R.drawable.phone_one)
        imageList.add(R.drawable.phone_two)
        imageList.add(R.drawable.phone_three)
        imageList.add(R.drawable.phone_four)
    }

    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
        super.onSizeChanged(w, h, oldw, oldh)
        imageList.forEach {
            // 创建与屏幕宽高相同大小的Bitmap
            val originalBitmap = BitmapFactory.decodeResource(resources, it)
            val scaledBitmap = Bitmap.createScaledBitmap(originalBitmap, width, height, true)
            bitmapList.add(scaledBitmap)
        }


    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        // 绘制每张图片，使其重叠在一起
        bitmapList.forEach { bitmap ->
            val layerId1 = canvas.saveLayer(0f, 0f, width.toFloat(), height.toFloat(), bitmapPaint)
            canvas.drawBitmap(bitmap, 0f, 0f, bitmapPaint)
            layerIdList.add(layerId1)
        }

        if (layerIdList.isNotEmpty()) {
            canvas.drawRect(0f, 0f, lastX, height.toFloat(), xfModePaint)
            canvas.restoreToCount(layerIdList.last())
        }
    }

    override fun performClick(): Boolean {
        return super.performClick()
    }

    private var dragModeType = false
    override fun onTouchEvent(event: MotionEvent): Boolean {
        when (event.action and MotionEvent.ACTION_MASK) {
            MotionEvent.ACTION_DOWN -> {
                performClick()
                lastX = event.x
                if (lastX < 60f) {
                    dragModeType = true
                    // 手指按下时，保存当前图层ID
                    layerIdList.clear()
                    invalidate()
                }
            }

            MotionEvent.ACTION_MOVE -> {
                if (dragModeType) {
                    lastX = event.x
                    // 通知刷新View
                    invalidate()
                }
            }

            MotionEvent.ACTION_CANCEL,
            MotionEvent.ACTION_UP -> {
                if (!dragModeType) {
                    return true
                }
                dragModeType = false
                if (lastX >= width / 2f) {
                    //启动一个动画进行刷新页面
                    if (layerIdList.isNotEmpty())
                        startElasticAnimationToRight(lastX)
                } else {
                    startElasticAnimationToLeft(lastX)
                }
            }
        }
        return true
    }

    private var animator: ValueAnimator? = null
    private fun startElasticAnimationToRight(starAnimal: Float) {
        animator?.cancel()
        // 设置弹性动画
        animator = ValueAnimator.ofFloat(starAnimal, width.toFloat())
        animator?.apply {
            duration = 200
            interpolator = DecelerateInterpolator()
            addUpdateListener { animation ->
                lastX = animation.animatedValue as Float
                invalidate()
            }
            addListener(onEnd = {
                // 手指抬起时，移除最后一个图层和对应的图片
                lastX = 0f
                layerIdList.removeLast()
                bitmapList.removeLast()
            })
            start()
        }
    }

    private fun startElasticAnimationToLeft(starAnimal: Float) {
        animator?.cancel()
        // 设置弹性动画
        animator = ValueAnimator.ofFloat(starAnimal, 0f)
        animator?.apply {
            duration = 200
            interpolator = DecelerateInterpolator()
            addUpdateListener { animation ->
                lastX = animation.animatedValue as Float
                invalidate()
            }
            addListener(onEnd = {
                // 手指抬起时，移除最后一个图层和对应的图片
                lastX = 0f
            })
            start()
        }
    }
}
