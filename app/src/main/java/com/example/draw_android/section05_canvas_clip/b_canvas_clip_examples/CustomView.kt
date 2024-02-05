package com.example.draw_android.section05_canvas_clip.b_canvas_clip_examples

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.Path
import android.graphics.PointF
import android.graphics.RectF
import android.graphics.Region
import android.util.AttributeSet
import android.view.MotionEvent
import android.view.View

class CustomView(context: Context, attrs: AttributeSet?) : View(context, attrs) {
    private val paint = Paint().apply {
        color = Color.BLACK
        strokeWidth = 5f
        style = Paint.Style.STROKE
    }

    private val points = mutableListOf<PointF>()

    init {
        // 初始化控制点
        for (i in 0 until 6) {
            points.add(PointF((i + 1) * 100f, 300f))
        }
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)

        // 绘制连线
        val path = Path()
        path.moveTo(points[0].x, points[0].y)
        for (i in 1 until points.size) {
            path.lineTo(points[i].x, points[i].y)
        }
        path.close()
        canvas.drawPath(path, paint)

        // 绘制控制点
        for (point in points) {
            canvas.drawCircle(point.x, point.y, 10f, paint)
        }
    }

    override fun onTouchEvent(event: MotionEvent): Boolean {
        val x = event.x
        val y = event.y
        when (event.action) {
            MotionEvent.ACTION_DOWN -> {
                // 判断点击的是否是控制点
                for (point in points) {
                    if (x > point.x - 20 && x < point.x + 20 &&
                        y > point.y - 20 && y < point.y + 20
                    ) {
                        point.x = x
                        point.y = y
                        invalidate()
                        return true
                    }
                }
            }

            MotionEvent.ACTION_MOVE -> {
                // 判断移动的是否是控制点
                for (point in points) {
                    if (x > point.x - 20 && x < point.x + 20 &&
                        y > point.y - 20 && y < point.y + 20
                    ) {
                        val index = points.indexOf(point)
                        if (index == 0 || index == points.size - 1) {
                            // 第一个点或最后一个点不需要判断相交
                            point.x = x
                            point.y = y
                            invalidate()
                            return true
                        }
                        // 判断移动的点和相邻两个点的连线是否和非相邻的其他三条边相交
                        val prevPoint = points[index - 1]
                        val nextPoint = points[index + 1]
                        val path = Path()
                        path.moveTo(prevPoint.x, prevPoint.y)
                        path.lineTo(point.x, point.y)
                        path.lineTo(nextPoint.x, nextPoint.y)
                        val rect = RectF()
                        path.computeBounds(rect, true)
                        val region = Region()
                        region.setPath(
                            path,
                            Region(
                                rect.left.toInt(),
                                rect.top.toInt(),
                                rect.right.toInt(),
                                rect.bottom.toInt()
                            )
                        )
                        for (i in 0 until points.size) {
                            if (i != index - 1 && i != index && i != index + 1) {
                                val otherPoint = points[i]
                                if (region.contains(otherPoint.x.toInt(), otherPoint.y.toInt())) {
                                    // 如果相交，不移动点
                                    return true
                                }
                            }
                        }
                        point.x = x
                        point.y = y
                        invalidate()
                        return true
                    }
                }

            }

        }
        return true
    }
}
