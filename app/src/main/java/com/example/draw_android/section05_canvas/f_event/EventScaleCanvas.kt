package com.example.draw_android.section05_canvas.f_event

import android.content.Context
import android.graphics.Canvas
import android.graphics.Paint
import android.graphics.Path
import android.graphics.Rect
import android.graphics.RectF
import android.graphics.Region
import android.util.AttributeSet
import android.util.Log
import android.view.MotionEvent
import android.view.ScaleGestureDetector
import android.view.View
import android.view.animation.RotateAnimation
import kotlin.math.abs
import kotlin.math.sqrt


class EventScaleCanvas :
    View,EventDisallowInterceptListener{
    private var curScale: Float = 1f
    private var eventModeType: Int = 1
    private lateinit var mScaleGestureDetector: ScaleGestureDetector

    private val paint = Paint(Paint.ANTI_ALIAS_FLAG)
    private var oriDis: Float = 0f
    private var preScale = 1f //之前的伸缩值
    private val SCALE_FACTOR = .5f
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
        initScaleGestureDetector()
    }

    private fun initScaleGestureDetector() {
        mScaleGestureDetector = ScaleGestureDetector(
            context,
            object : ScaleGestureDetector.SimpleOnScaleGestureListener() {
                override fun onScaleBegin(detector: ScaleGestureDetector): Boolean {
                    return true
                }

                override fun onScale(detector: ScaleGestureDetector): Boolean {
                    //为了保持连续性->当前的伸缩值*之前的伸缩值
                    curScale *= detector.scaleFactor
                    //当放大倍数其最大能放大沾满屏幕宽。计算不难的。宽度/(2*100)即宽度除以直径。最大放大到沾满屏幕
                    curScale = curScale.coerceIn(0.1f, width / 2 / 100f)
                    invalidate()
                    return true
                }

                override fun onScaleEnd(detector: ScaleGestureDetector) {}
            })
    }

    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
        super.onSizeChanged(w, h, oldw, oldh)
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        canvas.drawCircle(width / 2f, height / 2f, 100f * curScale, paint)
    }

    override fun performClick(): Boolean {
        return super.performClick()
    }

    override fun onTouchEvent(event: MotionEvent): Boolean {
       // mScaleGestureDetector.onTouchEvent(event)
        when (event.action and MotionEvent.ACTION_MASK) {
            MotionEvent.ACTION_DOWN -> {
                performClick()
                parent?.requestDisallowInterceptTouchEvent(disallowIntercept)
                //1.表示单点事件
                eventModeType = 1
            }

            MotionEvent.ACTION_POINTER_DOWN -> {
                //多点触控
                oriDis = distance(event)
                if (oriDis > 10) {
                    //2.表示多点触碰类型
                    eventModeType = 2
                }

            }

            MotionEvent.ACTION_MOVE -> {
                if (eventModeType == 2) {
                    // 获取两个手指缩放时候的之间距离
                    val newDist = distance(event)
//                    curScale *= newDist / oriDis
//                    curScale = 0.1f.coerceAtLeast(curScale.coerceAtMost(5.0f))
                    if (newDist > 10) {
                        //通过当前的距离除以上一手指按下两趾头之间的距离就为实时的缩放
                        curScale = (newDist / oriDis)
                        if (curScale >= 1) {
                            curScale = preScale + (curScale-1)*SCALE_FACTOR//有助于理解，简写如下：
                            //curScale += preScale-1
                        } else {
                            curScale = preScale - (1-curScale)*SCALE_FACTOR
                        }
                        preScale = curScale
                    }
                }
                //通知刷新View
                invalidate()
            }
            MotionEvent.ACTION_CANCEL,
            MotionEvent.ACTION_UP,
            MotionEvent.ACTION_POINTER_UP -> {
                eventModeType = 0
            }
        }
        return true
    }

    /**
     * 计算两个手指间的距离
     *
     * @param event 触摸事件
     * @return 放回两个手指之间的距离
     */
    private fun distance(event: MotionEvent): Float {
        val x = event.getX(0) - event.getX(1)
        val y = event.getY(0) - event.getY(1)
        return sqrt((x * x + y * y).toDouble()).toFloat() //两点间距离公式
    }

    private fun pointIsInPath(x: Float, y: Float, path: Path): Boolean {
        val bounds = RectF()
        path.computeBounds(bounds, true)
        val region = Region()
        region.setPath(
            path,
            Region(
                Rect(
                    bounds.left.toInt(),
                    bounds.top.toInt(),
                    bounds.right.toInt(),
                    bounds.bottom.toInt()
                )
            )
        )
        return region.contains(x.toInt(), y.toInt())
    }

    private var disallowIntercept: Boolean = false
    override fun requestDisallowInterceptTouchEvent(disallowIntercept: Boolean) {
        this.disallowIntercept = disallowIntercept
    }
}