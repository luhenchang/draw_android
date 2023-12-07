package com.example.draw_android.section05_canvas.f_event

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.PorterDuff
import android.graphics.PorterDuffXfermode
import android.graphics.Rect
import android.util.AttributeSet
import android.view.MotionEvent
import android.view.View

class PorterDuffDstOutView : View, EventDisallowInterceptListener {
    //记录手势实时水平放向上的位置
    private var lastX: Float = 0f
    private val bluePaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
        style = Paint.Style.FILL
        color = Color.BLUE
    }
    private val blueRect = Rect(0, 0, 200, height)

    //紫色
    private val purpleColor = Color.parseColor("#FFBB86FC")

    //粉红色
    private val pinkColor = Color.parseColor("#EC877F")

    private val xfModePaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
        xfermode = PorterDuffXfermode(PorterDuff.Mode.DST_OUT)
        color = Color.YELLOW
    }

    constructor(context: Context) : super(context) {

    }

    constructor(context: Context, attrs: AttributeSet?) : super(context, attrs) {

    }

    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int) : super(
        context,
        attrs,
        defStyleAttr
    ) {

    }


    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
        super.onSizeChanged(w, h, oldw, oldh)
        blueRect.bottom = h
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        //1、绘制紫色背景层
        canvas.drawColor(purpleColor)
        //2、通过saveLayer新建新涂层，在其涂层绘制粉色作为目标
        val layerId01 = canvas.saveLayer(0f, 0f, width.toFloat(), bottom.toFloat(), null)
        //3、绘制粉色目标像素
        canvas.drawColor(pinkColor)
        //4、绘制左侧蓝色巨型源像素
        //canvas.drawRect(blueRect, bluePaint)
        //5、画笔换为PorterDuff.Mode.DST_OUT混合模式
        //6、给巨型右距离屏幕左边的宽度负值为手势距离屏幕左边的距离lastX
        blueRect.right = lastX.toInt()
        canvas.drawRect(blueRect, xfModePaint)
        //xfModePaint.xfermode = null
        canvas.restoreToCount(layerId01)

    }

    override fun performClick(): Boolean {
        return super.performClick()
    }

    private var dragModeType: Boolean = false
    override fun onTouchEvent(event: MotionEvent): Boolean {
        when (event.action and MotionEvent.ACTION_MASK) {
            MotionEvent.ACTION_DOWN -> {
                performClick()
                lastX = event.x
                //避免手指放下任意位置突然一大块透下去，避免过猛，我们只允许举例屏幕左边60像素以内按下才算开始要拖动
                if (lastX < 60f) {
                    dragModeType = true
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

            }
        }
        return true
    }

    private var disallowIntercept: Boolean = false
    override fun requestDisallowInterceptTouchEvent(disallowIntercept: Boolean) {
        this.disallowIntercept = disallowIntercept
    }
}
