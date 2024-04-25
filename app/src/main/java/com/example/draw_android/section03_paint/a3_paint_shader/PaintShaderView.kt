package com.example.draw_android.section03_paint.a3_paint_shader

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.BitmapShader
import android.graphics.BlendMode
import android.graphics.Canvas
import android.graphics.ComposeShader
import android.graphics.LinearGradient
import android.graphics.Paint
import android.graphics.PorterDuff
import android.graphics.RadialGradient
import android.graphics.RectF
import android.graphics.RuntimeShader
import android.graphics.Shader
import android.graphics.SweepGradient
import android.os.Build
import android.util.AttributeSet
import android.view.View
import androidx.annotation.RequiresApi
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import com.example.draw_android.R

class PaintShaderView : View {
    private lateinit var dtBitmap: Bitmap
    private lateinit var rectF: RectF
    private lateinit var rectFCenter: RectF
    val linePaint = Paint().apply {
        style = android.graphics.Paint.Style.FILL
        color = android.graphics.Color.WHITE
        strokeWidth = 5f
        isAntiAlias = true
    }
    val lineDiagonalPaint = Paint().apply {
        style = android.graphics.Paint.Style.FILL
        //渐变的起始点(0f,0f)结束点位置（400f,0f）
        shader = LinearGradient(
            0f,
            0f,
            400f,
            400f,
            Color.Red.toArgb(),
            Color.Blue.toArgb(),
            Shader.TileMode.CLAMP
        )
        color = android.graphics.Color.BLUE
        strokeWidth = 11f
        isAntiAlias = true
    }

    val lineMoreColorPaint = Paint().apply {
        style = android.graphics.Paint.Style.FILL
        //渐变的起始点(0f,0f)结束点位置（400f,0f）
        shader = LinearGradient(
            0f,
            0f,
            400f,
            400f,
            intArrayOf(Color.Red.toArgb(), Color.Yellow.toArgb(), Color.Green.toArgb()),
            floatArrayOf(0f, 0.5f, 1f),
            Shader.TileMode.CLAMP
        )
        color = android.graphics.Color.BLUE
        strokeWidth = 11f
        isAntiAlias = true
    }

    val radialMoreColorPaint = Paint().apply {
        style = android.graphics.Paint.Style.FILL
        //渐变的起始点(0f,0f)结束点位置（400f,0f）
        shader = RadialGradient(
            200f,
            200f,
            200f,
            intArrayOf(Color.Red.toArgb(), Color.Yellow.toArgb(), Color.Green.toArgb()),
            floatArrayOf(0f, 0.5f, 1f),
            Shader.TileMode.CLAMP
        )
        color = android.graphics.Color.BLUE
        strokeWidth = 11f
        isAntiAlias = true
    }


    val paint = Paint().apply {
        style = android.graphics.Paint.Style.STROKE
        //渐变的起始点(0f,0f)结束点位置（400f,0f）
        shader = LinearGradient(
            0f,
            0f,
            100f,
            0f,
            Color.Red.toArgb(),
            Color.Blue.toArgb(),
            Shader.TileMode.CLAMP
        )
        color = android.graphics.Color.BLUE
        strokeWidth = 11f
        isAntiAlias = true
    }

    val textPaint = Paint().apply {
        style = android.graphics.Paint.Style.FILL
        color = android.graphics.Color.BLACK
        textSize = 50f
        strokeWidth = 2f
        isAntiAlias = true
    }

    val circlePaint = Paint().apply {
        style = android.graphics.Paint.Style.STROKE
        color = android.graphics.Color.BLACK
        textSize = 30f
        strokeWidth = 2f
        isAntiAlias = true
    }

    @RequiresApi(Build.VERSION_CODES.S)
    val paintRadialGradient = Paint().apply {
        style = android.graphics.Paint.Style.FILL
        //渐变的起始点(0f,0f)结束点位置（400f,0f）
        shader = RadialGradient(
            0f,
            0f,
            100f,
            Color.Red.toArgb(),
            Color.Blue.toArgb(),
            Shader.TileMode.CLAMP
        )
        color = android.graphics.Color.BLACK
        strokeWidth = 11f
        isAntiAlias = true
    }

    private val sweepGradientPaint = Paint().apply {
        style = android.graphics.Paint.Style.FILL
        color = android.graphics.Color.BLACK
        strokeWidth = 11f
        isAntiAlias = true
    }

    constructor(context: Context) : super(context) {
        init()
    }


    constructor(context: Context, attrs: AttributeSet?) : super(context, attrs) {
        init()
    }

    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int) : super(
        context,
        attrs,
        defStyleAttr
    ) {
        init()
    }

    private fun init() {
        rectF = RectF(0f, 0f, 400f, 400f)
        dtBitmap = BitmapFactory.decodeResource(resources, R.drawable.img_1)
    }

    @RequiresApi(Build.VERSION_CODES.S)
    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        //1、线性着色器
        //paintLinearGradient(canvas)
        //2、放射渐变
        //paintRadialGradient(canvas)
        //3、扫描渐变
        //paintSweepGradient(canvas)
        //4、图片着色器
        //paintBitmapShader(canvas)
        //5、AGSL着色器
        //paintRunTimeShader(canvas)
        //6、合成着色器
        paintComposeShader(canvas)

    }

    private val composeShaderPaint = Paint().apply {
        isAntiAlias = true
    }

    private fun paintComposeShader(canvas: Canvas) {
        //创建一个图片着色器
        val bitmapShader =
            BitmapShader(dtBitmap, Shader.TileMode.REPEAT, Shader.TileMode.REPEAT)

        //创建一个径向渐变着色器
        val radialGradientShader = RadialGradient(
            canvas.width/2f,
            canvas.height/2f,
            1300f,
            Color.Red.toArgb(),
            Color.Green.toArgb(),
            Shader.TileMode.CLAMP
        )
        //采用混合模式->合成新的着色器。
        composeShaderPaint.shader = ComposeShader(bitmapShader, radialGradientShader, PorterDuff.Mode.ADD)
        canvas.drawPaint(composeShaderPaint)
    }


    private val COLOR_SHADER_SRC =
        """layout(color) uniform half4 iColor;
           half4 main(float2 fragCoord) {
              return iColor;
            }"""

    @RequiresApi(Build.VERSION_CODES.TIRAMISU)
    val fixedColorShader = RuntimeShader(COLOR_SHADER_SRC)

    @RequiresApi(Build.VERSION_CODES.TIRAMISU)
    private fun paintRunTimeShader(canvas: Canvas) {
        fixedColorShader.setColorUniform("iColor", Color.Green.toArgb())
        val paint = Paint()
        paint.shader = fixedColorShader
        canvas.let {
            canvas.drawPaint(paint) // fill the Canvas with the shader
        }
    }


    private val bitmapShaderPaint = Paint().apply {
        isAntiAlias = true
    }

    private fun paintBitmapShader(canvas: Canvas) {
        bitmapShaderPaint.shader =
            BitmapShader(dtBitmap, Shader.TileMode.CLAMP, Shader.TileMode.CLAMP)
        //canvas.drawPaint(paintBitmapShader)

        bitmapShaderPaint.shader =
            BitmapShader(dtBitmap, Shader.TileMode.REPEAT, Shader.TileMode.CLAMP)
        //canvas.drawPaint(paintBitmapShader)

        bitmapShaderPaint.shader =
            BitmapShader(dtBitmap, Shader.TileMode.REPEAT, Shader.TileMode.REPEAT)
        //canvas.drawPaint(paintBitmapShader)

        bitmapShaderPaint.shader =
            BitmapShader(dtBitmap, Shader.TileMode.MIRROR, Shader.TileMode.MIRROR)
        canvas.drawPaint(bitmapShaderPaint)
    }

    private fun paintSweepGradient(canvas: Canvas) {
        canvas.translate(900f, height / 2f)
        canvas.drawCircle(0f, 0f, 400f, sweepGradientPaint.apply {
            shader = SweepGradient(
                0f, 0f,
                Color.Red.toArgb(), Color.Blue.toArgb()
            )
        })
        sweepGradientPaint.reset()

        canvas.translate(1900f, 0f)
        canvas.drawCircle(0f, 0f, 400f, sweepGradientPaint.apply {
            shader = SweepGradient(
                0f, 0f,
                intArrayOf(Color.Red.toArgb(), Color.Blue.toArgb(), Color.Yellow.toArgb()),
                floatArrayOf(0.3f, 0.6f, 0.9f)
            )
        })
    }

    @RequiresApi(Build.VERSION_CODES.S)
    private fun paintRadialGradient(canvas: Canvas) {
        canvas.translate(900f, height / 2f)
        canvas.drawCircle(0f, 0f, 400f, paintRadialGradient.apply {
            shader = RadialGradient(
                0f,
                0f,
                100f,
                Color.Red.toArgb(),
                Color.Blue.toArgb(),
                Shader.TileMode.CLAMP
            )
        })
        canvas.drawCircle(0f, 0f, 410f, circlePaint)

        canvas.drawText("Shader.TileMode.CLAMP", -200f, 500f, textPaint)

        canvas.translate(900f, 0f)
        canvas.drawCircle(0f, 0f, 400f, paintRadialGradient.apply {
            shader = RadialGradient(
                0f,
                0f,
                100f,
                Color.Red.toArgb(),
                Color.Blue.toArgb(),
                Shader.TileMode.REPEAT
            )
        })
        canvas.drawCircle(0f, 0f, 410f, circlePaint)

        canvas.drawText("Shader.TileMode.REPEAT", -200f, 500f, textPaint)


        canvas.translate(900f, 0f)
        canvas.drawCircle(0f, 0f, 400f, paintRadialGradient.apply {
            shader = RadialGradient(
                0f,
                0f,
                100f,
                Color.Red.toArgb(),
                Color.Blue.toArgb(),
                Shader.TileMode.MIRROR
            )
        })
        canvas.drawCircle(0f, 0f, 410f, circlePaint)
        canvas.drawText("Shader.TileMode.MIRROR", -200f, 500f, textPaint)


        canvas.translate(900f, 0f)
        canvas.drawCircle(0f, 0f, 400f, paintRadialGradient.apply {
            shader = RadialGradient(
                0f,
                0f,
                100f,
                Color.Red.toArgb(),
                Color.Blue.toArgb(),
                Shader.TileMode.DECAL
            )
        })
        canvas.drawCircle(0f, 0f, 410f, circlePaint)
        canvas.drawText("Shader.TileMode.DECAL", -200f, 500f, textPaint)
    }

    private fun paintLinearGradient(canvas: Canvas) {
        canvas.drawRect(rectF, paint)
        //绘制一根数直线段
        //canvas.drawLine(50f, 0f, 50f, 400f, linePaint)
        //绘制一根数直线段
        canvas.drawLine(100f, 0f, 100f, 400f, linePaint)

        canvas.translate(0f, 500f)
        canvas.drawRect(rectF, lineDiagonalPaint)

        canvas.translate(0f, 500f)
        canvas.drawRect(rectF, lineMoreColorPaint)

        canvas.translate(0f, 500f)
        canvas.drawRect(rectF, radialMoreColorPaint)
    }
}