package com.example.draw_android.section07_time_line.a_draw_time_line
/*com.example.draw_android.section07_time_line.a_draw_time_line.DrawTimeLineView*/
import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.Rect
import android.text.InputType
import android.util.AttributeSet
import android.util.Log
import android.view.KeyEvent
import android.view.MotionEvent
import android.view.View
import android.view.inputmethod.BaseInputConnection
import android.view.inputmethod.EditorInfo
import android.view.inputmethod.InputConnection
import android.view.inputmethod.InputMethodManager
import kotlin.math.ceil
import kotlin.math.floor

/**
 * Created by wangfei44 on 2022/2/7.
 */
class DrawTimeLineView constructor(context: Context, attributeSet: AttributeSet) :
    View(context, attributeSet) {
    private lateinit var viewCanvas: Canvas
    private var inputMethodManager: InputMethodManager =
        context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
    private var textInputString = ""
    private val dataList = arrayListOf(
        StudyLanguage("2015", "学习Java,Java作为前后端语言，真的太棒了", 1),
        StudyLanguage("2016", "学习H5,H5作为前端语言，真的不错，我们都可以尝试。", 2),
        StudyLanguage("2017", "学习Js,作为前端交互语言，我们也可以自定义绘制，学习了很多", 3),
        StudyLanguage(
            "2018",
            "学习Kotlin,作为Google推荐的以后官方语言，咋们还不学等啥呢？很多的框架例如携程，jetpack 里面的Compose等都是Kotlin语法，即使官方说不会废弃java但是种种的操作，那个不是让开发者转向kotlin呢？",
            0
        ),
        StudyLanguage("2018", "学习VUE作为前后端语言，真的太棒了", 2),
        StudyLanguage("2019", "学习C++作为前端语言，真的不错，我们都可以尝试。", 2),
        StudyLanguage("2020", "学习Flutter,作为前端交互语言，我们也可以自定义绘制，学习了很多", 3),
        StudyLanguage("2021", "学习Jetpack作为前后端语言，真的太棒了", 1),
        StudyLanguage("2022", "学习OpenGL作为前端语言，真的不错，我们都可以尝试。", 2),
    )

    init {

    }

    //距离左边距离
    private val margin = 100f

    //底部线的一半的高度
    private val rectHeight = 10f

    //底部线的长度
    private val rectWidth = 400f

    @SuppressLint("DrawAllocation")
    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        viewCanvas = canvas
        canvas.translate(0f, height.toFloat())
        canvas.scale(1f, -1f)
        //向右滑动,画布最左边距离屏幕左边，小于等于margin状态
        if (viewToY <= margin && viewToY > 0) {
            canvas.translate(margin + viewToY, height / 2f)
            //向左滑动,画布最右边距离屏幕右边，大于等于（总长度-width-margin）状态
        } else if (viewToY >= (width - margin - dataList.size * rectWidth) && viewToY < 0) {
            canvas.translate(margin + viewToY, height / 2f)
            //向右滑动,画布最左边距离屏幕左边，等于margin状态，但是继续想往右边滑动。你以为我还想让你滑动么？
        } else if (viewToY > margin) {
            canvas.translate(margin, height / 2f)
            //向左滑动,画布最右边距离屏幕右边等于margin，但是还想向左边滑动，当然不让其滑动了。
        } else if (viewToY < (width - margin - dataList.size * rectWidth)) {
            canvas.translate((width - margin - dataList.size * rectWidth), height / 2f)
        } else {//其他意外情况不用动。只要你考虑全没有意外。那到底咋们是否考虑全，这个问题留给你们自己测试。并解决，下节课我会这里提示的。如何测试，提示一下，删除这个else运行不就完事了。
            //应该这里每考虑到 viewToY = 0的情况吧。
            canvas.translate(margin , height / 2f)
        }
        canvas.save()
        for (index in 0 until dataList.size) {
            //绘制底部的绿色线条
            canvas.drawRoundRect(0f, rectHeight, rectWidth, -rectHeight, 10f, 10f, Paint().apply {
                color = getBottomRoundColor(dataList[index].state)
                style = Paint.Style.FILL
                strokeWidth = 20f
                isAntiAlias = true
                setShadowLayout(dataList[index].state)
            })
            //绘制绿色圆圈
            canvas.drawCircle(0f, 0f, 30f, Paint().apply {
                color = getBottomRoundColor(dataList[index].state)
                style = Paint.Style.FILL
                strokeWidth = 20f
                isAntiAlias = true
            })
            //绘制顶层圆圈
            canvas.drawCircle(0f, 0f, 18f, Paint().apply {
                color = getTopRoundColor(dataList[index].state)
                style = Paint.Style.FILL
                strokeWidth = 20f
                isAntiAlias = true
                setShadowLayer(10f, 5f, -6f, Color.argb(255, 50, 50, 50))
            })
            //白色圆环
            canvas.drawCircle(0f, 0f, 18f, Paint().apply {
                color = Color.argb(255, 255, 255, 255)
                style = Paint.Style.STROKE
                strokeWidth = 2f
                isAntiAlias = true
            })
            canvas.save()
            canvas.scale(1f, -1f)
            val paint = Paint().apply {
                color = Color.argb(255, 139, 171, 158)
                style = Paint.Style.FILL
                isAntiAlias = true
                textSize = 30f
                strokeWidth = 10f
            }
            val textWidth = paint.measureText(dataList[index].year)
            //绘制文字
            canvas.drawText(dataList[index].year, -textWidth / 2, -60f, paint)


            val rect = Rect()
            paint.getTextBounds(
                dataList[index].description,
                0,
                dataList[index].description.length,
                rect
            )
            val allWidth = rect.width()
            //我们规定每一行最大宽度为一个Item的宽度-40 = rectWidth - 40f
            var rowCount = ceil(allWidth / (rectWidth - 40))
            var lengthIndex = 0
            //这里通过行数+2增加绘制的行数来保证测量绘制过程的万无一失
            for (ind in 0 until (rowCount.toInt() + 2)) {
                var flag = true
                //作为容器来储存每一行的字符
                val buffer = StringBuffer()
                while (flag) {
                    //判断如果字符索引没有超出总共的索引那么继续向buffer里面添加字符
                    if ((lengthIndex < dataList[index].description.length)) {
                        buffer.append(dataList[index].description[lengthIndex])
                    } else {
                        //超出所有的字符结尾，设置flag = false表示结束
                        flag = false
                    }
                    //每添加完一个字符就进行和一行的总长度进行对比一下，如果没有超出那么进行增加记录的索引lengthIndex++,负责flag = false结束
                    if ((rectWidth.toInt() - 100) < (paint.measureText(
                            buffer.toString(),
                            0,
                            buffer.toString().length
                        ))
                    ) {
                        flag = false
                    } else {
                        lengthIndex++
                    }
                }
                //绘制每一行的字符
                canvas.drawText(
                    buffer.toString(),
                    -textWidth / 2,
                    60f + (rect.height() + 10) * ind,//根据文字的高度来绘制下一行文字的具体高度。这里10是行间距
                    paint
                )
            }

            canvas.restore()
            canvas.translate(rectWidth, 0f)
        }
        canvas.restore()
    }

    private fun getBottomRoundColor(state: Int): Int {
        return when (state) {
            0 -> {
                Color.argb(255, 200, 200, 200)
            }
            else -> {
                Color.argb(255, 139, 171, 158)
            }
        }
    }

    private fun getTopRoundColor(state: Int): Int {
        return when (state) {
            0 -> {
                Color.GREEN
            }
            1 -> {
                Color.BLUE
            }
            2 -> {
                Color.RED
            }
            3 -> {
                Color.YELLOW
            }
            else -> {
                Color.argb(255, 139, 171, 158)
            }
        }
    }

    private fun Paint.setShadowLayout(state: Int) {
        when (state) {
            0 -> {
                setShadowLayer(2f, 0f, 5f, Color.BLACK)
            }
            else -> {
                setShadowLayer(5f, 3f, -5f, Color.argb(155, 150, 150, 150))
            }
        }
    }

    var viewToY = 0f
    var startX = 0f
    private var clickDow = false
    override fun onTouchEvent(event: MotionEvent): Boolean {
        when (event.action) {
            MotionEvent.ACTION_UP -> {
                clickDow = false
                startX = 0f
            }
            MotionEvent.ACTION_MOVE -> {
                //每次通知计算滑动的一点点
                val dis = event.x - startX
                //记录这次移动结束的event.x就是下一次的滑动起始滑动的位置
                startX = event.x
                //将每次的滑动小段距离在当前距离的基础上叠加起来
                viewToY += dis
                invalidate()
            }
            MotionEvent.ACTION_DOWN -> {
                clickDow = true
                startX = event.x
                return true
            }

        }
        return true
    }
    data class StudyLanguage(val year: String, val description: String, val state: Int)
}