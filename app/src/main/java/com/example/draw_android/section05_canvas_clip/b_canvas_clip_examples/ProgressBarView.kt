package com.example.draw_android.section05_canvas_clip.b_canvas_clip_examples

import android.animation.ValueAnimator
import android.content.Context
import android.content.res.TypedArray
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.LinearGradient
import android.graphics.Paint
import android.graphics.Paint.Cap
import android.graphics.Path
import android.graphics.Rect
import android.graphics.Shader
import android.graphics.Typeface
import android.util.AttributeSet
import android.view.View
import android.view.animation.DecelerateInterpolator
import com.example.draw_android.R
import kotlin.math.min


class ProgressBarView : View {
    //这个用于路线部分的轻微调整的距离
    private val marginOut: Int = 2

    //文字框的高度可以作为变量，提供属性给开发着使用
    private var textRectHeight: Float = 60f
    //进度百分比范围0f-1f
    private var progressScale: Float = 0f
    //bitmap可有可无，可以使用background设置。
    private lateinit var bitmap: Bitmap
    //进度渠上边阴影路径
    private val shadowTopPath = Path()
    //进度渠下边缘阴影路径
    private val shadowBottomPath = Path()
    //覆盖到边缘上层，消除接触部分的差异
    private val overShadowPath = Path()
    //文字框区域
    private val textRectPath = Path()

    //控件自定义左右margin，大家可以获取自身margin做为变量
    private var margin: Float = 250f
    //进度条的高度，最好设置为属性对外提供方法等，由于进度我就不做分装属性了
    private var progressBarHeight: Float = 80f
    //进度渠背景绘制的画笔
    private var progressBgDitchPaint = Paint()
    //进度渠上边缘的阴影部分的画笔
    private var progressTopShadowOutPaint = Paint()
    //进度渠下边缘的阴影部分的画笔
    private var progressBottomShadowOutPaint = Paint()
    //覆盖上下边缘由于交界处的不连贯，进行覆盖。
    private val progressOutPathPaint = Paint()
    //进度条的画笔
    private val progressPaint = Paint()
    //进度条下面的阴影paint
    private val progressShadowPaint = Paint()
    //文字框背景颜色画笔
    private val textRectBgPaint = Paint()
    //文字框阴影画笔
    private val textRectShadowPaint = Paint()
    //文字画笔
    private val textPaint = Paint(Paint.ANTI_ALIAS_FLAG)



    constructor(context: Context) : super(context) {
        init(null)
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
    //设置动画，由动画控制其进度条。比较丝毫
    fun setProgressScale(progressScale: Float) {
        startAnimal(minOf(progressScale,1f))
    }

    private var animator: ValueAnimator? = null

    private fun startAnimal(starAnimal: Float) {
        animator?.cancel()
        // 设置弹性动画
        animator = ValueAnimator.ofFloat(
            0f,
            starAnimal
        )
        animator?.apply {
            duration = 1000 //时间1秒，当然你可以设置一个属性提供给开发着属性变量
            interpolator = DecelerateInterpolator()
            addUpdateListener { animation ->
                progressScale = animation.animatedValue as Float
                invalidate()
            }
            start()
        }
    }

    private fun init(attrs: AttributeSet?) {
        if (attrs != null) {
            val array: TypedArray =
                context.obtainStyledAttributes(attrs, R.styleable.ProgressBarView)
            //获取customBackground 属性的值【可能是图片可能是颜色】
            progressScale = array.getFloat(R.styleable.ProgressBarView_progressScale, 0f)
            array.recycle()
        }
        bitmap = BitmapFactory.decodeResource(resources, R.drawable.img_jinshu)

        progressTopShadowOutPaint.apply {
            color = Color.BLACK
            style = Paint.Style.STROKE
            setShadowLayer(
                6f,
                0f,
                6f,
                Color.BLACK
            )
            strokeWidth = 0f
            strokeCap = Cap.ROUND
            isAntiAlias = true
        }
        progressBottomShadowOutPaint.apply {
            color = Color.WHITE
            style = Paint.Style.STROKE
            setShadowLayer(
                6f,
                0f,
                -6f,
                Color.WHITE
            )
            strokeWidth = 3f
            strokeCap = Cap.ROUND
            isAntiAlias = true
        }

        progressBgDitchPaint.apply {
            color = Color.parseColor("#B7B6B4")
            style = Paint.Style.FILL
            strokeWidth = progressBarHeight
            strokeCap = Cap.ROUND
            isAntiAlias = true
        }

        progressPaint.apply {
            color = Color.parseColor("#A9B88F")
            style = Paint.Style.STROKE
            shader = LinearGradient(
                0f, -progressBarHeight / 2f, 0f,
                progressBarHeight / 2f,
                intArrayOf(
                    Color.parseColor("#E2EDD2"),
                    Color.parseColor("#A2B289"),
                    Color.parseColor("#7E8E6A")
                ),
                floatArrayOf(0f, 0.4f, 0.8f),
                Shader.TileMode.CLAMP
            )
            strokeWidth = progressBarHeight - 6
            strokeCap = Cap.ROUND
            isAntiAlias = true
        }

        progressShadowPaint.apply {
            color = Color.parseColor("#A9B88F")
            style = Paint.Style.STROKE
            setShadowLayer(
                10f,
                0f,
                10f,
                Color.parseColor("#2B2B2B")
            )
            strokeWidth = progressBarHeight - 6
            strokeCap = Cap.ROUND
            isAntiAlias = true
        }

        progressOutPathPaint.apply {
            color = Color.parseColor("#B7B6B4")
            style = Paint.Style.STROKE
            strokeWidth = 3.5f
            strokeCap = Cap.ROUND
            isAntiAlias = true
        }
        textRectBgPaint.apply {
            color = Color.parseColor("#A9B88F")
            style = Paint.Style.FILL
            shader = LinearGradient(
                0f, 0f, 0f,
                textRectHeight,
                intArrayOf(
                    Color.parseColor("#F5F4F6"),
                    Color.parseColor("#D0CFD8"),
                    Color.parseColor("#B8B8C6")
                ),
                floatArrayOf(0f, 0.4f, 0.8f),
                Shader.TileMode.CLAMP
            )
            strokeWidth = progressBarHeight - 6
            strokeCap = Cap.ROUND
            isAntiAlias = true
        }
        textRectShadowPaint.apply {
            strokeWidth = 2f
            color = Color.parseColor("#B8B8C6")
            style = Paint.Style.STROKE
            setShadowLayer(
                6f,
                0f,
                6f,
                Color.BLACK
            )
        }
        textPaint.apply {
            textSize = 30f

            color = Color.parseColor("#514F50")
            style = Paint.Style.FILL
        }
        // 设置字体加粗
        textPaint.typeface = Typeface.create(Typeface.DEFAULT, Typeface.BOLD)
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        //1、将画布平移到控件中心
        canvas.translate(0f, height / 2f)
        initOverShadowLine()

        //2、绘制进度渠
        canvas.drawLine(
            margin,
            0f,
            width.toFloat() - margin,
            0f,
            progressBgDitchPaint
        )


        //3、绘制上边缘的阴影
        shadowTopPath.reset()
        shadowTopPath.apply {
            moveTo(margin - progressBarHeight / 2f, 0f)
            quadTo(
                margin - progressBarHeight / 2f + marginOut,//c1控制点的横坐标
                0f - progressBarHeight / 2f + marginOut,//c1控制点的纵坐标
                margin,//b点的横坐标
                0f - progressBarHeight / 2f//b点的纵坐标
            )
            lineTo(width.toFloat() - margin, 0f - progressBarHeight / 2f)
            //右侧部分，不明白最好画图。确定此时画布的位置。
            quadTo(
                width.toFloat() - margin + progressBarHeight / 2f - marginOut,
                0f - progressBarHeight / 2f + marginOut,
                width.toFloat() - margin + progressBarHeight / 2f,
                0f
            )
        }
        //绘制阴影
        canvas.drawPath(shadowTopPath, progressTopShadowOutPaint)


        //4、绘制下边缘的阴影
        shadowBottomPath.reset()
        shadowBottomPath.apply {
            moveTo(margin - progressBarHeight / 2f, 0f)
            quadTo(
                margin - progressBarHeight / 2f + marginOut,
                0f + progressBarHeight / 2f - marginOut,
                margin,
                0f + progressBarHeight / 2f//b点的纵坐标
            )
            lineTo(width.toFloat() - margin, 0f + progressBarHeight / 2f)
            //右侧部分，不明白最好画图。确定此时画布的位置。
            quadTo(
                width.toFloat() - margin + progressBarHeight / 2f - marginOut,
                0f + progressBarHeight / 2f - marginOut,
                width.toFloat() - margin + progressBarHeight / 2f,
                0f
            )
        }
        //绘制阴影
        canvas.drawPath(shadowBottomPath, progressBottomShadowOutPaint)

        //5、覆盖部分需要放在最底部，也就是上下边缘绘制的上层，达到覆盖消除交界问题。
        canvas.drawPath(overShadowPath, progressOutPathPaint)

        val totalProgressWidth = (width - margin * 2)
        val progressWidth = totalProgressWidth * min(progressScale, 1f)
        //6、绘制进度条阴影，让其在进度条下方，显得真实，你试一试和进度绘制调换位置。
        canvas.drawLine(
            margin - 2f,
            0f,
            margin - 2f + progressWidth,
            0f,
            progressShadowPaint
        )
        //7、绘制进度条。
        canvas.drawLine(
            margin - 2f,
            0f,
            margin - 2f + progressWidth,
            0f,
            progressPaint
        )


        //8、绘制文字部分。画图画图，除非你可以有很长时间的空间想象力和记忆里。
        textRectPath.reset()
        val textTopCenterX = margin - 2f + progressWidth
        val textRectWidthGeneral = 50f
        val textRectWidthMin = textRectWidthGeneral - 20f
        val textRectCornerHeight =  9f
        val textRectCornerControlHeight =  11f
        canvas.save()
        canvas.translate(0f, -textRectHeight*2.4f)

        textRectPath.apply {
            moveTo(textTopCenterX, 0f)
            //绘制右上角
            lineTo(textTopCenterX + textRectWidthMin, 0f)
            quadTo(textTopCenterX + textRectWidthGeneral, 0f, textTopCenterX + textRectWidthGeneral, 10f)
            //绘制右下角
            lineTo(textTopCenterX + textRectWidthGeneral - 2, textRectHeight - 20f)

            quadTo(
                textTopCenterX + textRectWidthGeneral - 4,
                textRectHeight,
                textTopCenterX + textRectWidthMin,
                textRectHeight
            )
            lineTo(textTopCenterX + textRectWidthMin, textRectHeight)
            lineTo(textTopCenterX + 10f, textRectHeight)

            quadTo(textTopCenterX+5, textRectHeight, textTopCenterX+2, textRectHeight+textRectCornerHeight)
            lineTo(textTopCenterX+2,textRectHeight+textRectCornerHeight)

            //这里是底部最高点
            quadTo(textTopCenterX,textRectHeight+textRectCornerControlHeight,textTopCenterX-2,textRectHeight+textRectCornerHeight)
            lineTo(textTopCenterX-2,textRectHeight+textRectCornerHeight)

            quadTo(textTopCenterX-5, textRectHeight, textTopCenterX - textRectCornerControlHeight, textRectHeight)
            lineTo(textTopCenterX - textRectWidthMin, textRectHeight)
            quadTo(
                textTopCenterX - textRectWidthGeneral,
                textRectHeight,
                textTopCenterX - textRectWidthGeneral,
                textRectHeight - 20f
            )
            lineTo(textTopCenterX - textRectWidthGeneral - 4, 10f)
            quadTo(textTopCenterX - textRectWidthGeneral - 2, 0f, textTopCenterX - textRectWidthMin, 0f)
            close()
        }


        canvas.save()
        //稍作调整画布位置，先绘制阴影部分。
        canvas.translate(0f,-2f)
        canvas.drawPath(textRectPath, textRectShadowPaint)
        //绘制文字框背景之前赶紧恢复画布，让其绘制区域可以覆盖住阴影部分的带有颜色的边缘线
        canvas.restore()
        canvas.drawPath(textRectPath, textRectBgPaint)
        //绘制进度文字
        val formattedNumber = "%.0f".format(progressScale*100)
        val textContent = "$formattedNumber%"
        val rectContent = Rect()
        textPaint.getTextBounds(
            textContent,
            0,
            textContent.length,
            rectContent
        )
        val textWidth =rectContent.width()
        val textHeight = rectContent.height()
        //画图，加减计算。没别的。
        canvas.drawText(textContent,textTopCenterX-textWidth/2f,textRectHeight/2f+textHeight/2f,textPaint)
    }

    /**
     * 初始化覆盖阴影的路径
     */
    private fun initOverShadowLine() {
        overShadowPath.reset()
        overShadowPath.apply {
            moveTo(margin - progressBarHeight / 2f, 0f)
            quadTo(
                margin - progressBarHeight / 2f + marginOut,//c1控制点的横坐标
                0f - progressBarHeight / 2f + marginOut,//c1控制点的纵坐标
                margin,//b点的横坐标
                0f - progressBarHeight / 2f//b点的纵坐标
            )
            lineTo(width.toFloat() - margin, 0f - progressBarHeight / 2f)
            //右侧部分，不明白最好画图。确定此时画布的位置。
            quadTo(
                width.toFloat() - margin + progressBarHeight / 2f - marginOut,
                0f - progressBarHeight / 2f + marginOut,
                width.toFloat() - margin + progressBarHeight / 2f,
                0f
            )

            //右侧部分，不明白最好画图。确定此时画布的位置。
            quadTo(
                width.toFloat() - margin + progressBarHeight / 2f - marginOut,
                0f + progressBarHeight / 2f - marginOut,
                width.toFloat() - margin,
                progressBarHeight / 2f
            )
            lineTo(width.toFloat() - margin, 0f + progressBarHeight / 2f)
            lineTo(margin, 0f + progressBarHeight / 2f)
            quadTo(
                margin - progressBarHeight / 2f + marginOut,//c1控制点的横坐标
                progressBarHeight / 2f - marginOut,//c1控制点的纵坐标
                margin - progressBarHeight / 2f,
                0f
            )
            close()
        }
    }
}