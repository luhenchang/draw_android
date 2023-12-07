package com.example.draw_android.section05_canvas.f_event

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.Path
import android.util.AttributeSet
import android.view.MotionEvent
import android.view.View

class PageTurnView(context: Context, attrs: AttributeSet? = null) : View(context, attrs) {

    private val pagePaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
        color = Color.RED
        style = Paint.Style.FILL
    }
    private val textPaint = Paint(Paint.ANTI_ALIAS_FLAG)
    private val shadowPaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
        color = Color.BLACK
        style = Paint.Style.FILL
    }

    private var currentPage = 0
    private var totalPage = 5

    private val cornerPath = Path()
    private val shadowPath = Path()

    init {
        pagePaint.color = Color.WHITE
        textPaint.color = Color.BLACK
        textPaint.textSize = 40f

        shadowPaint.color = Color.parseColor("#80000000") // 半透明黑色
        shadowPaint.style = Paint.Style.FILL
    }

    fun setTotalPages(pages: Int) {
        totalPage = pages
        invalidate()
    }

    fun setCurrentPage(page: Int) {
        currentPage = page
        invalidate()
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)

        // 绘制当前页
        drawPage(canvas, currentPage, 0f)

        // 绘制下一页，实现翻页效果
        if (currentPage < totalPage - 1) {
            drawPageTurn(canvas, currentPage + 1, width.toFloat())
        }
    }

    private fun drawPage(canvas: Canvas, page: Int, offset: Float) {
        canvas.save()
        canvas.translate(offset, 0f)

        // 绘制背景
        canvas.drawRect(0f, 0f, width.toFloat(), height.toFloat(), pagePaint)

        // 模拟文本内容，实际中可根据需求替换为真实的文本内容
        val text = "Page $page Content"
        val textWidth = textPaint.measureText(text)
        val x = (width - textWidth) / 2
        val y = height / 2f

        // 绘制文本
        canvas.drawText(text, x, y, textPaint)

        canvas.restore()
    }

    private fun drawPageTurn(canvas: Canvas, page: Int, offset: Float) {
        canvas.save()
        canvas.translate(offset, 0f)

        // 绘制背景
        canvas.drawRect(0f, 0f, width.toFloat(), height.toFloat(), pagePaint)

        // 模拟文本内容，实际中可根据需求替换为真实的文本内容
        val text = "Page $page Content"
        val textWidth = textPaint.measureText(text)
        val x = (width - textWidth) / 2
        val y = height / 2f

        // 绘制文本
        canvas.drawText(text, x, y, textPaint)

        // 绘制翻页阴影效果
        drawPageTurnShadow(canvas, offset)

        canvas.restore()
    }

    private fun drawPageTurnShadow(canvas: Canvas, offset: Float) {
        cornerPath.reset()
        shadowPath.reset()

        // 贝塞尔曲线的控制点和顶点
        val controlX = offset / 2f
        val controlY = height / 4f
        val endX = offset
        val endY = height.toFloat()

        // 设置贝塞尔曲线的路径
        cornerPath.moveTo(0f, 0f)
        cornerPath.lineTo(offset, 0f)
        cornerPath.lineTo(offset, height.toFloat())
        cornerPath.lineTo(0f, height.toFloat())
        cornerPath.close()

        // 添加贝塞尔曲线到阴影路径
        shadowPath.moveTo(offset, 0f)
        shadowPath.lineTo(offset, height.toFloat())
        shadowPath.lineTo(0f, height.toFloat())
        shadowPath.quadTo(controlX, controlY, endX, endY)
        shadowPath.close()

        // 绘制阴影
        canvas.drawPath(shadowPath, shadowPaint)

        // 绘制翻页边缘效果
        canvas.drawPath(cornerPath, pagePaint)
    }

    // 处理触摸事件，实现翻页效果
    override fun onTouchEvent(event: MotionEvent): Boolean {
        when (event.action) {
            MotionEvent.ACTION_DOWN -> {
                // 按下时记录起始位置
                return true
            }
            MotionEvent.ACTION_UP -> {
                // 抬起时判断翻到上一页还是下一页
                if (event.x < width / 2 && currentPage > 0) {
                    setCurrentPage(currentPage - 1)
                } else if (event.x >= width / 2 && currentPage < totalPage - 1) {
                    setCurrentPage(currentPage + 1)
                }
                return true
            }
        }
        return super.onTouchEvent(event)
    }
}
