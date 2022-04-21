package com.example.draw_android.section05_canvas.c_scale

import android.content.Context
import android.graphics.*
import android.util.AttributeSet
import android.view.View

/**
 * Created by wangfei44 on 2022/1/26.
 */
class CanvasScaleView constructor(context: Context, attributeSet: AttributeSet) :
    View(context, attributeSet) {
    private var mWidth = 0
    private var mHeight = 0
    val paint = Paint().apply {
        color = Color.BLACK
        strokeWidth = 5f
        style = Paint.Style.FILL
        isAntiAlias = true
    }
    val circlePaint = Paint().apply {
        color = Color.parseColor("#99E16E79")
        strokeWidth = 5f
        style = Paint.Style.FILL
        isAntiAlias = true
    }
    val circleScaledPaint = Paint().apply {
        color = Color.parseColor("#9960CFFF")
        strokeWidth = 5f
        style = Paint.Style.FILL
        isAntiAlias = true
    }

    init {

    }

    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
        super.onSizeChanged(w, h, oldw, oldh)
        //这里目前作为了解:
        //当View大小发生变化或者初始化首次也会调用。这个调用在onDraw之前所以只要测量绘制过程中没有
        //发生大小改变那么在onDraw里面直接拿width和height也可以用，所以有很多开发场景中直接
        //在onDraw里面使用 height 和 width 应该杜绝。
        mWidth = w
        mHeight = h
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        paint.apply {
            textSize = 24f
        }
        //canvas.drawText(arrayScale[0],20f,20f,paint)
        //drawXY(canvas)
        //canvasScale(canvas)
        //canvasScaleY(canvas)
        //canvasScaleX(canvas)
        //canvasScaleXY(canvas)
        //canvasScaleBurdenXY(canvas)
        //canvasScaleXBurdenY(canvas)
        //canvasScaleBurdenXBurdenY(canvas)
        canvasScaleDrawLine(canvas)
    }
    private val margin = 60f
    private val arrayScale = arrayOf("Mon","Tue","Wed","Thu","Fri","Sat","Sunday")
    private fun canvasScaleDrawLine(canvas: Canvas) {
        canvas.scale(1f,-1f)
        canvas.translate(0f,-mHeight.toFloat())
        canvas.translate(margin,margin)
        //1.绘制X轴
        canvas.drawLine(0f, 0f, mWidth.toFloat()-margin*2f, 0f, paint)
        val scaleWidth =( mWidth - margin*2 ) / 7f
        for (index in 0 until 8){
            canvas.drawLine(paint.strokeWidth/2f,0f,paint.strokeWidth/2f,10f,paint)
            canvas.translate(scaleWidth,0f)
        }
        canvas.translate(-8*scaleWidth,0f)
        canvas.scale(1f,-1f)
        //获取当前文字的高度，当文字大小即textSize设置完成，文字高度基本固定，
         //所以我们测量第一个字符串高度即可。
        val bounds = Rect()
        paint.getTextBounds(arrayScale[0],0,arrayScale[0].length,bounds)
        //当然这里的平移变换可以放在上面的translate。
        canvas.translate(0f,bounds.height().toFloat()*1.3f)
        for (index in arrayScale.indices){
           //文字的宽度
           val mTextWidth = paint.measureText(arrayScale[index])
           val mTextStartX = scaleWidth * index + (scaleWidth-mTextWidth)/2f
           canvas.drawText(arrayScale[index],mTextStartX,0f,paint)
        }
        canvas.save()
    }


    //图1
    private fun canvasScale(canvas: Canvas) {
        //1.在圆心处绘制一个半径为100像素的圆圈
        canvas.drawCircle(0f,0f,100f,circlePaint)
        //2.x轴和y轴缩放比例都是1，也就是不缩放。
        canvas.scale(1f,1f)
        //3.在坐标系200像素，200像素的地方绘制一个半径为30的圆，作为参考系。表示(200,200)表示正方向。
        canvas.drawCircle(200f,200f,30f,circleScaledPaint)
    }


    //图2
    private fun canvasScaleY(canvas: Canvas) {
        //1.在圆心处绘制一个半径为100像素的圆圈
        canvas.drawCircle(0f,0f,100f,circlePaint)
        //2.x轴比例是1，2轴放大2倍。
        canvas.scale(1f,2f)
        //3.在坐标系200像素，200像素的地方绘制一个半径为30的圆，作为参考系。表示(200,200)表示正方向。
        canvas.drawCircle(200f,200f,30f,circleScaledPaint)
    }


    //图3
    private fun canvasScaleX(canvas: Canvas) {
        //1.在圆心处绘制一个半径为100像素的圆圈
        canvas.drawCircle(0f,0f,100f,circlePaint)
        //2.y轴比例是1，X轴放大2倍。
        canvas.scale(2f,1f)
        //3.在坐标系200像素，200像素的地方绘制一个半径为30的圆，作为参考系。表示(200,200)表示正方向。
        canvas.drawCircle(200f,200f,30f,circleScaledPaint)
    }

    //图4
    private fun canvasScaleXY(canvas: Canvas) {
        //1.在圆心处绘制一个半径为100像素的圆圈
        canvas.drawCircle(0f,0f,100f,circlePaint)
        //2.x轴和y轴缩放比例都是2。
        canvas.scale(2f,2f)
        //3.在坐标系200像素，200像素的地方绘制一个半径为30的圆，作为参考系。表示(200,200)表示正方向。
        canvas.drawCircle(200f,200f,30f,circleScaledPaint)
    }

    //图5
    private fun canvasScaleBurdenXY(canvas: Canvas) {
        //1.在圆心处绘制一个半径为100像素的圆圈
        canvas.drawCircle(0f,0f,100f,circlePaint)
        //2.x轴-1，y轴缩放比例都是1。
        canvas.scale(-1f,1f)
        //3.在坐标系200像素，200像素的地方绘制一个半径为30的圆，作为参考系。表示(200,200)表示正方向。
        canvas.drawCircle(200f,200f,30f,circleScaledPaint)
    }

    //图6
    private fun canvasScaleXBurdenY(canvas: Canvas) {
        //1.在圆心处绘制一个半径为100像素的圆圈
        canvas.drawCircle(0f,0f,100f,circlePaint)
        //2.x轴和y轴缩放比例都是2。
        canvas.scale(1f,-1f)
        //3.在坐标系200像素，200像素的地方绘制一个半径为30的圆，作为参考系。表示(200,200)表示正方向。
        canvas.drawCircle(200f,200f,30f,circleScaledPaint)
    }

    //图7
    private fun canvasScaleBurdenXBurdenY(canvas: Canvas) {
        //1.在圆心处绘制一个半径为100像素的圆圈
        canvas.drawCircle(0f,0f,100f,circlePaint)
        //2.x轴和y轴缩放比例都是2。
        canvas.scale(-1f,-1f)
        //3.在坐标系200像素，200像素的地方绘制一个半径为30的圆，作为参考系。表示(200,200)表示正方向。
        canvas.drawCircle(200f,200f,30f,circleScaledPaint)
    }



    private fun drawXY(canvas: Canvas) {
        //1.将圆心平移到屏幕中心
        canvas.translate(mWidth / 2f, mHeight / 2f)
        //2.绘制X轴
        canvas.drawLine(-mWidth / 2f, 0f, mWidth / 2f, 0f, paint)
        //3.绘制X轴的箭头，可以想一想箭头起点为X轴的端点，结束点X都是偏左，Y上下一定的距离即可。
        canvas.drawLine(mWidth / 2f, 0f, mWidth / 2f-20,10f, paint)
        canvas.drawLine(mWidth / 2f, 0f, mWidth / 2f-20,-10f, paint)

        //3.绘制Y轴
        canvas.drawLine(0f, -mHeight / 2f, 0f, mHeight / 2f, paint)
        //2.绘制Y轴箭头
        canvas.drawLine(0f, -mHeight / 2f, 20f, -mHeight / 2f+20, paint)
        canvas.drawLine(0f, -mHeight / 2f, -20f, -mHeight / 2f+20, paint)
    }
}