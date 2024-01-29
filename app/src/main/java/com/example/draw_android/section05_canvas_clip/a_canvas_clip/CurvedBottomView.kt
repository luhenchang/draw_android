package com.example.draw_android.section05_canvas_clip.a_canvas_clip

import android.content.Context
import android.content.res.TypedArray
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.Path
import android.graphics.drawable.Drawable
import android.util.AttributeSet
import android.view.View
import com.example.draw_android.R
import kotlin.math.max


class CurvedBottomView : View {
    //背景可能的图片或者颜色
    private var backgroundDrawable: Drawable? = null

    //弧度控制点
    private var curvedControlY = 0

    //弧度的起始点
    private var curvedStartY = 0

    private var clipPath = Path()
    private lateinit var linePaint: Paint
    private lateinit var controlPaint: Paint


    constructor(context: Context?) : super(context) {
        init()
    }

    constructor(context: Context, attrs: AttributeSet) : super(context, attrs) {
        val array: TypedArray = context.obtainStyledAttributes(attrs, R.styleable.CurvedBottomView)
        //获取customBackground 属性的值【可能是图片可能是颜色】
        backgroundDrawable = array.getDrawable(R.styleable.CurvedBottomView_customBackground)
        //获取控制点距离控件顶部的像素
        curvedControlY =
            array.getDimensionPixelSize(R.styleable.CurvedBottomView_curvedControllerY, 0)
        //获取曲线起始点距离控件顶部的像素
        curvedStartY = array.getDimensionPixelSize(R.styleable.CurvedBottomView_curvedStartY, 0)
        // 使用其他自定义属性的获取方式
        array.recycle() // 注意回收TypedArray
        init()
    }

    constructor(context: Context?, attrs: AttributeSet?, defStyleAttr: Int) : super(
        context,
        attrs,
        defStyleAttr
    ) {
        init()
    }

    private fun init() {
        linePaint = Paint().apply {
            style = Paint.Style.STROKE
            strokeWidth = 2f
            color = Color.RED
        }
        controlPaint = Paint().apply {
            style = Paint.Style.FILL
            strokeWidth = 2f
            color = Color.BLUE
        }
        clipPath = Path()
    }

    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
        super.onSizeChanged(w, h, oldw, oldh)
        //保证起始点不能小于0
        curvedStartY = max(0, curvedStartY)
        //这里进行约束控制点的高度，保证弧度不超过底部
        curvedControlY = if (curvedControlY >= height + (height - curvedStartY)) {
            height + (height - curvedStartY)
        } else curvedControlY
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        //1、裁剪区域
        clipPath.reset()
        clipPath.moveTo(0f, 0f)
        clipPath.lineTo(width.toFloat(), 0f)
        clipPath.lineTo(width.toFloat(), curvedStartY.toFloat())
        clipPath.quadTo(width / 2f, curvedControlY.toFloat(), 0f, curvedStartY.toFloat())
        clipPath.close()
        //2、裁剪可用区域
        canvas.clipPath(clipPath)
        //3、在裁剪区域绘制背景
        if (backgroundDrawable != null) {
            backgroundDrawable?.setBounds(0, 0, width, height)
            backgroundDrawable?.draw(canvas)
        }
    }

}

