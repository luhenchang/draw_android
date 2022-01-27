package com.example.draw_android.section01_draw.a_view

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.util.AttributeSet
import android.view.View

/**
 * Created by luhenchang on 2022/1/27.
 */
class CanvasView constructor(context: Context, attributeSet: AttributeSet) :
    View(context, attributeSet) {
    private var mWidth: Int = 0//画布的宽
    private var mHeight: Int = 0//画布的高

    private var mRadius: Float = 100f//画布的半径

    //画笔
    private var paint: Paint = Paint().apply {
        color = Color.RED
        style = Paint.Style.FILL
    }

    init {
        //初始化内容后面会讲解,例如xml里面的属性内容等获取。
    }

    //后面讲解内容
    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec)
    }

    //后面讲解内容
    override fun onLayout(changed: Boolean, left: Int, top: Int, right: Int, bottom: Int) {
        super.onLayout(changed, left, top, right, bottom)
    }

    //视图大小改变时回调
    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
        super.onSizeChanged(w, h, oldw, oldh)
        mWidth = width
        mHeight = height
    }

    //canvas作为纸
    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        //绘制一个圆圈在画布中间
        canvas.drawCircle(
            mWidth / 2f,
            mHeight / 2f,
            mRadius,
            paint
        )
    }
}