package com.example.draw_android.section05_canvas_clip.b_canvas_clip_examples

import android.animation.ValueAnimator
import android.content.Context
import android.content.res.TypedArray
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.Paint.Cap
import android.graphics.Path
import android.graphics.Rect
import android.graphics.Typeface
import android.util.AttributeSet
import android.util.Log
import android.view.View
import android.view.animation.DecelerateInterpolator
import android.view.animation.LinearInterpolator
import androidx.core.animation.addPauseListener
import com.example.draw_android.R
import kotlin.math.min


class ProgressBallView : View {
    private lateinit var srcRect: Rect
    private var frameIndex: Int = 0

    //这个用于路线部分的轻微调整的距离
    private val marginOut: Int = 2

    //文字框的高度可以作为变量，提供属性给开发着使用
    private var textRectHeight: Float = 60f

    //进度百分比范围0f-1f
    private var progressScale: Float = 0.5f

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
    private var progressBgAnimalDitchPaint = Paint()

    //进度渠上边缘的阴影部分的画笔
    private var progressTopShadowOutPaint = Paint()

    //进度渠下边缘的阴影部分的画笔
    private var progressBottomShadowOutPaint = Paint()

    //覆盖上下边缘由于交界处的不连贯，进行覆盖。
    private val progressOutPathPaint = Paint()





    //文字画笔
    private val textPaint = Paint(Paint.ANTI_ALIAS_FLAG)
    private val circlePaint = Paint()


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
        startAnimal(minOf(progressScale, 1f))
    }

    private var animator: ValueAnimator? = null
    private var animatorBall: ValueAnimator? = null

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
            addPauseListener {
                animatorBall?.cancel()
            }
            start()
        }

        animatorBall?.cancel()
        // 设置弹性动画
        animatorBall = ValueAnimator.ofInt(
            0,1
        )
        animatorBall?.apply {
            duration = (500* starAnimal).toLong()//时间1秒，当然你可以设置一个属性提供给开发着属性变量
            repeatCount = 1
            interpolator = LinearInterpolator()
            addUpdateListener { animation ->
                Log.e("frameIndex animation=", animation.toString())
                frameIndex++
                if (frameIndex>6){
                    frameIndex = 0
                }
                Log.e("frameIndex=", frameIndex.toString())
                invalidate()
            }
            start()
        }
    }

    private val ballBitmapArray = ArrayList<Bitmap>()
    private fun init(attrs: AttributeSet?) {
        ballBitmapArray.add(BitmapFactory.decodeResource(resources, R.drawable.ball_one))
        ballBitmapArray.add(BitmapFactory.decodeResource(resources, R.drawable.ball_two))
        ballBitmapArray.add(BitmapFactory.decodeResource(resources, R.drawable.ball_three))
        ballBitmapArray.add(BitmapFactory.decodeResource(resources, R.drawable.ball_four))
        ballBitmapArray.add(BitmapFactory.decodeResource(resources, R.drawable.ball_five))
        ballBitmapArray.add(BitmapFactory.decodeResource(resources, R.drawable.ball_six))
        ballBitmapArray.add(BitmapFactory.decodeResource(resources, R.drawable.ball_seven))
        if (attrs != null) {
            val array: TypedArray =
                context.obtainStyledAttributes(attrs, R.styleable.ProgressBallView)
            //获取customBackground 属性的值【可能是图片可能是颜色】
            progressScale = array.getFloat(R.styleable.ProgressBallView_progressBallScale, 0f)
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
            color = Color.parseColor("#DF0038")
            style = Paint.Style.FILL
            strokeWidth = progressBarHeight
            strokeCap = Cap.ROUND
            isAntiAlias = true
        }
        progressBgAnimalDitchPaint.apply {
            color = Color.parseColor("#DDB801")
            style = Paint.Style.FILL
            strokeWidth = progressBarHeight
            strokeCap = Cap.ROUND
            isAntiAlias = true
        }


        progressOutPathPaint.apply {
            color = Color.parseColor("#DF0038")
            style = Paint.Style.STROKE
            strokeWidth = 3.5f
            strokeCap = Cap.ROUND
            isAntiAlias = true
        }

        textPaint.apply {
            textSize = 30f

            color = Color.parseColor("#514F50")
            style = Paint.Style.FILL
        }
        // 设置字体加粗
        textPaint.typeface = Typeface.create(Typeface.DEFAULT, Typeface.BOLD)

        circlePaint.apply {
            strokeWidth = 2f
            color = Color.parseColor("#F7E814")
            style = Paint.Style.FILL
        }
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
        val totalProgressWidth = (width - margin * 2)
        val progressWidth = totalProgressWidth * min(progressScale, 1f)
        val textTopCenterX = margin - 2f + progressWidth

        //顶层动态水渠
        canvas.drawLine(
            margin,
            0f,
            textTopCenterX - marginOut,
            0f,
            progressBgAnimalDitchPaint
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


        //绘制球体在最顶部。
        srcRect =
            Rect(0, 0, ballBitmapArray[frameIndex].width, ballBitmapArray[frameIndex].height)
        val dstRect = Rect(
            (textTopCenterX - progressBarHeight / 2f).toInt(),
            (-progressBarHeight / 2f).toInt(),
            (textTopCenterX + progressBarHeight / 2f).toInt(),
            (progressBarHeight / 2f).toInt()
        )
        canvas.drawBitmap(ballBitmapArray[frameIndex], srcRect, dstRect, progressBgAnimalDitchPaint)
        canvas.drawCircle(textTopCenterX,-progressBarHeight*1f,8f,circlePaint)
        canvas.drawCircle(textTopCenterX,progressBarHeight*1f,8f,circlePaint)

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