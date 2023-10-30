package com.example.draw_android.section05_canvas.e_curve

import android.content.Context
import android.graphics.*
import android.util.AttributeSet
import android.util.Log
import android.view.MotionEvent
import android.view.MotionEvent.ACTION_DOWN
import android.view.MotionEvent.ACTION_MOVE
import android.view.View
import kotlin.math.pow
import kotlin.math.sqrt

/**
 * Created by wang fei on 2022/4/21.
 */
class CurveView constructor(context: Context, attributeSet: AttributeSet) :
    View(context, attributeSet) {

    private lateinit var controlRect: Rect

    //网格的宽度
    private var mWidth = 0
    private var mHeight = 0
    private var sizeChange = false
    private lateinit var rect: RectF
    val paint = Paint().apply {
        color = Color.GRAY
        style = Paint.Style.FILL
        isAntiAlias = true
        textSize = 24f
    }

    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
        super.onSizeChanged(w, h, oldw, oldh)
        mWidth = w
        mHeight = h
        rect = RectF(0f, 0f, width.toFloat(), width.toFloat())
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        Log.e("onDraw=", "001")
        BaseCanvasView.drawBaseBg(canvas, mWidth, mHeight)
        sizeChange = false
        drawLine(canvas)
        drawCircle(canvas)
        drawQuz(canvas)
    }

    //圆方程式:(x-a)^2+(y-b)^2=r^2
    private fun drawCircle(canvas: Canvas) {
        val gPaint = Paint().apply {
            color = Color.BLACK
            style = Paint.Style.STROKE
            strokeWidth = 2f
            isAntiAlias = true
        }
        //绘制圆圈
        number.forEach { t ->
            val point = PointF()
            val pointDown = PointF()
            //(x-80)2+（y-80）2=1602
            point.x = t.toFloat()
            pointDown.x = t.toFloat()
            //y计算应该不用我说吧。
            point.y =
                sqrt(160.0.pow(2.0).toFloat() - ((point.x - 80).toDouble()).pow(2.0)).toFloat() + 80
            pointDown.y = -sqrt(
                160.0.pow(2.0).toFloat() - ((pointDown.x - 80).toDouble()).pow(2.0)
            ).toFloat() + 80
            canvas.drawPoint(point.x, point.y, gPaint)
            canvas.drawPoint(pointDown.x, pointDown.y, gPaint)
        }
    }

    private lateinit var pointList: ArrayList<PointF>
    private var number = -420..420

    //直线方程y=2x-80
    private fun drawLine(canvas: Canvas) {
        pointList = ArrayList()
        //绘制方程式y=10x+20
        val gPaint = Paint().apply {
            color = Color.BLACK
            style = Paint.Style.STROKE
            strokeWidth = 2f
            isAntiAlias = true
        }
        number.forEach { t ->
            val point = PointF()
            point.x = t.toFloat()
            point.y = 2f * t - 80
            pointList.add(point)
            canvas.drawPoint(point.x, point.y, gPaint)
        }
    }

    //记录移动的canvas画布坐标,不是手势坐标，由手势坐标转换为canvas坐标进行刷新
    private var moveX: Float = 160f
    private var moveY: Float = 160f
    private fun drawQuz(canvas: Canvas) {
        controlRect = Rect(
            (moveX - 30f).toInt(),
            (moveY + 30f).toInt(),
            (moveX + 30).toInt(),
            (moveY - 30f).toInt()
        )
        val quePath = Path()
        canvas.drawCircle(0f, 0f, 10f, getPaintCir(Paint.Style.FILL))
        canvas.drawCircle(320f, 0f, 10f, getPaintCir(Paint.Style.FILL))
        //第一个点和控制点的连线到最后一个点链线。为了方便观察
        val lineLeft = Path().apply {
            moveTo(0f, 0f)
            lineTo(moveX, moveY)
            lineTo(320f, 0f)
        }
        canvas.drawPath(lineLeft, getPaint(Paint.Style.STROKE))
        //第一个p0处画一个圆。第二个p1处画一个控制点圆,最后画一个。
        canvas.drawCircle(moveX, moveY, 10f, getPaintCir(Paint.Style.FILL))
        quePath.quadTo(moveX, moveY, 320f, 0f)
        canvas.drawPath(quePath, getPaint(Paint.Style.STROKE))
    }

    override fun onTouchEvent(event: MotionEvent): Boolean {
        when (event.action) {
            ACTION_DOWN,
            ACTION_MOVE -> {
                //在控制点附近范围内部,进行移动
                Log.e(
                    "x=",
                    "onTouchEvent: (x,y)" + (event.x - width / 2).toInt() + ":" + (-(event.y - height / 2)).toInt()
                )
                //将手势坐标转换为屏幕坐标
                moveX = event.x - width / 2
                moveY = -(event.y - height / 2)
                invalidate()
            }
        }
        return true
    }

}

private fun getPaint(style: Paint.Style): Paint {
    val gPaint = Paint()
    gPaint.color = Color.BLUE
    gPaint.strokeWidth = 2f
    gPaint.isAntiAlias = true
    gPaint.style = style
    gPaint.textSize = 26f
    gPaint.color = Color.argb(255, 75, 151, 79)
    return gPaint
}

private fun getPaint(style: Paint.Style, color: Int): Paint {
    val gPaint = Paint()
    gPaint.color = Color.BLUE
    gPaint.strokeWidth = 2f
    gPaint.isAntiAlias = true
    gPaint.style = style
    gPaint.textSize = 26f
    gPaint.color = color
    return gPaint
}

private fun getPaintCir(fill: Paint.Style): Paint {
    val gPaint = Paint()
    gPaint.strokeWidth = 12f
    gPaint.isAntiAlias = true
    gPaint.style = fill
    gPaint.textSize = 26f
    gPaint.color = Color.argb(255, 111, 111, 111)
    return gPaint
}