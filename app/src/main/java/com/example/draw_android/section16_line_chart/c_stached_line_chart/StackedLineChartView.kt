package com.example.draw_android.section16_line_chart.c_stached_line_chart

import android.content.Context
import android.graphics.*
import android.util.AttributeSet
import android.util.Log
import android.view.MotionEvent
import android.view.View
import kotlin.math.abs
import kotlin.math.min

/**
 * Created by wangfei44 on 2022/2/6.
 */
class StackedLineChartView constructor(context: Context, attributeSet: AttributeSet) :
    View(context, attributeSet) {
    private val lineLength = 20f
    private val margin = 150f
    private val data = arrayListOf("Mon", "Tue", "Wed", "Thu", "Fri", "Sat", "Sun")
    private val dataArray = arrayListOf(
        Series(
            "Email",
            "line",
            "Total",
            arrayListOf(120, 132, 101, 134, 90, 230, 210),
            Color.argb(255, 141, 192, 240)
        ),
        Series(
            "Union Ads",
            "line",
            "Total",
            arrayListOf(350, 332, 201, 294, 290, 330, 410),
            Color.argb(255, 217, 131, 131)

        ),
        Series(
            "Video Ads",
            "line",
            "Total",
            arrayListOf(820, 890, 790, 800, 900, 1300, 1400),
            Color.argb(255, 237, 189, 96)

        ),
        Series(
            "Direct",
            "line",
            "Total",
            arrayListOf(500, 560, 500, 540, 530, 800, 910),
            Color.argb(255, 181, 213, 194)
        ),
        Series(
            "Search Engine",
            "line",
            "Total",
            arrayListOf(1640, 1860, 1802, 1868, 2580, 2660, 2640),
            Color.argb(255, 119, 135, 194)
        )
    )
    private val linePaint = Paint().apply {
        color = Color.argb(66, 123, 123, 123)
        style = Paint.Style.STROKE
        strokeWidth = 3f
        isAntiAlias = true
    }
    private val dataPaint = Paint().apply {
        color = Color.argb(66, 123, 123, 123)
        style = Paint.Style.STROKE
        strokeWidth = 3f
        isAntiAlias = true
    }

    private val circlePaint = Paint().apply {
        color = Color.WHITE
        style = Paint.Style.STROKE
        strokeWidth = 4f
        isAntiAlias = true
    }
    private val innerCirclePaint = Paint().apply {
        color = Color.WHITE
        style = Paint.Style.FILL
        strokeWidth = 4f
        isAntiAlias = true
    }
    private val textPaint = Paint().apply {
        color = Color.argb(242, 123, 123, 123)
        style = Paint.Style.FILL
        strokeWidth = 4f
        textSize = 24f
        isAntiAlias = true
    }

    init {

    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        drawScaleLine(canvas)
        drawScaleText(canvas)
        drawData(canvas)
        drawLine(canvas)
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
        val spaceHeight = (height - margin * 2) / 6
        for (index in 0..6) {
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
        val spaceHeight = (height - margin * 2) / 6
        for (index in 0 until 7) {
            val str = (500 * index).toString()
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
        //水平每个刻度之间的间距
        val spaceWidth = (width - margin * 2) / (data.size - 1)
        //计算每一个单位真实的实际像素高度
        val scaleHeight = (height - margin * 2) / 3000
        //循环绘制数据线条
        for (index in 0 until dataArray.size) {
            val dataList = dataArray[index].data
            val path = Path()
            for (ind in 0 until dataList.size) {
                if (ind == 0) {
                    path.moveTo(ind * spaceWidth, dataList[ind] * scaleHeight)
                } else {
                    path.lineTo(ind * spaceWidth, dataList[ind] * scaleHeight)
                }
            }
            canvas.drawPath(path, dataPaint.apply {
                color = dataArray[index].color
            })
        }
        //这里进行绘制线上的点圆圈，和画线分开保证覆盖在线上面。
        for (index in 0 until dataArray.size) {
            val dataList = dataArray[index].data
            for (ind in 0 until dataList.size) {
                if (ind == 0) {
                    canvas.drawCircle(
                        ind * spaceWidth,
                        dataList[ind] * scaleHeight,
                        if (visible&&ind == recentIndex) {
                            8f
                        } else {
                            6f
                        },
                        innerCirclePaint.apply {
                            color = Color.WHITE
                        })
                    canvas.drawCircle(
                        ind * spaceWidth,
                        dataList[ind] * scaleHeight,
                        if (visible&&ind == recentIndex) {
                            6f
                        } else {
                            4f
                        },
                        circlePaint.apply {
                            color = dataArray[index].color
                        })
                } else {
                    canvas.drawCircle(
                        ind * spaceWidth,
                        dataList[ind] * scaleHeight,
                        if (visible&&ind == recentIndex) {
                            8f
                        } else {
                            6f
                        },
                        innerCirclePaint.apply {
                            color = Color.WHITE
                        })
                    canvas.drawCircle(
                        ind * spaceWidth,
                        dataList[ind] * scaleHeight,
                        if (visible&&ind == recentIndex) {
                            6f
                        } else {
                            4f
                        },
                        circlePaint.apply {
                            color = dataArray[index].color
                        })
                }
            }
        }

    }

    private var minValue: Float = 0f
    private var recentIndex: Int = 0
    private var visible: Boolean = false
    private val recentPaint = Paint().apply {
        color = Color.argb(200, 123, 223, 136)
        style = Paint.Style.STROKE
        strokeWidth = 3f
        isAntiAlias = true
    }
    private val rectPaint = Paint().apply {
        color = Color.WHITE
        style = Paint.Style.FILL
        strokeWidth = 4f
        isAntiAlias = true
        setShadowLayer(10f, 1f, 1f, Color.GRAY)
    }

    private fun drawLine(canvas: Canvas) {
        val spaceWidth = (width - margin * 2) / (data.size - 1)
        val minMarginLeft = recentIndex * spaceWidth
        if (visible) {
            canvas.drawLine(minMarginLeft, 0f, minMarginLeft, height - margin * 2, recentPaint)
            canvas.drawRoundRect(
                canvasX,
                canvasY,
                canvasX + 400,
                canvasY - 300,
                20f,
                20f,
                rectPaint
            )
            canvas.save()
            canvas.translate(canvasX, canvasY - 300)
            canvas.scale(1f, -1f)
            canvas.drawText(data[recentIndex], 20f, -270f, textPaint)
            for (index in 0 until dataArray.size) {
                canvas.drawCircle(20f, -250f + 50 * index, 5f, circlePaint.apply {
                    color = dataArray[index].color
                })
                canvas.save()
                canvas.translate(0f, 10f)
                canvas.drawText(dataArray[index].name, 40f, -250f + 50 * index, textPaint)
                canvas.drawText(
                    dataArray[index].data[recentIndex].toString(),
                    320f,
                    -250f + 50 * index,
                    textPaint
                )
                canvas.restore()
            }
            canvas.restore()
        }
    }

    private var canvasX = 0f
    private var canvasY = 0f
    override fun dispatchTouchEvent(event: MotionEvent?): Boolean {
        return super.dispatchTouchEvent(event)
    }

    /**
     * 解决onTouchEvent警告⚠️
     */
    override fun performClick(): Boolean {
        return super.performClick()
    }

    override fun onTouchEvent(event: MotionEvent): Boolean {
        when (event.action) {
            MotionEvent.ACTION_DOWN -> {
                visible = true
                performClick()
                invalidate()
            }
            MotionEvent.ACTION_MOVE -> {
                //水平方向每个刻度之间的实际像素距离
                val spaceWidth = (width - margin * 2) / (data.size - 1)
                //默认初始化最小距离折线点的距离为手势映射到画布坐标系的距离
                minValue = event.x - margin
                //遍历每个刻度比较那个距离手势最近。获取最近距离的折线点
                for (index in 0 until data.size) {
                    //event.x - margin为手势坐标映射到画布坐标系之后的位置。这里取绝对值，我们根据观察在最近的左右只要最近就会出现线。
                    if (abs(event.x - margin - spaceWidth * index) <= minValue) {
                        minValue = abs(event.x - margin - spaceWidth * index)
                        recentIndex = index
                    }
                }
                canvasX = event.x - margin
                canvasY = height - event.y - margin
                //循环获取来距离最近的刻度，然后通过invalidate取刷新当然这里为了方便全局刷新了。可以局部刷新为了性能。
                invalidate()
            }
            MotionEvent.ACTION_UP -> {
                visible = false
                invalidate()
            }
        }
        return true

    }

    data class Series(
        val name: String,
        val type: String,
        val stack: String,
        val data: ArrayList<Int>,
        val color: Int
    )
}