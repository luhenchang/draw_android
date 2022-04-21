package com.example.draw_android.section03_paint.a2_blend_modle

import android.content.Context
import android.util.AttributeSet
import android.view.View

import android.annotation.SuppressLint
import android.graphics.*
import android.graphics.PorterDuff.Mode.*
import android.renderscript.Matrix4f
import com.example.draw_android.R
import kotlin.math.max


/**
 * Created by wangfei44 on 2022/3/15.
 */
class PaintBlendView constructor(context: Context, attributeSet: AttributeSet) :
    View(context, attributeSet) {
    private val mPaint = Paint().apply {
        strokeWidth = 50f
        style = Paint.Style.STROKE
        color = Color.YELLOW
    }
    private var mSrcB: Bitmap = BitmapFactory.decodeResource(resources, R.drawable.longnv)

    @SuppressLint("DrawAllocation")
    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        //setMode(canvas)
        setStrokeJoin(canvas)

    }

    private fun setStrokeJoin(canvas: Canvas) {
        canvas.translate(0f,100f)
        mPaint.strokeJoin = Paint.Join.ROUND
        val path = Path().apply {
            moveTo(100f, 100f)
            lineTo(300f, 100f)
            lineTo(200f, 200f)
        }
        canvas.drawPath(path,mPaint)


        canvas.translate(0f,230f)
        mPaint.strokeJoin = Paint.Join.MITER
        canvas.drawPath(path,mPaint)

        canvas.translate(0f,230f)
        mPaint.strokeJoin = Paint.Join.BEVEL
        canvas.drawPath(path,mPaint)
    }

    private fun setMode(canvas: Canvas) {
        //禁止硬件加速
        setLayerType(LAYER_TYPE_SOFTWARE, null)
        //保存图层
        val saveId = canvas.saveLayer(0f, 0f, width.toFloat(), height.toFloat(), mPaint)

        //1.绘制源图像素---黄色圆圈
        canvas.drawCircle(width / 2f, 500f, 500f, mPaint)

        //2.设置PorterDuff
        mPaint.xfermode = PorterDuffXfermode(DST_OUT)

        //3.绘制目标图源像素--龙女
        canvas.drawBitmap(mSrcB, 0f, 0f, mPaint)

        //用完清除
        mPaint.xfermode = null
        //图层恢复
        canvas.restoreToCount(saveId)
    }

    /**
     * 是否抗锯齿的验证对比
     */
    private fun setAntiAlias(canvas: Canvas) {
        val paint = Paint()
        paint.color = Color.RED
        paint.strokeWidth = 133f
        paint.style = Paint.Style.STROKE

        //不抗锯齿
        paint.isAntiAlias = false
        canvas.translate((height / 2).toFloat(), 600f)
        canvas.drawCircle(0f, 0f, 250f, paint)

        //设置抗锯齿
        paint.isAntiAlias = true
        canvas.translate(700f, 0f)
        canvas.drawCircle(0f, 0f, 250f, paint)
    }

    private fun setShaderLayout(canvas: Canvas) {
        val paint= Paint()
        paint.color= Color.RED
        paint.strokeWidth=22f
        paint.style= Paint.Style.STROKE
        //设置阴影
        paint.setShadowLayer(10f,10f,0f,Color.BLUE)
        canvas.translate((height / 2).toFloat(), 300f)
        canvas.drawCircle(0f, 0f, 150f, paint)


        canvas.translate(500f,0f)
        paint.setShadowLayer(30f,30f,0f,Color.GREEN)
        canvas.drawCircle(0f, 0f, 150f, paint)


        canvas.translate(500f,0f)
        paint.setShadowLayer(30f,0f,30f,Color.BLACK)
        canvas.drawCircle(0f, 0f, 150f, paint)
    }
}