package com.example.draw_android.section16_line_chart.b_basic_area_chart
/*app/src/main/java/com/example/draw_android/section16_line_chart/b_basic_area_chart/BasicAreaChartView.kt*/
import android.animation.ValueAnimator
import android.content.Context
import android.graphics.*
import android.graphics.text.MeasuredText
import android.util.AttributeSet
import android.util.Log
import android.view.View

/**
 * Created by wangfei44 on 2022/2/4.
 */
class BasicAreaChartView constructor(context: Context, attributeSet: AttributeSet) :
    View(context, attributeSet) {
    private val lineLength = 20f
    private val margin = 150f
    private val data = arrayListOf("Mon", "Tue", "Wed", "Thu", "Fri", "Sat", "Sun")
    private val dataY = arrayListOf(820, 932, 901, 934, 1290, 1330, 1320)
    private val linePaint = Paint().apply {
        color = Color.argb(99, 123, 123, 123)
        style = Paint.Style.STROKE
        strokeWidth = 3f
        isAntiAlias = true
    }
    private val lineDataPaint = Paint().apply {
        color = Color.argb(222, 123, 123, 223)
        style = Paint.Style.STROKE
        strokeWidth = 5f
        isAntiAlias = true
    }
    private val circleDataPaint = Paint().apply {
        color = Color.WHITE
        style = Paint.Style.FILL
        strokeWidth = 5f
        isAntiAlias = true
    }
    private val textPaint = Paint().apply {
        color = Color.argb(242, 123, 123, 123)
        style = Paint.Style.FILL
        strokeWidth = 4f
        textSize = 24f
        isAntiAlias = true
    }
    private val innerPaint = Paint().apply {
        color = Color.argb(192, 123, 123, 223)
        style = Paint.Style.FILL
        strokeWidth = 5f
        isAntiAlias = true
    }
    private var mCurAnimValue: Float = 0f
    init {
        val animator = ValueAnimator.ofFloat(0f, 2f)
        animator.repeatCount = ValueAnimator.INFINITE
        animator.addUpdateListener { animation ->
            mCurAnimValue = animation.animatedValue as Float
            postInvalidate()
        }
        animator.duration = 2000
        animator.start()
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        drawScaleLine(canvas)
        drawScaleText(canvas)
        drawData(canvas)

    }

    //绘制刻度
    private fun drawScaleLine(canvas: Canvas) {
        canvas.translate(0f, height.toFloat())
        canvas.scale(1f, -1f)
        canvas.translate(margin, margin)


        //绘制X轴的刻度
        canvas.save()
        val spaceWidth = (width - margin * 2) / (data.size - 1)
        for (index in 0 until data.size) {
            //绘制X轴上刻度线
            canvas.drawLine(0f, 0f, 0f, -lineLength, linePaint)
            //通过水平变换移动画布来循环绘制刻度
            canvas.translate(spaceWidth, 0f)
        }
        //恢复画布坐标系到左下角。
        canvas.restore()

        //绘制竖直方向的线
        canvas.save()
        val spaceHeight = (height - margin * 2) / 5
        for (index in 0..5) {
            //绘制平行于X轴线
            canvas.drawLine(0f, 0f, width.toFloat() - margin * 2, 0f, linePaint)
            canvas.translate(0f, spaceHeight)
        }
        canvas.restore()

    }

    //绘制文字
    private fun drawScaleText(canvas: Canvas) {
        canvas.save()
        val spaceWidth = (width - margin * 2) / (data.size - 1)
        for (index in 0 until data.size) {
            val str = data[index]
            val rect = Rect()
            textPaint.getTextBounds(str, 0, str.length, rect)
            canvas.save()
            canvas.scale(1f, -1f)
            canvas.translate(-rect.width() / 2f, rect.height().toFloat() + lineLength)
            canvas.drawText(data[index], 0f, 0f, textPaint)
            canvas.restore()
            canvas.translate(spaceWidth, 0f)
        }
        canvas.restore()

        canvas.save()
        val spaceHeight = (height - margin * 2) / 5
        for (index in 0 until 6) {
            val str = (300 * index).toString()
            val rect = Rect()
            textPaint.getTextBounds(str, 0, str.length, rect)
            canvas.save()
            canvas.scale(1f, -1f)
            canvas.translate(-rect.width().toFloat() - lineLength, rect.height() / 2f)
            canvas.drawText(str, 0f, 0f, textPaint)
            canvas.restore()
            canvas.translate(0f, spaceHeight)
        }
        canvas.restore()
    }

    private fun drawData(canvas: Canvas) {
        val spaceWidth = (width - margin * 2) / (data.size - 1)
        val scaleYH = (height - margin * 2) / 1500

        //绘制闭合区域
        val innerPath = Path()
        innerPath.moveTo(0f, scaleYH * dataY[0])
        for (index in 1 until dataY.size) {
            innerPath.lineTo(spaceWidth * index, scaleYH * dataY[index].toFloat())
        }

        val mPathMeasure=PathMeasure(innerPath, false)
        val length:Float= mPathMeasure.length
        val stop: Float = length
        val start = 0f
        //用于截取整个path中某个片段,通过参数startD和stopD来控制截取的长度,
        // 并将截取后的path保存到参数dst中,最后一个参数表示起始点是否使用moveTo将路径的新起始点移到结果path的起始点中,
        // 通常设置为true
        val resultGreenPath=Path()
        mPathMeasure.getSegment(start, stop*(mCurAnimValue), resultGreenPath, true)
        mPathMeasure.setPath(resultGreenPath, false)
        val pos = FloatArray(2)
        mPathMeasure.getPosTan(length - 1, pos, null)

        resultGreenPath.lineTo(pos[0],0f)
        resultGreenPath.lineTo(0f,0f)
        resultGreenPath.close()
        canvas.drawPath(resultGreenPath, innerPaint)



        val path = Path()
        path.moveTo(0f, scaleYH * dataY[0])
        for (index in 1 until dataY.size) {
            path.lineTo(spaceWidth * index, scaleYH * dataY[index].toFloat())
        }
        val mPathMeasure1=PathMeasure(innerPath, false)
        val length1:Float= mPathMeasure1.length
        val stop1: Float = length1
        val start1 = 0f
        val resultGreenPath1=Path()
        mPathMeasure.getSegment(start1, stop1*(mCurAnimValue), resultGreenPath1, true)
        mPathMeasure.setPath(resultGreenPath1, false)
        val pos1 = FloatArray(2)
        mPathMeasure.getPosTan(length1 - 1, pos1, null)

        resultGreenPath1.lineTo(pos1[0],0f)
        canvas.drawPath(resultGreenPath1, lineDataPaint)
        //圆圈内白色部分
        for (index in 1 until dataY.size) {
            canvas.drawCircle(
                spaceWidth * index,
                scaleYH * dataY[index].toFloat(),
                6f,
                circleDataPaint
            )
        }
        //圆圈外部蓝色部分
        for (index in 0 until dataY.size) {
            canvas.drawCircle(
                spaceWidth * index,
                scaleYH * dataY[index].toFloat(),
                6f,
                lineDataPaint
            )
        }
    }
}