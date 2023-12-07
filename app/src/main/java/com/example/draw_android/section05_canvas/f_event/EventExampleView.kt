package com.example.draw_android.section05_canvas.f_event

import android.animation.ValueAnimator
import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.Path
import android.graphics.PorterDuff
import android.graphics.PorterDuffXfermode
import android.util.AttributeSet
import android.view.MotionEvent
import android.view.View
import android.view.animation.DecelerateInterpolator
import androidx.core.animation.addListener
import com.example.draw_android.R
import kotlin.math.min

class EventExampleView : View, EventDisallowInterceptListener {


    private var lastX = 0f
    private var eventY = 0f
    private val bitmapPaint = Paint(Paint.ANTI_ALIAS_FLAG)
    private val xfModePaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
        xfermode = PorterDuffXfermode(PorterDuff.Mode.DST_OUT)
    }

    private val shadowLinePaint = Paint().apply {
        strokeWidth =2f
        color = Color.parseColor("#907F7C7C")
        style = Paint.Style.STROKE
        setShadowLayer(10f,5f,0f,Color.BLACK)
    }
    private val shadowPaint = Paint().apply {
        strokeWidth =40f
        color = Color.TRANSPARENT
        style = Paint.Style.STROKE
        setShadowLayer(80f,-80f,0f,Color.BLACK)
    }

    private val iconPaint = Paint().apply {
        color = Color.WHITE
        style = Paint.Style.STROKE
        strokeWidth = 5f
    }
    private val iconPath = Path()

    private val imageList = mutableListOf<Int>()
    private val bitmapCacheList = mutableListOf<Bitmap>()
    private val bitmapList = mutableListOf<Bitmap>()

    private val layerIdList = mutableListOf<Int>()
    var path = Path()
    var shadowPath = Path()
    var shadowRightPath = Path()


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
        imageList.add(R.drawable.shu_img_tow)
        imageList.add(R.drawable.shu_img)
    }

    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
        super.onSizeChanged(w, h, oldw, oldh)
        Thread {
            imageList.forEach {
                // 创建与屏幕宽高相同大小的Bitmap
                val originalBitmap = BitmapFactory.decodeResource(resources, it)
                val scaledBitmap = Bitmap.createScaledBitmap(originalBitmap, width, height, true)
                bitmapList.add(scaledBitmap)
                bitmapCacheList.add(scaledBitmap)
            }
            postInvalidate()
        }.start()

    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        val scale = lastX / width
        // 绘制每张图片，使其重叠在一起
        bitmapList.forEach { bitmap ->
            val layerId1 = canvas.saveLayer(0f, 0f, width.toFloat(), height.toFloat(), bitmapPaint)
            canvas.drawBitmap(bitmap, 0f, 0f, bitmapPaint)
            layerIdList.add(layerId1)
        }
        if (layerIdList.isNotEmpty()) {
            path.reset()
            path.apply {
                moveTo(0f, 0f)
                val offsetEventX = 150f
                val offsetEventY = 100f

                val changeStarY = (eventY - offsetEventY) - height / 2 * scale
                val changeEndY = (eventY + offsetEventY) + height / 2 * scale
                lineTo(lastX - offsetEventX, 0f)
                lineTo(lastX - offsetEventX, changeStarY)


                cubicTo(
                    lastX - offsetEventX,
                    eventY - offsetEventY,
                    lastX,
                    eventY - offsetEventY,
                    lastX,
                    eventY
                )
                cubicTo(
                    lastX,
                    eventY + offsetEventY,
                    lastX - offsetEventX,
                    eventY + offsetEventY,
                    lastX - offsetEventX,
                    changeEndY
                )

                lineTo(lastX - offsetEventX, height.toFloat())
                lineTo(0f, height.toFloat())

                close()
            }
            canvas.drawPath(path, xfModePaint)
            canvas.restoreToCount(layerIdList.last())
            shadowPath.reset()
            shadowPath.apply {
                val offsetEventX = 150f
                val offsetEventY = 100f

                val changeStarY = (eventY - offsetEventY) - height / 2 * scale
                val changeEndY = (eventY + offsetEventY) + height / 2 * scale
                lineTo(lastX - offsetEventX, 0f)
                lineTo(lastX - offsetEventX, changeStarY)


                cubicTo(
                    lastX - offsetEventX,
                    eventY - offsetEventY,
                    lastX,
                    eventY - offsetEventY,
                    lastX,
                    eventY
                )
                cubicTo(
                    lastX,
                    eventY + offsetEventY,
                    lastX - offsetEventX,
                    eventY + offsetEventY,
                    lastX - offsetEventX,
                    changeEndY
                )

                lineTo(lastX - offsetEventX, height.toFloat())
                lineTo(0f, height.toFloat())
            }

            canvas.drawPath(shadowPath, shadowPaint)

            shadowRightPath.reset()
            shadowRightPath.apply {
                val offsetEventX = 150f
                val offsetEventY = 100f

                val changeStarY = (eventY - offsetEventY) - height / 2 * scale
                val changeEndY = (eventY + offsetEventY) + height / 2 * scale
                lineTo(lastX - offsetEventX, 0f)
                lineTo(lastX - offsetEventX, changeStarY)


                cubicTo(
                    lastX - offsetEventX,
                    eventY - offsetEventY,
                    lastX,
                    eventY - offsetEventY,
                    lastX,
                    eventY
                )
                cubicTo(
                    lastX,
                    eventY + offsetEventY,
                    lastX - offsetEventX,
                    eventY + offsetEventY,
                    lastX - offsetEventX,
                    changeEndY
                )

                lineTo(lastX - offsetEventX, height.toFloat())
                lineTo(0f, height.toFloat())
                close()
            }
            canvas.drawPath(shadowRightPath, shadowLinePaint)


            //绘制按钮
            canvas.drawCircle(lastX - 36, eventY, 30f, iconPaint)
            iconPath.reset()
            iconPath.apply {
                moveTo(lastX - 36, eventY - 16)
                lineTo(lastX - 36 + 20, eventY)
                lineTo(lastX - 36, eventY + 16)
            }.apply {
                moveTo(lastX - 36, eventY - 16)
                lineTo(lastX - 36 + 20, eventY)
                lineTo(lastX - 36, eventY + 16)
            }
            canvas.drawPath(iconPath, iconPaint)
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
                parent?.requestDisallowInterceptTouchEvent(disallowIntercept)
                lastX = event.x
                eventY = event.y
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
                    eventY = event.y
                    // 通知刷新View
                    invalidate()
                }
            }
            //⚠️这里需要特别注意，如果有父节点处理事件，如果不做处理，事件滑动中途被取消，
            //就不会执行 MotionEvent.ACTION_UP导致滑动结束之后，不能执行动画恢复页面。
            MotionEvent.ACTION_CANCEL,
            MotionEvent.ACTION_UP -> {
                eventY = event.y
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
        animator = ValueAnimator.ofFloat(
            starAnimal,
            width.toFloat(),
            width.toFloat() - 120,
            width.toFloat(),
            width.toFloat() - 70,
            width.toFloat()
        )
        animator?.apply {
            duration = 600
            interpolator = DecelerateInterpolator()
            addUpdateListener { animation ->
                lastX = animation.animatedValue as Float
                invalidate()
            }
            addListener(onEnd = {
                // 手指抬起时，移除最后一个图层和对应的图片
                lastX = 0f
                layerIdList.removeLastOrNull()
                bitmapList.removeLastOrNull()
                if (bitmapList.isEmpty()) {
                    bitmapList.addAll(bitmapCacheList)
                    invalidate()
                }
            })
            start()
        }
    }

    private fun startElasticAnimationToLeft(starAnimal: Float) {
        animator?.cancel()
        // 设置弹性动画
        animator = ValueAnimator.ofFloat(
            starAnimal,
            0f,
            min(starAnimal, 120f),
            0f,
            min(starAnimal, 60f),
            0f
        )
        animator?.apply {
            duration = 600
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

    private var disallowIntercept: Boolean = false
    override fun requestDisallowInterceptTouchEvent(disallowIntercept: Boolean) {
        this.disallowIntercept = disallowIntercept
    }
}
