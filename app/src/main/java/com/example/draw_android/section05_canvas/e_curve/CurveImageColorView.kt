package com.example.draw_android.section05_canvas.e_curve

import android.content.Context
import android.content.res.TypedArray
import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.ColorMatrix
import android.graphics.ColorMatrixColorFilter
import android.graphics.Paint
import android.graphics.Path
import android.graphics.drawable.Drawable
import android.util.AttributeSet
import android.view.MotionEvent
import android.view.View
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.core.graphics.drawable.toBitmap
import com.example.draw_android.R

class CurveImageColorView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : View(context, attrs, defStyleAttr) {
    private var changeColor: Boolean = true
    private var defImg: Drawable?
    private var bitmap: Bitmap? = null
    private val mPaint = Paint()
    private var colorModeType = 0 //默认红色

    init {
        val array: TypedArray =
            context.obtainStyledAttributes(attrs, R.styleable.CurveImageColorView)
        //使用完之后，立马释放，避免内存泄漏
        defImg = array.getDrawable(R.styleable.CurveImageColorView_defImg).also { array.recycle() }
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        bitmap = defImg?.toBitmap(width, height, Bitmap.Config.ARGB_8888)
        //实例化一支画笔
        mPaint.strokeWidth = 10f
        //获得色相的计算公式
        //val degrees = 90f
        //实例化处理色相的颜色矩阵
        val colorMatrix = ColorMatrix()
        colorMatrix.set(
            floatArrayOf(
                0.33f, 0.59f, 0.11f, 0f, 0f,

                0.33f, 0.59f, 0.11f, 0f, 0f,

                0.33f, 0.59f, 0.11f, 0f, 0f,

                0f, 0f, 0f, 1f, 0f,
            )
        )
        //获得色相的计算公式
        val degrees = moveY * 1.0F / width * 180
        //0表示红色看源码
        //设置色调
        //colorMatrix.setRotate(colorModeType, degrees)
        //设置饱和度
        //colorMatrix.setSaturation(112f)
        //亮度
        //colorMatrix.setScale(1f, 0.6f, 0.7f, 1f)
        //将调好的颜色设置给画笔
        if (changeColor) {
            mPaint.colorFilter = ColorMatrixColorFilter(colorMatrix)
        } else {
            mPaint.colorFilter = null
        }
        //然后我们用调整好的颜色画笔将原来的图片bmp画到新的bitmap上
        bitmap?.run {
            canvas.drawBitmap(this, 0f, 0f, mPaint)
        }

        drawGrid(canvas)
        //直线和圆圈绘制
        drawLineAndCubit(canvas)
    }

    private fun drawGrid(canvas: Canvas) {
        //1.我们左下角为屏幕的圆点进行操作
        canvas.translate(0f, height.toFloat())
        canvas.scale(1f, -1f)
        canvas.save()

        val xpath = Path()
        xpath.moveTo(0f, 0f)
        xpath.lineTo(width.toFloat(), 0f)

        val paint = Paint()
        paint.color = Color.GRAY
        paint.strokeWidth = 2f
        paint.style = Paint.Style.STROKE

        val scaleWidth = width / 6
        for (index in 0 until 6) {
            canvas.translate(0f, scaleWidth.toFloat())
            canvas.drawPath(xpath, paint)
        }
        canvas.restore()

        val path = Path()
        path.moveTo(0f, 0f)
        path.lineTo(0f, width.toFloat())

        val paintY = Paint()
        paintY.color = Color.argb(200, 225, 225, 225)
        paintY.strokeWidth = 2f
        paintY.style = Paint.Style.STROKE

        canvas.save()
        canvas.drawPath(path, paint)
        for (index in 0 until 6) {
            canvas.translate(scaleWidth.toFloat(), 0f)
            canvas.drawPath(path, paint)
        }
        canvas.restore()
    }

    private var moveX = 300f
    private var moveY = 127f
    private fun drawLineAndCubit(canvas: Canvas) {
        val paint = Paint()
        paint.color = Color.GRAY
        paint.strokeWidth = 2f
        paint.style = Paint.Style.STROKE
        //斜线段
        val xpath = Path()
        xpath.moveTo(10f, 10f)
        xpath.lineTo(width.toFloat(), width.toFloat() - 120)
        canvas.drawPath(xpath, paint)
        //起点和终点圆圈
        paint.style = Paint.Style.FILL
        paint.color = Color.RED
        canvas.drawCircle(15f, 15f, 15f, paint)
        canvas.drawCircle(width.toFloat() - 15, width.toFloat() - 120 - 15, 15f, paint)
        val cubicPath = Path()
        cubicPath.moveTo(0f, 0f)
        paint.style = Paint.Style.STROKE
        paint.strokeWidth = 5f

        cubicPath.quadTo(moveX, moveY, width.toFloat() - 15, width.toFloat() - 120 - 15)
        //绘制曲线
        canvas.drawPath(cubicPath, paint)
    }

    override fun onTouchEvent(event: MotionEvent): Boolean {
        when (event.action) {
            MotionEvent.ACTION_DOWN,
            MotionEvent.ACTION_MOVE -> {
                //在控制点附近范围内部,进行移动
                moveX = event.x
                moveY = minOf(-(event.y - height), width.toFloat())
                invalidate()
            }
        }
        return true
    }

    fun setTypeMode(index: Int) {
        this.colorModeType = index
        invalidate()
    }
}


