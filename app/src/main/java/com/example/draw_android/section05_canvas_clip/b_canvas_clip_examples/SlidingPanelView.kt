package com.example.draw_android.section05_canvas_clip.b_canvas_clip_examples

import android.content.Context
import android.graphics.Color
import android.graphics.Point
import android.util.AttributeSet
import android.util.Log
import android.view.MotionEvent
import android.view.animation.DecelerateInterpolator
import android.widget.FrameLayout
import android.widget.RelativeLayout
import android.widget.Scroller
import kotlin.math.abs
import kotlin.math.max
import kotlin.math.min


class SlidingPanelView : RelativeLayout {
    private var curScrollX: Int = 0
    private var context: Context? = null
    private var leftMenu: FrameLayout? = null
    private var middleMenu: FrameLayout? = null
    private var rightMenu: FrameLayout? = null
    private var middleMask: FrameLayout? = null
    private var mScroller: Scroller? = null
    val LEFT_ID = 0xaabbcc
    val MIDEELE_ID = 0xaaccbb
    val RIGHT_ID = 0xccbbaa
    private var isSlideCompete = false
    private var isHorizontalScroll = false
    private val point = Point()

    constructor(context: Context) : super(context) {
        initView(context)
    }

    constructor(context: Context, attrs: AttributeSet?) : super(context, attrs) {
        initView(context)
    }

    private fun initView(context: Context) {
        this.context = context
        mScroller = Scroller(context, DecelerateInterpolator())
        leftMenu = FrameLayout(context)
        middleMenu = FrameLayout(context)
        rightMenu = FrameLayout(context)
        middleMask = FrameLayout(context)
        leftMenu!!.setBackgroundColor(Color.RED)
        middleMenu!!.setBackgroundColor(Color.GREEN)
        rightMenu!!.setBackgroundColor(Color.RED)
        middleMask!!.setBackgroundColor(-0x78000000)
        addView(leftMenu)
        addView(middleMenu)
        addView(rightMenu)
        addView(middleMask)
        middleMask!!.setAlpha(0f)
    }

    fun onMiddleMask(): Float {
        return middleMask!!.alpha
    }

    override fun scrollTo(x: Int, y: Int) {
        super.scrollTo(x, y)
        onMiddleMask()
        // Log.e("getScrollX","getScrollX="+getScrollX());//可以是负值
        val curX = abs(scrollX.toDouble()).toInt()
        val scale = curX / leftMenu!!.measuredWidth.toFloat()
        middleMask!!.setAlpha(scale)
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec)
        middleMenu!!.measure(widthMeasureSpec, heightMeasureSpec)
        middleMask!!.measure(widthMeasureSpec, heightMeasureSpec)
        val realWidth = MeasureSpec.getSize(widthMeasureSpec)
        val tempWidthMeasure =
            MeasureSpec.makeMeasureSpec((realWidth * 0.8f).toInt(), MeasureSpec.EXACTLY)
        leftMenu!!.measure(tempWidthMeasure, heightMeasureSpec)
        rightMenu!!.measure(tempWidthMeasure, heightMeasureSpec)
    }

    override fun onLayout(changed: Boolean, l: Int, t: Int, r: Int, b: Int) {
        super.onLayout(changed, l, t, r, b)
        middleMenu!!.layout(l, t, r, b)
        middleMask!!.layout(l, t, r, b)
        leftMenu!!.layout(l - leftMenu!!.measuredWidth, t, r, b)
        rightMenu!!.layout(
            l + middleMenu!!.measuredWidth,
            t,
            l + middleMenu!!.measuredWidth
                    + rightMenu!!.measuredWidth, b
        )
    }

    override fun dispatchTouchEvent(ev: MotionEvent): Boolean {
        if (!isSlideCompete) {
            handleSlideEvent(ev)
            return true
        }
        if (isHorizontalScroll) {
            when (ev.actionMasked) {
                MotionEvent.ACTION_MOVE -> {
                    val curScrollX = scrollX
                    val dis_x = (ev.x - point.x).toInt()
                    //滑动方向和滚动滚动条方向相反,因此dis_x必须取负值
                    val expectX = -dis_x + curScrollX
                    if (dis_x > 0) {
                        Log.d("I", "Right-Slide,Left-Scroll") //向右滑动，向左滚动
                    } else {
                        Log.d("I", "Left-Slide,Right-Scroll")
                    }
                    Log.e("I", "ScrollX=" + curScrollX + " , X=" + ev.x + " , dis_x=" + dis_x)
                    //规定expectX的变化范围
                    val finalX =
                        max(
                            -leftMenu!!.measuredWidth.toDouble(),
                            min(expectX.toDouble(), rightMenu!!.measuredWidth.toDouble())
                        )
                            .toInt()
                    scrollTo(finalX, 0)
                    point.x = ev.x.toInt() //更新，保证滑动平滑
                }

                MotionEvent.ACTION_UP, MotionEvent.ACTION_CANCEL -> {
                    curScrollX = scrollX
                    if (abs(curScrollX.toDouble()) > (leftMenu!!.measuredWidth shr 1)) {
                        if (curScrollX < 0) {
                            mScroller!!.startScroll(
                                curScrollX, 0,
                                -leftMenu!!.measuredWidth - curScrollX, 0,
                                200
                            )
                        } else {
                            mScroller!!.startScroll(
                                curScrollX, 0,
                                leftMenu!!.measuredWidth - curScrollX, 0,
                                200
                            )
                        }
                    } else {
                        mScroller!!.startScroll(curScrollX, 0, -curScrollX, 0, 200)
                    }
                    invalidate()
                    isHorizontalScroll = false
                    isSlideCompete = false
                }
            }
        } else {
            when (ev.actionMasked) {
                MotionEvent.ACTION_UP -> {
                    isHorizontalScroll = false
                    isSlideCompete = false
                }

                else -> {}
            }
        }
        return super.dispatchTouchEvent(ev)
    }

    /**
     * 通过invalidate操纵,此方法通过draw方法调用
     */
    override fun computeScroll() {
        super.computeScroll()
        if (!mScroller!!.computeScrollOffset()) {
            //计算currX，currY,并检测是否已完成“滚动”
            return
        }
        val tempX = mScroller!!.currX
        scrollTo(tempX, 0) //会重复调用invalidate
    }

    private fun handleSlideEvent(ev: MotionEvent) {
        when (ev.action and MotionEvent.ACTION_MASK) {
            MotionEvent.ACTION_DOWN -> {
                point.x = ev.x.toInt()
                point.y = ev.y.toInt()
                super.dispatchTouchEvent(ev)
            }

            MotionEvent.ACTION_MOVE -> {
                val dX = abs((ev.x.toInt() - point.x).toDouble()).toInt()
                val dY = abs((ev.y.toInt() - point.y).toDouble()).toInt()
                if (dX >= SLIDE_SLOP && dX > dY) { // 左右滑动
                    isHorizontalScroll = true
                    isSlideCompete = true
                    point.x = ev.x.toInt()
                    point.y = ev.y.toInt()
                } else if (dY >= SLIDE_SLOP && dY > dX) { // 上下滑动
                    isHorizontalScroll = false
                    isSlideCompete = true
                    point.x = ev.x.toInt()
                    point.y = ev.y.toInt()
                }
            }

            MotionEvent.ACTION_UP, MotionEvent.ACTION_OUTSIDE, MotionEvent.ACTION_CANCEL -> {
                super.dispatchTouchEvent(ev)
                isHorizontalScroll = false
                isSlideCompete = false
            }
        }
    }

    companion object {
        private val SLIDE_SLOP = 20
    }
}

