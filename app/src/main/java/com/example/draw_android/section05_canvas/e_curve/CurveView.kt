package com.example.draw_android.section05_canvas.e_curve

import android.content.Context
import android.graphics.*
import android.os.Handler
import android.os.Looper
import android.util.AttributeSet
import android.util.Log
import android.view.MotionEvent
import android.view.MotionEvent.ACTION_DOWN
import android.view.MotionEvent.ACTION_MOVE
import android.view.View
import kotlin.math.max
import kotlin.math.pow
import kotlin.math.sqrt

/**
 * Created by wang fei on 2022/12/27.
 */
class CurveView :
    View {

    private var controlRect: Rect = Rect()

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

    constructor(context: Context) : super(context)

    constructor(context: Context, attrs: AttributeSet?) : super(context, attrs)

    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int) : super(
        context,
        attrs,
        defStyleAttr
    )

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        Log.e("onDraw=", "001")
        //绘制网格线//绘制文字x和y轴的
        BaseCanvasView.drawBaseBg(canvas, mWidth, mHeight)
        sizeChange = false
        //绘制直线方程
        drawLine(canvas)
        //绘制圆
        drawCircle(canvas)
        //二阶曲线
        drawQuz(canvas)
        //三阶曲线
        drawCubic(canvas)
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

    //三阶控制点
    private var cubicLeftRect = Rect()

    //三阶控制点
    private var cubicRightRect = Rect()
    private var moveCubeX: Float = 80f
    private var moveCubeY: Float = 80f
    private var moveCubeXX: Float = 240f
    private var moveCubeYY: Float = 80f
    private fun drawCubic(canvas: Canvas) {
        val cubicPath = Path()
        cubicPath.moveTo(0f, 0f)
        cubicLeftRect = Rect(
            (moveCubeX - 30f).toInt(),
            (moveCubeY - 30f).toInt(),
            (moveCubeX + 30).toInt(),
            (moveCubeY + 30f).toInt()
        )
        cubicRightRect = Rect(
            (moveCubeXX - 30f).toInt(),
            (moveCubeYY - 30f).toInt(),
            (moveCubeXX + 30).toInt(),
            (moveCubeYY + 30f).toInt()
        )
        val lineLeft = Path().apply {
            moveTo(0f, 0f)
            lineTo(moveCubeX, moveCubeY)
            lineTo(moveCubeXX, moveCubeYY)
            lineTo(320f, 0f)
        }
        canvas.drawPath(lineLeft, getPaint(Paint.Style.STROKE, Color.GRAY))
        canvas.drawCircle(moveCubeX, moveCubeY, 10f, getPaintCir(Paint.Style.FILL))
        canvas.drawCircle(moveCubeXX, moveCubeYY, 10f, getPaintCir(Paint.Style.FILL))
        cubicPath.cubicTo(moveCubeX, moveCubeY, moveCubeXX, moveCubeYY, 320f, 0f)
        canvas.drawPath(cubicPath, getPaint(Paint.Style.STROKE, Color.RED))
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
                //二阶曲线
                if (controlRect.contains(
                        (event.x - width / 2).toInt(),
                        (-(event.y - height / 2)).toInt()
                    )
                ) {
                    Log.e("点击来", "对")
                    moveX = event.x - width / 2
                    moveY = -(event.y - height / 2)
                    invalidate()
                    //三阶曲线控制点1
                } else if (cubicLeftRect.contains(
                        (event.x - width / 2).toInt(),
                        (-(event.y - height / 2)).toInt()
                    )
                ) {
                    moveCubeX = event.x - width / 2
                    moveCubeY = -(event.y - height / 2)
                    invalidate()
                    //三阶曲线控制点2
                } else if (cubicRightRect.contains(
                        (event.x - width / 2).toInt(),
                        (-(event.y - height / 2)).toInt()
                    )
                ) {
                    moveCubeXX = event.x - width / 2
                    moveCubeYY = -(event.y - height / 2)
                    invalidate()
                }
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