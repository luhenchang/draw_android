package com.example.draw_android.section05_canvas_clip.b_canvas_clip_examples

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.LinearGradient
import android.graphics.Paint
import android.graphics.Path
import android.graphics.Rect
import android.graphics.Shader
import android.graphics.Typeface
import android.util.AttributeSet
import android.view.MotionEvent
import android.view.View
import android.widget.OverScroller
import android.widget.Scroller
import com.example.draw_android.R
import kotlin.math.min

class ScrollerToView : View {
    private var clipPath: Path = Path()
    private val textMarginX: Float = 20f
    private val poWidth: Float = 250f
    private var cubicCircle: Float = 0.5f
    private var bitmap: Bitmap? = null
    private var unitH: Float = 0f
    private val marginWidth: Float = 100f
    private val marginTopAndBottom: Float = 100f

    private var maxValue: Float = 1000f
    private var outPathLine = Paint()
    private var outPathShadowPaint = Paint()
    private var fillPathPaint = Paint()
    private var arrowheadYPaint = Paint()

    private var dataList = arrayListOf(50f, 360f, 150f, 500f, 161f, 250f, 30f, 200f, 130f, 300f)
    private var cubicPath = Path()
    private var cubicShadowPath = Path()
    private var cubicInnerPath = Path()

    //Y轴顶端的箭头路径
    private var arrowheadYPath = Path()
    private val scroller = Scroller(this.context,null)
    private val overScroller = OverScroller(this.context,null)

    //文字画笔
    private val textPaint = Paint(Paint.ANTI_ALIAS_FLAG)

    constructor(context: Context) : super(context) {
        init()
    }

    constructor(context: Context, attrs: AttributeSet?) : super(context, attrs) {
        init(attrs)
    }

    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int) : super(
        context,
        attrs,
        defStyleAttr
    ) {
        init(attrs)
    }
    fun setCubicCircle(cubicCircle:Float){
        this.cubicCircle = cubicCircle
        invalidate()
    }
    val srcRect = Rect()
    val dstRect = Rect()
    private fun init(attrs: AttributeSet? = null) {
        bitmap = BitmapFactory.decodeResource(resources, R.drawable.img_jinshu_svg)
        outPathLine.apply {
            color = Color.parseColor("#80FBBC")
            style = Paint.Style.STROKE
            strokeWidth = 6f
            strokeCap = Paint.Cap.ROUND
            isAntiAlias = true
        }
        outPathShadowPaint.apply {
            color = Color.BLACK
            style = Paint.Style.STROKE
            setShadowLayer(
                6f,
                0f,
                -6f,
                Color.BLACK
            )
            strokeWidth = 6f
            strokeCap = Paint.Cap.ROUND
            isAntiAlias = true
        }
        fillPathPaint.apply {
            color = Color.parseColor("#80FBBC")
            style = Paint.Style.FILL
            strokeWidth = 1f
            strokeCap = Paint.Cap.ROUND
            isAntiAlias = true
        }
        arrowheadYPaint.apply {
            color = Color.parseColor("#80FBBC")
            style = Paint.Style.FILL
            strokeWidth = 1f
            strokeCap = Paint.Cap.ROUND
            isAntiAlias = true
        }
        textPaint.apply {
            textSize = 30f
            setShadowLayer(
                6f,
                0f,
                6f,
                Color.BLACK
            )
            color = Color.parseColor("#80FBBC")
            style = Paint.Style.FILL
        }
        // 设置字体加粗
        textPaint.typeface = Typeface.create(Typeface.DEFAULT, Typeface.BOLD)
        maxValue = dataList.max()

    }


    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
        super.onSizeChanged(w, h, oldw, oldh)
        unitH = (height - marginTopAndBottom * 2) / dataList.max()
        bitmap?.apply {
            srcRect.apply {
                left = 0
                top = 0
                right = width
                bottom = height
            }
        }
        dstRect.apply {
            left = 0
            top = 0
            right = (dataList.size-1)*poWidth.toInt()+marginWidth.toInt()
            bottom = height
        }
        fillPathPaint.apply {
            shader = LinearGradient(
                0f, 0f, 0f,
                dataList.max() * unitH,
                intArrayOf(
                    Color.parseColor("#3368C891"),
                    Color.parseColor("#8368C891"),
                    Color.parseColor("#68C891")
                ),
                floatArrayOf(0f, 0.4f, 0.8f),
                Shader.TileMode.CLAMP
            )
        }
        arrowheadYPaint.apply {
            shader = LinearGradient(
                0f, dataList.max() * unitH, 0f,
                dataList.max() * unitH + 15,
                intArrayOf(
                    Color.parseColor("#3368C891"),
                    Color.parseColor("#8368C891"),
                    Color.parseColor("#68C891")
                ),
                floatArrayOf(0f, 0.4f, 0.8f),
                Shader.TileMode.CLAMP
            )
        }
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        canvas.drawBitmap(bitmap!!, srcRect, dstRect, fillPathPaint)
        canvas.save()
        //在坐标变换之前进行裁剪，避免在移动中跟随画布移动
        clipPath.apply {
            reset()
            moveTo(marginWidth+outPathLine.strokeWidth, marginTopAndBottom-outPathLine.strokeWidth)
            lineTo(marginWidth+outPathLine.strokeWidth, height.toFloat())
            lineTo((dataList.size-1) * poWidth + poWidth,height.toFloat())
            lineTo((dataList.size-1) * poWidth + poWidth,marginTopAndBottom-outPathLine.strokeWidth)
            close()
        }
        //裁剪画布
        canvas.clipPath(clipPath)

        cubicPath.reset()
        cubicPath.moveTo(0f, dataList[0])
        cubicPath.lineTo(0f, dataList[0])
        cubicShadowPath.reset()
        cubicShadowPath.moveTo(0f, dataList[0])
        cubicShadowPath.lineTo(0f, dataList[0])
        cubicInnerPath.reset()
        cubicInnerPath.moveTo(0f, dataList[0])
        cubicInnerPath.lineTo(0f, dataList[0])
        //坐标变换到左下角为坐标原点，且右上方为正方向
        canvas.save()
        canvas.translate(marginWidth, height - marginTopAndBottom)
        canvas.scale(1f, -1f)
        //裁剪
        //计算每一个上坡或者下坡的水平宽度，水平单位长度都应该是固定的。例如总共有5条数据，会分为4条上下坡，画图试一试。
        val scaleWidth = poWidth//(width - marginWidth*2) / (dataList.size - 1)
        //我们将每一个上或者下坡段分为4等份，四分之一处是坡段起点到控制点C1控制点的距离。四分之三处是坡段起点到C2控制点的距离。
        val circleScale = min(cubicCircle, 1f)
        //遍历计算上和下坡路径
        dataList.forEachIndexed { index, value ->
            if (index + 1 > dataList.size - 1) {
                return@forEachIndexed
            }
            //上坡和下坡部分
            cubicPath.cubicTo(
                index * scaleWidth + scaleWidth * circleScale,//1
                value * unitH,
                index * scaleWidth + scaleWidth * (1 - circleScale),//3
                dataList[index + 1] * unitH,
                index * scaleWidth + scaleWidth,
                dataList[index + 1] * unitH
            )
            cubicInnerPath.cubicTo(
                index * scaleWidth + scaleWidth * circleScale,//1
                value * unitH,
                index * scaleWidth + scaleWidth * (1 - circleScale),//3
                dataList[index + 1] * unitH,
                index * scaleWidth + scaleWidth,
                dataList[index + 1] * unitH
            )
            cubicShadowPath.cubicTo(
                index * scaleWidth + scaleWidth * circleScale,//1
                value * unitH,
                index * scaleWidth + scaleWidth * (1 - circleScale),//3
                dataList[index + 1] * unitH,
                index * scaleWidth + scaleWidth,
                dataList[index + 1] * unitH
            )
        }
        cubicInnerPath.lineTo(scaleWidth * (dataList.size - 1), 0f)
        cubicInnerPath.lineTo(0f, 0f)
        cubicInnerPath.lineTo(0f, dataList[0])
        cubicInnerPath.close()
        canvas.drawPath(cubicInnerPath, fillPathPaint)
        canvas.drawPath(cubicShadowPath, outPathShadowPaint)
        canvas.drawPath(cubicPath, outPathLine)


        //绘制X对称轴上面的刻度
        dataList.forEachIndexed { index, value ->
            canvas.save()
            canvas.translate(0f, outPathLine.strokeWidth / 2f)
            val centerX = index * scaleWidth + scaleWidth
            canvas.drawLine(centerX, 0f, centerX, 20f, outPathShadowPaint)
            canvas.drawLine(centerX, 0f, centerX, 20f, outPathLine)
            canvas.restore()
        }
        //绘制文字
        dataList.forEachIndexed { index, value ->
            canvas.save()
            canvas.scale(1f, -1f)
            val centerX = index * scaleWidth + scaleWidth
            val contextText = "${index + 1}月"
            val textRect = Rect()
            textPaint.getTextBounds(contextText, 0, contextText.length, textRect)
            val textWidth = textRect.width()
            val textHeight = textRect.height()
            canvas.drawText(
                contextText,
                centerX - textWidth / 2f,
                textHeight.toFloat() + textMarginX,
                textPaint
            )
            canvas.restore()
        }
        //恢复画布状态，避免移动过程导致对称轴和文字跟随变化
        canvas.restore()
        //恢复裁剪之前画布状态，避免Y轴看不见
        canvas.restore()
        canvas.save()
        canvas.translate(marginWidth, height - marginTopAndBottom)
        canvas.scale(1f, -1f)
        //绘制Y对称轴
        canvas.drawLine(0f, 0f, 0f, dataList.max() * unitH, outPathShadowPaint)
        canvas.drawLine(0f, 0f, 0f, dataList.max() * unitH, outPathLine)
        //Y轴顶部的箭头
        canvas.save()
        canvas.translate(0f, outPathLine.strokeWidth)
        arrowheadYPath.reset()
        arrowheadYPath.moveTo(-10f, dataList.max() * unitH - 15f)
        arrowheadYPath.lineTo(0f, dataList.max() * unitH)
        arrowheadYPath.lineTo(10f, dataList.max() * unitH - 15f)
        canvas.drawPath(arrowheadYPath, outPathShadowPaint)
        canvas.drawPath(arrowheadYPath, outPathLine)
        canvas.restore()
        //绘制X对称轴
        canvas.drawLine(0f, 0f, scaleWidth * (dataList.size - 1), 0f, outPathShadowPaint)
        canvas.drawLine(0f, 0f, scaleWidth * (dataList.size - 1), 0f, outPathLine)

    }

    override fun performClick(): Boolean {
        return super.performClick()
    }
    //距离Y轴的水平移动距离
    private var initScrollToViewWidth = 0f
    private var startX = 0f

    override fun onTouchEvent(event: MotionEvent): Boolean {
        when (event.action) {
            MotionEvent.ACTION_UP -> {
                startX = 0f
            }

            MotionEvent.ACTION_MOVE -> {
                //每次通知计算滑动的一点点
                val dis = event.x - startX
                //将每次的滑动小段距离在当前距离的基础上叠加起来
                if (initScrollToViewWidth + dis >= -(dataList.size - 1) * poWidth +(width-2*marginWidth) && initScrollToViewWidth + dis <= outPathLine.strokeWidth / 2f) {
                    initScrollToViewWidth += dis
                    //scrollTo((-initScrollToViewWidth).toInt(),0)
                    scrollBy(-dis.toInt(),0)
                }
                //前面手势课程讲的很清楚了，记录当前手势所在横坐标，在每次执行手势移动事件结束都需要更新移动的开始点，避免距离过大，滑动过快。
                startX = event.x
            }

            MotionEvent.ACTION_DOWN -> {
                performClick()
                startX = event.x
            }

        }
        return true
    }
}