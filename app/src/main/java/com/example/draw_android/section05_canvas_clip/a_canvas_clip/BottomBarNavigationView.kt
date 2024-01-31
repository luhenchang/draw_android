package com.example.draw_android.section05_canvas_clip.a_canvas_clip


import android.content.Context
import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.Path
import android.graphics.Rect
import android.graphics.RectF
import android.graphics.Region
import android.util.AttributeSet
import android.view.MotionEvent
import android.view.View
import androidx.core.graphics.drawable.toBitmap
import com.example.draw_android.R

class BottomBarNavigationView(context: Context, attrs: AttributeSet) : View(context, attrs) {
    //弧度部分口的大小控制变量
    private var cubicWidthMargin: Int = 20
    //图标的大小变量
    private var iconSize: Int = 60
    //底部颜色
    private var bgColor = Color.parseColor("#009688")
    private var controlPaint: Paint = Paint().apply {
        style = Paint.Style.FILL
        strokeWidth = 2f
        color = bgColor
    }

    //圆形区域的半径
    private var circleRadius = 50f
    //底部导航栏的所有图标集合
    private var icons: ArrayList<Bitmap> = ArrayList()

    init {
        // 获取自定义属性
        val typedArray = context.obtainStyledAttributes(attrs, R.styleable.BottomBarNavigationView)
        // 获取图标资源数组
        val iconResId = typedArray.getResourceId(R.styleable.BottomBarNavigationView_icons, 0)
        if (iconResId != 0) {
            val iconsArray = resources.obtainTypedArray(iconResId)
            for (i in 0 until  iconsArray.length()) {
                iconsArray.getDrawable(i)?.let {
                    icons.add(it.toBitmap())
                }
            }
            invalidate()
            iconsArray.recycle()
        }
        bgColor = typedArray.getColor(R.styleable.BottomBarNavigationView_bgColor,bgColor)
        iconSize = typedArray.getDimensionPixelSize(R.styleable.BottomBarNavigationView_iconSize,iconSize)
        cubicWidthMargin = typedArray.getDimensionPixelSize(R.styleable.BottomBarNavigationView_cubicMargin,cubicWidthMargin)
        circleRadius = typedArray.getDimensionPixelSize(R.styleable.BottomBarNavigationView_circleRadius,circleRadius.toInt()).toFloat()

        controlPaint.apply {
            style = Paint.Style.FILL
            strokeWidth = 2f
            color = bgColor
        }
        // 回收 TypedArray
        typedArray.recycle()
    }

    private val circlePathList: ArrayList<Path> = ArrayList()
    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
        super.onSizeChanged(w, h, oldw, oldh)
        val scaleWidth = width / icons.size
        val unit = scaleWidth / 8
        icons.forEachIndexed { index, bitmap ->
            val moveWidth = scaleWidth * index
            val path = Path().apply {
                addCircle(
                    unit * 4f + moveWidth,
                    (height - circleRadius + circleRadius) / 2f,//这里画图理解，因为在画布绘制时候进行了移动translate(0f,circleRadius)所以圆心Y的坐标得画图计算避免点击icon不精确问题
                    circleRadius,
                    Path.Direction.CW
                )
            }
            circlePathList.add(path)
        }

    }

    override fun onDraw(canvas: Canvas) {
        // 绘制画布的背景颜色
        drawSelfBackground(canvas)
        // 让子 View 继续绘制
        super.onDraw(canvas)
    }

    private var clickIndex = 0
    private fun drawSelfBackground(canvas: Canvas) {
        //裁剪之前保存画布状态，为了恢复这个时候的画布状态，用于绘制圆形区域
        canvas.save()
        canvas.translate(0f, circleRadius)
        val scaleWidth = (width / icons.size).toFloat()
        val moveWidth = scaleWidth * clickIndex
        val unit = scaleWidth / 8
        val cubicBottom = height / 2.5f
        val path = Path().apply {
            moveTo(0f, 0f)
            lineTo(unit + cubicWidthMargin + moveWidth, 0f)
            cubicTo(
                unit * 2f + cubicWidthMargin + moveWidth,
                0f,
                unit * 2.5f + cubicWidthMargin + moveWidth,
                cubicBottom,
                unit * 4f + moveWidth,
                cubicBottom
            )
            cubicTo(
                unit * 5.5f - cubicWidthMargin + moveWidth,
                cubicBottom,
                unit * 6f - cubicWidthMargin + moveWidth,
                0f,
                unit * 7f - cubicWidthMargin + moveWidth,
                0f
            )
            lineTo(width.toFloat(), 0f)
            lineTo(width.toFloat(), height.toFloat())
            lineTo(0f, height.toFloat())
            close()
        }
        canvas.clipPath(path)
        canvas.drawColor(bgColor)
        //恢复到开始画布状态，用于绘制圆圈
        canvas.restore()
        //保存好一个移动之前的画布，好在后面需要时候恢复。
        canvas.save()
        //画布坐标系向下移动圆圈半径的高度。绘制圆圈不超过空间顶部
        canvas.translate(0f, circleRadius)
        canvas.drawCircle(unit * 4f + moveWidth, 0f, circleRadius, controlPaint)

        icons.forEachIndexed { index, bitmap ->
            //如果绘制时候需要移动画布做处理，就用canvas.save和restore进行，
            //如果通过坐标点计算就没必要进行canvas.save()这里我们通过坐标点计算所以可以注释掉save和restore
            canvas.save()
            //不难计算吧。三个弧度中间横坐标，根据图进行计算，一点儿也不难。
            val bitmapX = unit * 4f + index * scaleWidth
            //源Rect，如下，我们需要所有像素点，所以根据bitmap的宽高进行获取
            val srcRect = Rect(0, 0, bitmap.width, bitmap.height)
            //计算绘制区域位置，计算icon绘制的圆心位置，通过自身宽高的一半进行计算其绘制区域。中心+-自身宽高的一半，
            //可以得出其rect的left,top,right,bottom。选中的绘制区域比较高。
            val dstRect = if (index == clickIndex) Rect(
                (bitmapX - iconSize / 2).toInt(),
                (-iconSize / 2),
                (bitmapX + iconSize / 2).toInt(),
                (iconSize / 2)
            ) else Rect(//非选中的位置区域是底部背景的中心。根据图进行计算很简单。
                (bitmapX - iconSize / 2).toInt(),
                ((height - circleRadius) / 2f - iconSize / 2).toInt(),
                (bitmapX + iconSize / 2).toInt(),
                ((height - circleRadius) / 2f + iconSize / 2).toInt()
            )
            canvas.drawBitmap(
                bitmap,
                srcRect,
                dstRect,
                controlPaint
            )
            canvas.restore()
        }

        //恢复画布状态，避免后面操作混乱
        canvas.restore()
    }

    override fun performClick(): Boolean {
        return super.performClick()
    }
    //标记是否按下
    private var pressDown = false
    override fun onTouchEvent(event: MotionEvent): Boolean {

        when (event.action) {
            MotionEvent.ACTION_DOWN -> {
                performClick()
                pressDown = true
            }

            MotionEvent.ACTION_UP -> {
                //在控制点附近范围内部,进行移动
                if (pressDown) {
                    circlePathList.forEachIndexed { index, path ->
                        val contain = path.containsPoint(event.x, event.y)
                        if (contain && clickIndex != index) {
                            clickIndex = index
                            invalidate()
                            return true
                        }
                    }
                }
                pressDown = false
            }
        }
        return true
    }
    //Kotlin的扩展函数，如果使用不多的，可以看看其定义，this即指path。
    private fun Path.containsPoint(x: Float, y: Float): Boolean {
        val bounds = RectF()
        this.computeBounds(bounds, true)
        val region = Region()
        region.setPath(
            this,
            Region(
                Rect(
                    bounds.left.toInt(),
                    bounds.top.toInt(),
                    bounds.right.toInt(),
                    bounds.bottom.toInt()
                )
            )
        )
        return region.contains(x.toInt(), y.toInt())
    }
}

