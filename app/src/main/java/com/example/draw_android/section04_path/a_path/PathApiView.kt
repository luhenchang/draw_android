package com.example.draw_android.section04_path.a_path

import android.content.Context
import android.graphics.*
import android.util.AttributeSet
import android.util.Log
import android.view.View
import kotlin.math.cos
import kotlin.math.min

/**
 * Created by wangfei44 on 2022/12/30.
 */
class PathApiView constructor(context: Context, attributeSet: AttributeSet) :
    View(context, attributeSet) {
    private var radius: Float = 0f
    private var mAxisWidth = 0f
    val TAG = this.javaClass.name
    lateinit var path: Path
    private lateinit var pathRect: Path
    lateinit var paint: Paint
    private lateinit var paintRec: Paint
    private lateinit var paintArc: Paint
    lateinit var paintRecArc: Paint
    lateinit var drawPaint: Paint

    //雷达图表
    var mWidth:Int = 0
    var mHeight:Int = 0
    private lateinit var radarChartPaint: Paint
    private lateinit var radiusPaint: Paint
    private lateinit var lineFillPaint: Paint
    private lateinit var linePaint: Paint

    private val data = arrayOf(
        arrayListOf(70, 100, 20, 5, 21, 99),
        arrayListOf(100, 120, 50, 75, 121, 99),
        arrayListOf(117, 211, 259, 232, 190, 200),
        arrayListOf(217, 240, 259, 282, 190, 120)
    )
    private val cos30 = cos(Math.PI/180*30).toFloat()
    private val cos60 = cos(Math.PI/180*60).toFloat()

    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
        super.onSizeChanged(w, h, oldw, oldh)
        mWidth = w
        mHeight = h
        path = Path().apply {
            moveTo(0f, 0f)
            lineTo(width.toFloat(), height.toFloat())
        }
        pathRect = Path().apply {
            moveTo(0f, 0f)
            lineTo(200f, 0f)
            lineTo(200f,100f)
            lineTo(0f,100f)
            close()
        }
        paint = Paint().apply {
            color = Color.RED
            strokeWidth = 11f
            style = Paint.Style.STROKE
        }
        drawPaint = Paint().apply {
            color = Color.RED
            strokeWidth = 11f
            style = Paint.Style.FILL
        }
        paintRec = Paint().apply {
            color = Color.YELLOW
            strokeWidth = 11f
            style = Paint.Style.STROKE
        }

        paintArc = Paint().apply {
            color = Color.RED
            strokeWidth = 11f
            style = Paint.Style.FILL
        }
        paintRecArc = Paint().apply {
            color = Color.YELLOW
            strokeWidth = 11f
            style = Paint.Style.FILL
        }

        //3、复杂图形
        //获取坐标轴的长
        mAxisWidth = min(mWidth, mHeight) * 3 / 4f / 2
        radarChartPaint = Paint().apply {
            color = Color.argb(77,227,192,105)
            strokeWidth = 4f
            style = Paint.Style.STROKE
            isAntiAlias = true
        }
        radiusPaint  = Paint().apply {
            color = Color.argb(44,227,192,105)
            strokeWidth = 4f
            style = Paint.Style.STROKE
            isAntiAlias = true
        }
        linePaint  = Paint().apply {
            color = Color.argb(200,171,89,53)
            strokeWidth = 4f
            style = Paint.Style.STROKE
            isAntiAlias = true
        }
        lineFillPaint = Paint().apply {
            color = Color.argb(100,171,89,53)
            strokeWidth = 4f
            style = Paint.Style.FILL
            isAntiAlias = true
        }

    }

    override fun onDraw(canvas: Canvas?) {
        super.onDraw(canvas)
        //六、 Path-线段
        //drawLine(canvas)
        //七、drawOval
        //drawOval(canvas)

        //八、drawPaint
        //drawPaint(canvas)

        //九、drawPath
        //Path-矩形
        //drawRect(canvas)
        //drawRadarChart(canvas)

        //Path-复杂图形
        drawComplexView(canvas)





        //4、裁剪
        //drawArc(canvas)
    }

    private fun drawComplexView(canvas: Canvas?) {
        if (null == canvas){
            Log.e(TAG,"drawComplexView::canvas is null return")
            return
        }
        canvas.translate(width / 2f, height / 2f)
        canvas.scale(1f, -1f)

        val path = Path().apply {
            moveTo(0f,0f)
            lineTo(200f,200f)
        }
        val path1 = Path().apply {
            moveTo(0f, 0f)
            lineTo(0f, 100f)
            addArc(0f, 100f, 100f, 0f, 0f, (Math.PI / 180 * 60).toFloat())
        }
        canvas.drawPath(path1, paint)

    }


    private fun drawRadarChart(canvas: Canvas?) {
        //1、首先将坐标系进行平移变换到屏幕中心
        if (null == canvas){
            Log.e(TAG,"drawRadarChart::canvas is null return")
            return
        }
        canvas.drawRGB(0, 0, 0)
        canvas.translate(width / 2f, height / 2f)
        canvas.scale(1f,-1f)
        canvas.save()
        //2、绘制坐标轴
        val path = Path()
        path.moveTo(0f, mAxisWidth)
        path.lineTo(0f, -mAxisWidth)
        for (index in 0 until 3) {
            canvas.rotate(60f)
            canvas.drawPath(path, radarChartPaint)
        }
        canvas.restore()
        //绘制圆环
        for (index in 1 ..5){
            radius += mAxisWidth / 5
            canvas.drawCircle(0f, 0f, radius, radiusPaint)
        }
        //绘制雷达多边形
        //val data = arrayListOf(70, 200, 180, 190, 220, 100)
        data.forEach{
            drawEveryPath(it, canvas)
        }

    }

    private fun drawEveryPath(
        it: ArrayList<Int>,
        canvas: Canvas
    ) {
        val mPath = Path()
        //我们最大数据默认为300，那么将数据映射到坐标系，即单位数据所占的坐标系中实际长度
        val each = mAxisWidth / 300
        it.forEachIndexed { index, data ->
            Log.e("Hellodata", data.toString())
            when (index) {
                0 ->
                    mPath.moveTo(data * cos30 * each, data * cos60 * each)
                1 ->
                    mPath.lineTo(data * cos30 * each, -data * cos60 * each)
                2 ->
                    mPath.lineTo(0f, -data.toFloat() * each)
                3 ->
                    mPath.lineTo(-data * cos30 * each, -data * cos60 * each)
                4 ->
                    mPath.lineTo(-data * cos30 * each, data * cos60 * each)
                5 ->
                    mPath.lineTo(0f, data.toFloat() * each)

            }
        }
        mPath.close()
        canvas.drawPath(mPath, lineFillPaint)
        canvas.drawPath(mPath, linePaint)
    }

    private fun drawArc(canvas: Canvas?) {
        val path = Path()
        path.addCircle(100f,100f,50f, Path.Direction.CCW)
        val clipOk =canvas?.clipOutPath(path)
        Log.e("clickOK=",clipOk.toString())

        canvas?.drawArc(0f,0f,200f,200f,0f,60f,true,paintArc)
        canvas?.drawArc(0f,0f,200f,200f,60f,300f,true,paintRecArc)


        //canvas?.drawColor(Color.WHITE) //剪裁过后，在画布上执行绘制操作
//        canvas?.save()
//        canvas?.clipRect(110, 110, 190, 190) //第一次裁剪
//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
//            canvas?.clipOutRect(130f, 130f, 170f, 170f)
//        } //第二次裁剪
//
//        canvas?.drawColor(Color.WHITE) //剪裁过后，在画布上执行绘制操作
//        canvas?.restore()
    }

    //1、绘制线段
    private fun drawLine(canvas: Canvas?) {
        //canvas?.drawPath(path, paint)
        canvas?.drawLine(0f,0f,400f,0f,paint)
    }
    //2、绘制矩形
    private fun drawRect(canvas: Canvas?) {
        canvas?.translate(100f,100f)
        //canvas?.drawPath(pathRect,paint)
        //Rect和RectF等绘制矩形，都试一试
        val rectF = RectF(0f,0f,200f,100f)
        canvas?.drawRect(rectF,paint)
    }

    //七、drawOval
    private fun drawOval(canvas: Canvas?) {
        if (null == canvas){
            Log.e(TAG,"drawRadarChart::drawOvl is null return")
            return
        }
        canvas.translate(width / 2f, height / 2f)
        canvas.drawOval(-100f,-150f,100f,150f,paint)

        val rectF = RectF(-100f,-150f,100f,150f)
        canvas.drawOval(rectF,paint)
    }

    //八、drawPaint
    private fun drawPaint(canvas: Canvas?) {
        if (null == canvas){
            Log.e(TAG,"drawRadarChart::drawOvl is null return")
            return
        }
        canvas.translate(width / 2f, height / 2f)
        canvas.drawPaint(drawPaint)
    }



}