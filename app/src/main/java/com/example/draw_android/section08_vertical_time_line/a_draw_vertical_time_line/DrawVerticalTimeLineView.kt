package com.example.draw_android.section08_vertical_time_line.a_draw_vertical_time_line
/*app/src/main/java/com/example/draw_android/section08_vertical_time_line/a_draw_vertical_time_line/DrawVerticalTimeLineView.kt*/
import android.content.Context
import android.graphics.*
import android.util.AttributeSet
import android.view.View
import com.example.draw_android.R
import android.graphics.BitmapFactory

import android.graphics.Bitmap
import android.util.Log
import android.view.MotionEvent
import android.view.ScaleGestureDetector
import kotlin.math.ceil


/**
 * Created by wangfei44 on 2022/2/8.
 */
class DrawVerticalTimeLineView constructor(context: Context, attributeSet: AttributeSet) :
    View(context, attributeSet) {

    //1，首先创造数据
    private val dataList = arrayListOf(
        DrawData(
            "1995-1998",
            R.drawable.ic_yuel,
            "Hilt includes extensions for providing classes from other Jetpack libraries. Hilt currently supports the following Jetpack components"
        ),
        DrawData(
            "1998-2000",
            R.drawable.ic_yuel,
            "You must add the Hilt dependencies to take advantage of these integrations. For more information about adding dependencies, see Dependency injection with Hilt."
        ),
        DrawData(
            "2000-2003",
            R.drawable.ic_yuel,
            "Provide a ViewModel by annotating it with @HiltViewModel and using the @Inject annotation in the ViewModel object's constructor."
        ),
        DrawData(
            "2013-2003",
            R.drawable.ic_yuel,
            "All Hilt ViewModels are provided by the ViewModelComponent which follows the same lifecycle as a ViewModel, and as such, can survive configuration changes. To scope a dependency to a ViewModel use the @ViewModelScoped annotation."
        ),
        DrawData(
            "2003-2008",
            R.drawable.ic_yuel,
            "Add the following additional dependencies to your Gradle file"
        ),
        DrawData(
            "2008-2010",
            R.drawable.ic_yuel,
            "Add the following additional dependencies to your Gradle file. Note that in addition to the library, you need to include an additional annotation processor that works on top of the Hilt annotation processor"
        ),
        DrawData(
            "2010-2019",
            R.drawable.ic_yuel,
            "Note that in addition to the library, you need to include an additional"
        ),
        DrawData(
            "2019-2022",
            R.drawable.ic_yuel,
            "Note that in addition to the library, you need to include an additional Add the following additional dependencies"
        ),
        DrawData(
            "2022-2023",
            R.drawable.ic_yuel,
            "Then, have your Application class implement the Configuration.Provider interface, inject an instance of HiltWorkFactory, and pass it into the WorkManager configuration as follows"
        ),
        DrawData(
            "2023-2026",
            R.drawable.ic_yuel,
            "Inject a Worker using the @HiltWorker annotation in the class and @AssistedInject in the Worker object's constructor. You can use only @Singleton"
        ),
        DrawData(
            "2026-2029",
            R.drawable.ic_yuel,
            "Note: Because this customizes the WorkManager configuration, you also must remove the default initializer from the AndroidManifest.xml file as specified in the WorkManager docs"
        ),
        DrawData(
            "2029-2030",
            R.drawable.ic_yuel,
            "Content and code samples on this page are subject to the licenses described in the Content License"
        ),
        DrawData(
            "2030-2036",
            R.drawable.ic_yuel,
            "Java is a registered trademark of Oracle and/or its affiliates.Last updated 2021-10-27 UTC."
        ),
        DrawData(
            "2036-2039",
            R.drawable.ic_yuel,
            "One of the benefits of using dependency injection frameworks like Hilt is that it makes testing your code easier."
        ),
        DrawData(
            "2039-2042",
            R.drawable.ic_yuel,
            "Hilt isn't necessary for unit tests, since when testing a class that uses constructor injection, you don't need to use Hilt to instantiate that class. Instead, just as you would if the constructor weren't annotated:"
        ),
        DrawData(
            "2042-2047",
            R.drawable.ic_yuel,
            "you can directly call a class constructor by passing in fake or mock dependencies"
        ),
        DrawData(
            "2047-2049",
            R.drawable.ic_yuel,
            "For integration tests, Hilt injects dependencies as it would in your production code. "
        ),
        DrawData(
            "2049-2052",
            R.drawable.ic_yuel,
            "Testing with Hilt requires no maintenance because Hilt automatically generates a new set of components for each test"
        ),
        DrawData(
            "2052-2056",
            R.drawable.ic_yuel,
            "o use Hilt in your tests, include the hilt-android-testing dependency in your project:"
        ),
        DrawData(
            "2056-2090",
            R.drawable.ic_yuel,
            "You must annotate any UI test that uses Hilt with @HiltAndroidTest. This annotation is responsible for generating the Hilt components for each test."
        ),
    )

    private var bitmap: Bitmap? = null

    init {
        initScaleGestureDetector()
        bitmap = BitmapFactory.decodeResource(resources, R.drawable.ic_yuel)
    }
    private var mScaleGestureDetector: ScaleGestureDetector? = null
    private var preScale=1f //之前的伸缩值
    private var curScale =1f //当前的伸缩值
    private fun initScaleGestureDetector() {
        mScaleGestureDetector = ScaleGestureDetector(
            context,
            object : ScaleGestureDetector.SimpleOnScaleGestureListener() {
                override fun onScaleBegin(detector: ScaleGestureDetector): Boolean {
                    return true
                }

                override fun onScale(detector: ScaleGestureDetector): Boolean {
                    curScale = detector.scaleFactor * preScale //当前的伸缩值*之前的伸缩值 保持连续性
                    Log.e("ScaleGestureDetector", "onScale: " + detector.scaleFactor)
                    if (curScale > 3 || curScale < 0.3) {//当放大倍数大于5或者缩小倍数小于0.1倍 就不伸缩图片 返回true取消处理伸缩手势事件
                        preScale = curScale;
                        return true
                    }
                    preScale = curScale//保存上一次的伸缩值
                    invalidate()
                    return false
                }

                override fun onScaleEnd(detector: ScaleGestureDetector) {}
            })
    }
    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        drawTopView(canvas)
        drawListView(canvas)
    }

    //刻度圆圈半径
    private val scaleCircleRadius = 15f

    //距离顶部距离
    private val margin = 60f

    //默认最上面的线长
    private val topLineHeight = 100f

    //刻度圆圈画笔
    private val circlePaint = Paint().apply {
        color = Color.argb(255, 185, 155, 84)
        isAntiAlias = true
        style = Paint.Style.STROKE
        strokeWidth = 8f//圆环宽度为5像素
        isAntiAlias = true
    }

    //刻度坐标线画笔
    private val linePaint = Paint().apply {
        color = Color.GRAY
        isAntiAlias = true
        style = Paint.Style.STROKE
        strokeWidth = 5f//圆环宽度为5像素
        isAntiAlias = true
    }

    private fun drawTopView(canvas: Canvas) {
        //最上面绘制的圆和线
        canvas.translate(width / 2f, (margin*curScale+viewToTop))
        canvas.scale(curScale,curScale)
        canvas.drawCircle(0f, 0f, scaleCircleRadius, circlePaint)
        //这里大家应该回顾一下，基础课程里面，当我绘制一个圆且画笔设置了宽度，那圆圈的半径是 = r+strokeWidth/2
        canvas.drawLine(
            0f,
            scaleCircleRadius + circlePaint.strokeWidth / 2,
            0f,
            topLineHeight,
            linePaint
        )

        //Item部分的绘制
        //1，先在上面的基础上绘制圆圈
        canvas.translate(0f, topLineHeight + scaleCircleRadius + circlePaint.strokeWidth / 2)
        canvas.drawCircle(0f,0f, scaleCircleRadius, circlePaint)
    }


    //水平线固定宽度
    private val levelWidth = 300f

    //左侧大圆圈半径
    private val largeCircle = 60f

    //绘制文字背景筐
    private val rectPaint = Paint().apply {
        color = Color.argb(255, 185, 155, 84)
        isAntiAlias = true
        style = Paint.Style.FILL
        strokeWidth = 8f//圆环宽度为5像素
        isAntiAlias = true
    }


    //刻度坐标线画笔
    private val vLinePaint = Paint().apply {
        color = Color.argb(255, 185, 155, 84)
        isAntiAlias = true
        style = Paint.Style.STROKE
        strokeWidth = 5f//圆环宽度为5像素
        isAntiAlias = true
    }

    private val textPaint = Paint().apply {
        color = Color.argb(255, 185, 155, 84)
        isAntiAlias = true
        style = Paint.Style.FILL
        isAntiAlias = true
        textSize = 30f
    }
    private val contentPaint = Paint().apply {
        color = Color.argb(255, 185, 155, 84)
        isAntiAlias = true
        style = Paint.Style.FILL
        isAntiAlias = true
        textSize = 24f
    }
    private val titlePaint = Paint().apply {
        color = Color.argb(255, 185, 155, 84)
        isAntiAlias = true
        style = Paint.Style.FILL
        isAntiAlias = true
        strokeWidth = 34f
        textSize = 28f
    }
    private val textWhitePaint = Paint().apply {
        color = Color.WHITE
        isAntiAlias = true
        style = Paint.Style.FILL
        isAntiAlias = true
        textSize = 30f
    }
    private val paddingHeight = 8f

    //绘制内容的行之间上下间距
    private val rowHeightened = 10f

    //决定文字距离右边时间线的宽度
    private val marginLevelRight = 30f

    private fun drawListView(canvas: Canvas) {
        maxYInit = 0f
        for (index in 0 until dataList.size) {
            if (index % 2 == 0) {
                drawLeftItem(canvas, index)
            } else {
                drawRightItem(canvas, index)
            }

        }
    }

    private fun drawRightItem(canvas: Canvas, index: Int) {
        val startX = (scaleCircleRadius + circlePaint.strokeWidth / 2)
        canvas.drawLine(startX, 0f, levelWidth + startX, 0f, vLinePaint)
        //绘制左侧大圆圈
        canvas.drawCircle(
            (levelWidth + startX + largeCircle),
            0f,
            largeCircle,
            circlePaint
        )
        //region绘制文字部分---------1---------
        canvas.save()
        val rect = Rect()
        textPaint.getTextBounds(dataList[index].year, 0, dataList[index].year.length, rect)
        //-(levelWidth+rect.width())/2 （画图即水平线的长度-文字宽度）/2 -10基本就是文字开始绘制的位置。
        canvas.translate((levelWidth - rect.width()) / 2 + paddingHeight, -paddingHeight)
        canvas.drawRect(
            0f - paddingHeight,
            -paddingHeight * 2 - rect.height(),
            rect.width().toFloat() + paddingHeight * 2,
            -paddingHeight + paddingHeight,
            rectPaint
        )
        canvas.drawText(
            dataList[index].year,
            0f,
            -paddingHeight,
            textWhitePaint
        )
        canvas.restore()
        //endregion---------------------1-----------

        //region绘制圆形内部图片
        canvas.save()
        //将画布圆点移动在左边大圆的左上角，绘制内部图标
        canvas.scale(-1f, 1f)
        canvas.translate(-(levelWidth + largeCircle * 2 + startX), 0f)
        //小图标圆为大圆的0.7。可计算小圆的左边距离y轴的距离为leftX
        val leftX = (largeCircle * 2 - 0.7 * largeCircle * 2) / 2
        //小圆圈上边距离X轴的距离为topY
        val topY = largeCircle - leftX
        bitmap?.let {
            val src = Rect(0, 0, it.width, it.height)
            val dst =
                Rect(leftX.toInt(), -topY.toInt(), (largeCircle * 2 - leftX).toInt(), topY.toInt())
            canvas.drawBitmap(it, src, dst, circlePaint)
        }

        //恢复坐标系到刻度线的圆点中间
        canvas.restore()
        //endregion

        //region绘制文字部分内容
        canvas.save()
        val rectContent = Rect()
        textPaint.getTextBounds(
            dataList[index].content,
            0,
            dataList[index].content.length,
            rectContent
        )
        canvas.translate(-levelWidth, 0f)
        canvas.restore()
        //endregion
        //region 绘制title
        canvas.save()
        canvas.translate((levelWidth - titlePaint.measureText("Lorem Ipsum")) / 2, 0f)
        canvas.drawText(
            "Lorem Ipsum",
            0f,
            30f,//根据文字的高度来绘制下一行文字的具体高度。这里10是行间距
            titlePaint
        )
        canvas.restore()
        //endregion

        //region 绘制content内容 保存好状态，避免丢失当前坐标系，
        canvas.save()
        //坐标系向左边移动levelWidth距离
        canvas.translate(startX, 0f)
        val (rowHeight, rowCount) = drawItemContent(rectContent, index, rect, canvas)
        canvas.restore()
        //endregion

        //region 根据行数和行高绘制刻度圆圈下面的线
        canvas.save()
        canvas.drawLine(
            0f,
            scaleCircleRadius + 2.5f,
            0f,
            rowCount * rowHeight + 60f,
            linePaint
        )
        val translateY = rowCount * rowHeight + scaleCircleRadius + circlePaint.strokeWidth / 2 + 60f
        //这里进行绘制下一个圆形刻度
        canvas.translate(0f, translateY)
        canvas.drawCircle(0f, 0f, scaleCircleRadius, circlePaint)
        canvas.restore()
        //变换坐标到下一个圆形刻度位置处
        canvas.translate(0f, translateY)
        maxYInit += translateY

    }

    private fun drawLeftItem(canvas: Canvas, index: Int) {
        val startX = (scaleCircleRadius + circlePaint.strokeWidth / 2)
        canvas.drawLine(-startX, 0f, -levelWidth - startX, 0f, vLinePaint)
        //绘制左侧大圆圈
        canvas.drawCircle(
            -(levelWidth + startX + largeCircle),
            0f,
            largeCircle,
            circlePaint
        )
        //region绘制文字部分---------1---------
        canvas.save()
        val rect = Rect()
        textPaint.getTextBounds(dataList[index].year, 0, dataList[index].year.length, rect)
        //-(levelWidth+rect.width())/2 （画图即水平线的长度-文字宽度）/2 -10基本就是文字开始绘制的位置。
        canvas.translate(-(levelWidth + rect.width()) / 2 - paddingHeight, -paddingHeight)
        canvas.drawRect(
            0f - paddingHeight,
            -paddingHeight * 2 - rect.height(),
            rect.width().toFloat() + paddingHeight * 2,
            -paddingHeight + paddingHeight,
            rectPaint
        )
        canvas.drawText(
            dataList[0].year,
            0f,
            -paddingHeight,
            textWhitePaint
        )
        canvas.restore()
        //endregion---------------------1-----------

        //region绘制圆形内部图片
        canvas.save()
        //将画布圆点移动在左边大圆的左上角，绘制内部图标
        canvas.translate(-(levelWidth + largeCircle * 2 + startX), 0f)
        //小图标圆为大圆的0.7。可计算小圆的左边距离y轴的距离为leftX
        val leftX = (largeCircle * 2 - 0.7 * largeCircle * 2) / 2
        //小圆圈上边距离X轴的距离为topY
        val topY = largeCircle - leftX
        bitmap?.let {
            val src = Rect(0, 0, it.width, it.height)
            val dst =
                Rect(leftX.toInt(), -topY.toInt(), (largeCircle * 2 - leftX).toInt(), topY.toInt())
            canvas.drawBitmap(it, src, dst, circlePaint)
        }

        //恢复坐标系到刻度线的圆点中间
        canvas.restore()
        //endregion

        //region绘制文字部分内容
        canvas.save()
        val rectContent = Rect()
        textPaint.getTextBounds(
            dataList[index].content,
            0,
            dataList[index].content.length,
            rectContent
        )
        canvas.translate(-levelWidth, 0f)
        canvas.restore()
        //endregion
        //region 绘制title
        canvas.save()
        canvas.translate(-levelWidth, 0f)
        canvas.translate((levelWidth - titlePaint.measureText("Lorem Ipsum")) / 2, 0f)
        canvas.drawText(
            "Lorem Ipsum",
            0f,
            30f,//根据文字的高度来绘制下一行文字的具体高度。这里10是行间距
            titlePaint
        )
        canvas.restore()
        //endregion

        //region 绘制content内容 保存好状态，避免丢失当前坐标系，
        canvas.save()
        //坐标系向左边移动levelWidth距离
        canvas.translate(-levelWidth - startX, 0f)
        val (rowHeight, rowCount) = drawItemContent(rectContent, index, rect, canvas)
        canvas.restore()
        //endregion

        //region 根据行数和行高绘制刻度圆圈下面的线
        canvas.save()
        canvas.drawLine(
            0f,
            scaleCircleRadius + 2.5f,
            0f,
            rowCount * rowHeight + 60f,
            linePaint
        )
        val translateY =
            rowCount * rowHeight + scaleCircleRadius + circlePaint.strokeWidth / 2 + 60f
        //这里进行绘制下一个圆形刻度
        canvas.translate(0f, translateY)
        canvas.drawCircle(0f, 0f, scaleCircleRadius, circlePaint)
        canvas.restore()
        //变换坐标到下一个圆形刻度位置处
        canvas.translate(0f, translateY)
        maxYInit += translateY
    }

    private fun drawItemContent(
        rectContent: Rect,
        index: Int,
        rect: Rect,
        canvas: Canvas
    ): Pair<Float, Float> {
        var rowHeight = 0f
        val allWidth = rectContent.width()
        //我们规定每一行最大宽度为一个Item的宽度-40 = rectWidth - 40f
        var rowCount = ceil(allWidth / (levelWidth))
        var lengthIndex = 0
        //这里通过行数+2增加绘制的行数来保证测量绘制过程的万无一失
        for (ind in 0 until (rowCount.toInt() + 2)) {
            var flag = true
            //作为容器来储存每一行的字符
            val buffer = StringBuffer()
            while (flag) {
                //判断如果字符索引没有超出总共的索引那么继续向buffer里面添加字符
                if ((lengthIndex < dataList[index].content.length)) {
                    buffer.append(dataList[index].content[lengthIndex])
                } else {
                    //超出所有的字符结尾，设置flag = false表示结束
                    flag = false
                }
                //每添加完一个字符就进行和一行的总长度进行对比一下，如果没有超出那么进行增加记录的索引lengthIndex++,负责flag = false结束
                if ((levelWidth - marginLevelRight * 2) <= (contentPaint.measureText(
                        buffer.toString(),
                        0,
                        buffer.toString().length
                    ))
                ) {
                    lengthIndex++
                    flag = false
                } else {
                    lengthIndex++
                }
            }
            //这里的rowHeightpadding
            rowHeight = ((rect.height() + rowHeightened).toFloat())
            //绘制每一行的字符
            //这里的80是title的高度+margin边距
            canvas.drawText(
                buffer.toString(),
                marginLevelRight,
                60f + rowHeight * ind,//根据文字的高度来绘制下一行文字的具体高度。这里10是行间距
                contentPaint
            )

        }
        return Pair(rowHeight, rowCount)
    }

    var clickDow = false
    var startY = 0f
    var viewToTop = 0f
    private var maxYInit: Float = 0f

    override fun onTouchEvent(event: MotionEvent): Boolean {
        mScaleGestureDetector?.onTouchEvent(event)
        when (event.action) {
            MotionEvent.ACTION_UP -> {
                clickDow = false
                startY = 0f
            }
            MotionEvent.ACTION_MOVE -> {
                //每次通知计算滑动的一点点
                val dis = event.y - startY
                //记录这次移动结束的event.x就是下一次的滑动起始滑动的位置
                startY = event.y
                //将每次的滑动小段距离在当前距离的基础上叠加起来
                viewToTop += dis
                Log.e("viewToY","viewToY = $viewToTop")
                Log.e("viewToY","maxYInit = $maxYInit")
                val scrollBottomHeight = (maxYInit*curScale-(measuredHeight - margin*curScale-(topLineHeight + scaleCircleRadius + circlePaint.strokeWidth / 2)*curScale)+margin*curScale)
                if (viewToTop <=  -scrollBottomHeight){
                    viewToTop = -scrollBottomHeight
                }
                //向下滑动时候
                if (viewToTop>margin*curScale){
                    viewToTop = margin*curScale
                }
                if (viewToTop<=margin*curScale&&viewToTop>0){
                    viewToTop = 0f
                }
               
                invalidate()
            }
            MotionEvent.ACTION_DOWN -> {
                clickDow = true
                startY = event.y
                return true
            }

        }
        return true
    }

    data class DrawData(val year: String, val img: Int, val content: String)

}