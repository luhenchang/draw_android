package com.example.draw_android.section05_canvas_clip.a_canvas_clip

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.Path
import android.os.Handler
import android.os.Looper
import android.util.AttributeSet
import android.view.View
import com.example.draw_android.R

class ClipClockView : View {


    private var routeAnge: Float = 0f
    private lateinit var bitmap: Bitmap
    private val circlePath = Path()
    private val innerCirclePath = Path()

    private val rectPath = Path()

    private val circlePathInner = Path()


    private var clipPath = Path()
    private lateinit var linePaint: Paint
    private lateinit var textSizePaint: Paint
    private lateinit var controlPaint: Paint


    constructor(context: Context?) : super(context) {
        init()
    }

    constructor(context: Context, attrs: AttributeSet) : super(context, attrs) {
        init()
    }

    constructor(context: Context?, attrs: AttributeSet?, defStyleAttr: Int) : super(
        context,
        attrs,
        defStyleAttr
    ) {
        init()
    }

    private fun init() {
        linePaint = Paint().apply {
            style = Paint.Style.STROKE
            isAntiAlias = true
            strokeWidth = 4f
            color = Color.parseColor("#F5F1EFEF")

        }

        controlPaint = Paint().apply {
            style = Paint.Style.FILL
            strokeWidth = 2f
            color = Color.RED
        }

        textSizePaint = Paint().apply {
            style = Paint.Style.STROKE
            isAntiAlias = true
            strokeWidth = 4f
            textSize = 46f
            letterSpacing = 0.2f
            color = Color.parseColor("#F5F1EFEF")
        }
        clipPath = Path()
        bitmap = BitmapFactory.decodeResource(resources, R.drawable.img_lp)


    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        //clipStudy(canvas)
        //clipClock(canvas)
        clipAnimalClock(canvas)
    }


    private fun clipStudy(canvas: Canvas) {
        //1、绘制灰色背景
        canvas.drawColor(Color.GRAY)
        //2、屏幕中心为圆点，半径为200像素的圆圈
        circlePath.addCircle(width / 2f, height / 2f, 200f, Path.Direction.CCW)
        //创建巨型区域并添加到circlePath取并集
//        rectPath.addRect(
//            width / 2f - 50f,
//            height / 2f - 250,
//            width / 2f + 50f,
//            height / 2f,
//            Path.Direction.CCW
//        )
        //创建弧度区域
        rectPath.moveTo(width / 2f - 50f, height / 2f - 190f)
        rectPath.quadTo(width / 2f, height / 2f - 230f, width / 2f + 50f, height / 2f - 190f)
        rectPath.close()
        circlePath.addPath(rectPath)
        //3、裁剪圆形区域
        canvas.clipPath(circlePath)
        //4、在限制区域进行绘制绿色
        canvas.drawColor(Color.GREEN)

        //5、裁剪一个更小区域的圆圈
        innerCirclePath.addCircle(width / 2f, height / 2f, 150f, Path.Direction.CCW)
        canvas.clipPath(innerCirclePath)
        canvas.drawColor(Color.GRAY)
    }

    private fun clipClock(canvas: Canvas) {
        //1、绘制灰色背景
        canvas.drawColor(Color.GRAY)
        //2、屏幕中心为圆点，半径为200像素的圆圈
        circlePath.addCircle(width / 2f, height / 2f, 200f, Path.Direction.CCW)
        //3、曲线路径
        rectPath.moveTo(width / 2f - 80f, height / 2f - 175f)
        rectPath.quadTo(width / 2f, height / 2f - 250f, width / 2f + 80f, height / 2f - 180f)
        rectPath.close()
        circlePath.addPath(rectPath)
        canvas.save()
        //4、裁剪
        canvas.clipPath(circlePath)
        canvas.drawColor(Color.GRAY)

        //5、绘制线条
        for (index in 0 until 100) {
            canvas.save()
            canvas.translate(width / 2f, height / 2f)
            canvas.rotate(3.6f * index)
            canvas.drawLine(0f, 0f, 0f, -250f, linePaint)
            canvas.restore()
        }
        canvas.restore()
        //6、裁剪内部区域
        circlePathInner.addCircle(width / 2f, height / 2f, 166f, Path.Direction.CCW)
        canvas.save()
        canvas.clipPath(circlePathInner)
        //7、覆盖中间部分，达到裁剪环形效果
        canvas.drawColor(Color.GRAY)
        canvas.restore()
    }


    private fun clipAnimalClock(canvas: Canvas) {
        canvas.translate(width / 2f, height / 2f)
        //1、绘制灰色背景
        canvas.drawColor(Color.GRAY)
        //2、绘制线条
        for (index in 0 until 100) {
            canvas.save()
            canvas.rotate(3.6f * index)
            canvas.drawLine(0f, 0f, 0f, -250f, linePaint)
            canvas.restore()
        }

        //2、屏幕中心为圆点，半径为200像素的圆圈
        circlePath.reset()
        circlePath.addCircle(0f, 0f, 200f, Path.Direction.CW)
        //3、曲线路径
        rectPath.reset()
        rectPath.moveTo(-80f, -175f)
        rectPath.quadTo(0f, -250f, 80f, -180f)
        rectPath.close()
        circlePath.addPath(rectPath)
        canvas.save()

        //4、旋转画布，让其裁剪区域不断变换
        canvas.rotate(routeAnge)
        routeAnge += 2
        //5、裁剪
        canvas.clipOutPath(circlePath)
        canvas.drawColor(Color.GRAY)

        canvas.restore()


        //6、裁剪内部区域
        circlePathInner.reset()
        circlePathInner.addCircle(0f, 0f, 166f, Path.Direction.CCW)


        canvas.clipPath(circlePathInner)
        //7、覆盖中间部分，达到裁剪环形效果
        canvas.drawColor(Color.GRAY)

        canvas.save()

        //4、旋转画布，让其裁剪区域不断变换
        canvas.rotate(routeAnge)
        routeAnge += 2
        //小红点
        canvas.drawCircle(0f, -150f, 10f, controlPaint)
        canvas.restore()

        // 测量文字的宽度
        val textWidth = textSizePaint.measureText("11:23")

        // 获取文字的高度
        val fontMetrics = textSizePaint.fontMetrics
        val  textHeight = fontMetrics.bottom - fontMetrics.top
        canvas.drawText("11:23",-textWidth/2f,textHeight/2f,textSizePaint)

    }

    //开始执行Runnable
    private val handler = Handler(Looper.getMainLooper())
    private var runnable: Runnable = object : Runnable {
        override fun run() {
            // 执行需要刷新的操作
            invalidate() // 重新绘制视图
            handler.postDelayed(this, 50) // 1秒后再次执行
        }
    }

    override fun onAttachedToWindow() {
        super.onAttachedToWindow()
        handler.postDelayed(runnable, 50)
    }

    // 在View销毁时停止刷新
    override fun onDetachedFromWindow() {
        super.onDetachedFromWindow()
        handler.removeCallbacks(runnable)
    }

}