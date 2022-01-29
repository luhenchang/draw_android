package com.example.draw_android.section16_line_chart.a_basic_line_chart
/*com/example/draw_android/section16_line_chart/a_basic_line_chart/BasicLineChartView.kt*/
import android.animation.ValueAnimator
import android.content.Context
import android.graphics.*
import android.util.AttributeSet
import android.view.View

/**
 * Created by wangfei44 on 2022/1/29.
 */
class BasicLineChartView constructor(context: Context, attributeSet: AttributeSet) :
    View(context, attributeSet) {
    private var mCurAnimValue: Float = 0f
    private val marginWidth = 60f
    private val xScaleText = arrayListOf("Mon", "Tue", "Wed", "Thu", "Fri", "Sat", "Sun")
    private val dataArray = arrayListOf(150, 230, 224, 218, 135, 147, 260)

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


        //画布整体向下平移
        canvas.translate(0f, height.toFloat())
        //画布沿X轴纵向翻转
        canvas.scale(1f, -1f)
        //原点位置画圆圈做为参照物
//        canvas.drawCircle(
//            0f, 0f, 60f,
//            Paint().apply {
//                color = Color.BLACK
//                style = Paint.Style.FILL
//            })

        drawBottomScaleLine(canvas)
        drawOtherScaleLine(canvas)
        drawData(canvas)
    }
    private fun drawData(canvas: Canvas) {
        val widthScale = width - 2 * marginWidth
        //水平方向总共7个天数,8个刻度短线，每一份刻度之间间距 = widthScale/7
        val eachScale = widthScale / 7f
        //每一刻度所占的实际像素
        val scale = (height - marginWidth * 2) / 300f
        canvas.save()
        canvas.translate(marginWidth, marginWidth)
        val path = Path()
        //绘制起点开始
        path.moveTo(eachScale / 2, dataArray[0] * scale)
        for (index in 1 until dataArray.size) {
            path.lineTo(eachScale / 2 + eachScale * index, dataArray[index] * scale)
        }


        val mPathMeasure=PathMeasure(path, false)
        val length:Float= mPathMeasure.length
        val stop: Float = length
        val start = 0f
        //用于截取整个path中某个片段,通过参数startD和stopD来控制截取的长度,
        // 并将截取后的path保存到参数dst中,最后一个参数表示起始点是否使用moveTo将路径的新起始点移到结果path的起始点中,
        // 通常设置为true
        val resultGreenPath=Path()
        mPathMeasure.getSegment(start, stop*(mCurAnimValue), resultGreenPath, true)

        canvas.drawPath(resultGreenPath, Paint().apply {
            color = Color.argb(255, 103, 123, 211)
            style = Paint.Style.STROKE
            strokeWidth = 6f
            isAntiAlias = true
        })
        for (index in 0 until dataArray.size){
            canvas.drawCircle(
                eachScale / 2 + eachScale * index,
                dataArray[index] * scale,
                6f,
                Paint().apply {
                    color = Color.WHITE
                    style = Paint.Style.FILL
                    strokeWidth = 6f
                    isAntiAlias = true
                })
            canvas.drawCircle(
                eachScale / 2 + eachScale * index,
                dataArray[index] * scale,
                8f,
                Paint().apply {
                    color = Color.argb(255, 103, 123, 211)
                    style = Paint.Style.STROKE
                    strokeWidth = 6f
                    isAntiAlias = true
                })
        }
        canvas.restore()
    }

    private fun drawBottomScaleLine(canvas: Canvas) {
        //绘制最底部一条横线
        canvas.drawLine(marginWidth, marginWidth, width - marginWidth, marginWidth,
            Paint().apply {
                color = Color.BLACK
                style = Paint.Style.STROKE
                strokeWidth = 2f
                isAntiAlias = true
            })

        //绘制水平方向刻度线
        //总的宽度= 总的画布宽度-两边的边距
        val widthScale = width - 2 * marginWidth
        //水平方向总共7个天数,8个刻度短线，每一份刻度之间间距 = widthScale/7
        val eachScale = widthScale / 7f
        //保存当前画布矩阵到堆栈
        canvas.save()
        for (index in 0 until 8) {
            canvas.drawLine(marginWidth, marginWidth, marginWidth, marginWidth - 10,
                Paint().apply {
                    color = Color.BLACK
                    style = Paint.Style.STROKE
                    strokeWidth = 2f
                    isAntiAlias = true
                })
            //画布从左往右逐步平移每个刻度，方便绘制。
            canvas.translate(eachScale, 0f)
        }
        //将上次保存的画布矩阵推出堆栈，画布坐标回到左下角
        canvas.restore()
        //绘制x轴的文字
        drawXScaleText(canvas)
    }

    private fun drawXScaleText(canvas: Canvas) {
        val xTextPaint = Paint().apply {
            color = Color.argb(255,111,111,111)
            style = Paint.Style.FILL
            strokeWidth = 4f
            textSize = 22f
            isAntiAlias = true
        }
        //总的宽度= 总的画布宽度-两边的边距
        val widthScale = width - 2 * marginWidth
        val eachScale = widthScale / 7f
        //保存当前画布矩阵到堆栈
        canvas.save()
        canvas.translate(marginWidth, 20f)
        for (index in 0 until 7) {
            //画布从左往右逐步平移每个刻度，方便绘制。
            canvas.save()
            //字体是上下颠倒，这里可以通过scale进行反转
            canvas.scale(1f, -1f)
            val rect = Rect()
            xTextPaint.getTextBounds(xScaleText[index], 0, xScaleText[index].length, rect)
            canvas.drawText(
                xScaleText[index], eachScale / 2f - rect.width() / 2f,
                0f,
                xTextPaint
            )
            canvas.restore()
            canvas.translate(eachScale, 0f)
        }
        //将上次保存的画布矩阵推出堆栈，画布坐标回到左下角
        canvas.restore()
    }

    private fun drawOtherScaleLine(canvas: Canvas) {
        //Y轴方向总共6条线进行逐步绘制
        val eachYScale = (height - 2 * marginWidth) / 6f
        canvas.save()
        for (index in 0 until 6) {
            canvas.translate(0f, eachYScale)
            canvas.drawLine(marginWidth, marginWidth, width - marginWidth, marginWidth,
                Paint().apply {
                    color = Color.argb(255, 215, 215, 215)
                    style = Paint.Style.STROKE
                    strokeWidth = 2f
                    isAntiAlias = true
                })
        }
        canvas.restore()
        drawYScaleText(canvas)
    }

    private fun drawYScaleText(canvas: Canvas) {
        val xTextPaint = Paint().apply {
            color = Color.argb(255,111,111,111)
            style = Paint.Style.FILL
            strokeWidth = 4f
            textSize = 22f
            isAntiAlias = true
        }
        //Y轴方向总共6条线进行逐步绘制
        val eachYScale = (height - 2 * marginWidth) / 6f
        canvas.save()
        canvas.translate(marginWidth, marginWidth)
        for (index in 0 until 7) {
            //画布从左往右逐步平移每个刻度，方便绘制。
            canvas.save()
            //字体是上下颠倒，这里可以通过scale进行反转
            canvas.scale(1f, -1f)
            val rect = Rect()
            val yScaleText = (50 * index).toString()
            xTextPaint.getTextBounds(
                yScaleText,
                0,
                yScaleText.length,
                rect
            )
            canvas.drawText(
                yScaleText, -(rect.width() + 20f),
                rect.height() / 2f,
                xTextPaint
            )
            canvas.restore()
            canvas.translate(0f, eachYScale)
        }
        canvas.restore()

    }
}