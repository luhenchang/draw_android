package com.example.draw_android.section05_canvas.f_event

import android.content.ContentValues.TAG
import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Canvas
import android.graphics.Paint
import android.graphics.PorterDuff
import android.graphics.PorterDuffXfermode
import android.graphics.RectF
import android.util.AttributeSet
import android.util.Log
import android.view.MotionEvent
import android.view.ScaleGestureDetector
import android.view.View
import androidx.viewpager.widget.ViewPager
import com.example.draw_android.R
import kotlin.math.sqrt

class EventMeasurePathView constructor(context: Context, attributeSet: AttributeSet) :
    View(context, attributeSet) {
    private var curScale: Float = 1f
    private var eventModeType: Int = 1
    private lateinit var mScaleGestureDetector: ScaleGestureDetector
    private var isDragging = false
    private var lastX = 0f
    private var lastY = 0f
    private lateinit var titleBox: RectF // 初始巨型框的位置和大小

    private val paint = Paint(Paint.ANTI_ALIAS_FLAG)
    private val imageList = mutableListOf<Bitmap>()
    private var oriDis: Float = 0f

    init {
        // 初始化图片资源
        //imageList.add(BitmapFactory.decodeResource(resources, R.drawable.item_bg))
        //imageList.add(BitmapFactory.decodeResource(resources, R.drawable.item_bg_2))
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
                    Log.e(TAG, "onScale:" + detector.scaleFactor)
                    return false
                }

                override fun onScaleEnd(detector: ScaleGestureDetector) {}
            })
    }

    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
        super.onSizeChanged(w, h, oldw, oldh)
        titleBox = RectF(0f, 0f, 100f, h.toFloat())
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        // 绘制每张图片，使其重叠在一起
        canvas.drawBitmap(imageList[0], 0f, 0f, paint)
        val layerId = canvas.saveLayer(0f, 0f, width.toFloat(), height.toFloat(), paint)
        canvas.drawBitmap(imageList[1], 0f, 0f, paint)


        paint.xfermode = PorterDuffXfermode(PorterDuff.Mode.DST_OUT)
        canvas.drawRect(0f, 0f, lastX, height.toFloat(), paint)
        paint.xfermode = null
        canvas.restoreToCount(layerId)
    }

    override fun performClick(): Boolean {
        return super.performClick()
    }

    override fun onTouchEvent(event: MotionEvent): Boolean {
        when (event.action and MotionEvent.ACTION_MASK) {
            MotionEvent.ACTION_DOWN -> {
                //1.表示单点事件
                eventModeType = 1
            }

            MotionEvent.ACTION_POINTER_DOWN -> {
                //多点触控
                oriDis = distance(event)
                if (oriDis > 10f) {
                    //2.表示多点触碰类型
                    eventModeType = 2
                }

            }

            MotionEvent.ACTION_MOVE -> {
                if (eventModeType == 2) {
                    // 获取两个手指缩放时候的之间距离
                    val newDist = distance(event)
                    if (newDist > 10f) {
                        curScale = newDist / oriDis
                    }
                }
                //通知刷新View
                invalidate()
            }

            MotionEvent.ACTION_UP -> {
                eventModeType = 0
            }

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

}
//    override fun onTouchEvent(event: MotionEvent): Boolean {
//        val x = event.x
//        val y = event.y
//        when (event.action) {
//            MotionEvent.ACTION_DOWN -> {
//                performClick()
//                if (titleBox.contains(x, y)) {
//                    // 按下位置在巨型框内
//                    isDragging = true
//                    lastX = x
//                    lastY = y
//                }
//            }
//            MotionEvent.ACTION_MOVE -> {
//                if (isDragging) {
//                    // 移动巨型框
//                    val dx = x - lastX
//                    val dy = y - lastY
//                    titleBox.offset(dx, dy)
//                    lastX = x
//                    lastY = y
//                    invalidate()
//                }
//            }
//            MotionEvent.ACTION_UP -> {
//                isDragging = false
//            }
//        }
//
//        return true
//    }
//}