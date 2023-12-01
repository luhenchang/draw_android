package com.example.draw_android.section05_canvas.f_event

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.Rect
import android.graphics.RectF
import android.util.AttributeSet
import android.util.Log
import android.view.MotionEvent
import android.view.View
import android.widget.Toast
import com.example.draw_android.R

/**
 * 自定义View: Bitmap自定义裁剪工具
 */
class BitmapClippingView(context: Context?, attrs: AttributeSet?, defStyleAttr: Int) :
    View(context, attrs, defStyleAttr) {
    @JvmOverloads
    constructor(context: Context, attrs: AttributeSet? = null) : this(context, attrs, 0) {
        initBitmap(context)
    }

    private fun initBitmap(context: Context) {
        Thread {
            val originalBitmap =
                BitmapFactory.decodeResource(context.resources, R.drawable.phone_four)
            setBitmap(originalBitmap, 3, 5)
        }.start()
    }

    private var widthSize = 0f //布局宽度
    private var heightSize = 0f //布局高度
    private var bitmap: Bitmap? = null //要裁剪的位图
    private var bitmapWidth = 0f //位图原始宽度
    private var bimapHeight = 0f //位图原始高度
    private var proportionWidth = 0f //比例：宽  如裁图比例3:4，此处传3
    private var proportionHeight = 0f //比例：高  如裁图比例3:4，此处传4
    private var bitmapPaint: Paint? = null //图片画笔
    private var shadowPaint: Paint? = null //阴影画笔
    private var linePaint: Paint? = null //线条画笔
    var scaleStep = 0f //缩放比例
    private var initTag = true //用于判断是不是首次绘制
    private var leftLine = -1f //选区左线
    private var topLine = -1f //选区上线
    private var rightLine = -1f //选区右线
    private var bottomLine = -1f //选区下线
    private var focus = "NONE" //事件焦点
    private val LEFT_TOP = "LEFT_TOP" //LEFT_TOP:拖动左上角
    private val BODY = "BODY" //BODY：拖动整体
    private val RIGHT_BOTTOM = "RIGHT_BOTTOM" //RIGHT_BOTTOM:拖动右下角
    private val NONE = "NONE" //NONE:释放焦点
    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec)
        widthSize = MeasureSpec.getSize(widthMeasureSpec).toFloat()
        heightSize = MeasureSpec.getSize(heightMeasureSpec).toFloat()
        bitmapPaint = Paint()
        bitmapPaint!!.strokeWidth = 0f
        shadowPaint = Paint()
        shadowPaint!!.setColor(Color.parseColor("#57FF9800"))
        shadowPaint!!.strokeWidth = 4f
        shadowPaint!!.style = Paint.Style.FILL_AND_STROKE
        linePaint = Paint()
        linePaint!!.setColor(Color.parseColor("#FF9800"))
        linePaint!!.strokeWidth = 4f
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        if (bitmap == null) return

        //绘制参考背景背景
        scaleStep = widthSize / bitmapWidth
        val backgroundImgHeight = bimapHeight * scaleStep
        heightSize = backgroundImgHeight //把有效图像高度设置作为布局高度计算
        val rect = Rect(0, 0, bitmapWidth.toInt(), bimapHeight.toInt()) //裁剪图片中的部分(此处：全图)
        val rectF = RectF(0f, 0f, widthSize, backgroundImgHeight) //显示在屏幕中的什么位置
        canvas.drawBitmap(bitmap!!, rect, rectF, bitmapPaint)
        canvas.save()
        if (initTag) {
            //绘制初始状态的选框（最大选框）
            if (bitmapWidth > bimapHeight) {
                //宽大于高，取高
                val checkboxWidth = backgroundImgHeight / proportionHeight * proportionWidth //选框的宽
                leftLine = widthSize / 2f - checkboxWidth / 2f
                topLine = heightSize / 2f - backgroundImgHeight / 2f
                rightLine = widthSize / 2f + checkboxWidth / 2f
                bottomLine = heightSize / 2f + backgroundImgHeight / 2f
            } else {
                //高大于宽 取宽
                val checkboxWidth = widthSize //选框的宽
                val checkboxHeight = widthSize / proportionWidth * proportionHeight //选框的高
                leftLine = widthSize / 2f - checkboxWidth / 2f
                topLine = heightSize / 2f - checkboxHeight / 2f
                rightLine = widthSize / 2f + checkboxWidth / 2f
                bottomLine = heightSize / 2f + checkboxHeight / 2f
            }
            initTag = false
        }

        //绘制选择的区域
        //绘制周边阴影部分（分四个方块）
        linePaint!!.setColor(Color.parseColor("#FF9800"))
        linePaint!!.strokeWidth = 4f
        canvas.drawRect(0f, 0f, leftLine, heightSize, shadowPaint!!) //左
        canvas.drawRect(leftLine + 4, 0f, rightLine - 4, topLine, shadowPaint!!) //上
        canvas.drawRect(rightLine, 0f, widthSize, heightSize, shadowPaint!!) //右
        canvas.drawRect(leftLine + 4, bottomLine, rightLine - 4, heightSize, shadowPaint!!) //下

        //绘制选区边缘线
        canvas.drawLine(leftLine, topLine, rightLine, topLine, linePaint!!)
        canvas.drawLine(rightLine, topLine, rightLine, bottomLine, linePaint!!)
        canvas.drawLine(rightLine, bottomLine, leftLine, bottomLine, linePaint!!)
        canvas.drawLine(leftLine, bottomLine, leftLine, topLine, linePaint!!)

        //绘制左上和右下调节点
        linePaint!!.setColor(Color.RED)
        linePaint!!.strokeWidth = 6f
        canvas.drawLine(
            rightLine - 4,
            bottomLine - 4,
            rightLine - 4,
            bottomLine - 40 - 4,
            linePaint!!
        )
        canvas.drawLine(
            rightLine - 4,
            bottomLine - 4,
            rightLine - 40 - 4,
            bottomLine - 4,
            linePaint!!
        )
        canvas.drawLine(leftLine + 4, topLine + 4, leftLine + 40 + 4, topLine + 4, linePaint!!)
        canvas.drawLine(leftLine + 4, topLine + 4, leftLine + 4, topLine + 40 + 4, linePaint!!)

        //绘制焦点圆
        linePaint!!.strokeWidth = 2f
        linePaint!!.style = Paint.Style.STROKE
        canvas.drawCircle(rightLine - 4, bottomLine - 4, 80f, linePaint!!)
        canvas.drawCircle(leftLine + 4, topLine + 4, 80f, linePaint!!)

        //绘制扇形
        linePaint!!.setColor(Color.parseColor("#57FF0000"))
        linePaint!!.style = Paint.Style.FILL_AND_STROKE
        val mRectF =
            RectF(rightLine - 4 - 40, bottomLine - 4 - 40, rightLine - 4 + 40, bottomLine - 4 + 40)
        canvas.drawArc(mRectF, 270f, 270f, true, linePaint!!)
        linePaint!!.setColor(Color.parseColor("#57FF0000"))
        linePaint!!.style = Paint.Style.FILL_AND_STROKE
        val mRectF2 =
            RectF(leftLine + 4 - 40, topLine + 4 - 40, leftLine + 4 + 40, topLine + 4 + 40)
        canvas.drawArc(mRectF2, 90f, 270f, true, linePaint!!)
    }

    override fun onTouchEvent(event: MotionEvent): Boolean {
        if (leftLine == -1f) return false
        if (topLine == -1f) return false
        if (rightLine == -1f) return false
        if (bottomLine == -1f) return false
        if (bitmap == null) return false
        val touchX = event.x
        val touchY = event.y
        if (event.action == MotionEvent.ACTION_DOWN) {
            return actionDown(touchX, touchY)
        }
        if (event.action == MotionEvent.ACTION_MOVE) {
            return actionMove(touchX, touchY)
        }
        return if (event.action == MotionEvent.ACTION_UP) {
            actionUp(touchX, touchY)
        } else true
    }

    //抬起
    private fun actionUp(touchX: Float, touchY: Float): Boolean {
        Log.d("fxHou", "抬起X=$touchX   touchY=$touchY")
        Log.d("fxHou", "释放焦点")
        focus = NONE //释放焦点
        return true
    }

    //移动
    private fun actionMove(touchX: Float, touchY: Float): Boolean {
        Log.d("fxHou", "滑动X=$touchX   touchY=$touchY")
        if (focus == LEFT_TOP) {
            //移动边线
            leftLine = touchX
            topLine =
                bottomLine - (rightLine - leftLine) / proportionWidth * proportionHeight //约束比例

            //限制最小矩形 宽
            if (rightLine - leftLine < 100) {
                leftLine = rightLine - 100
                topLine = bottomLine - (rightLine - leftLine) / proportionWidth * proportionHeight
                //重绘
                postInvalidate()
                return true
            }

            //限制最小矩形 高
            if (bottomLine - topLine < 100) {
                topLine = bottomLine - 100
                leftLine = rightLine - (bottomLine - topLine) / proportionHeight * proportionWidth
                //重绘
                postInvalidate()
                return true
            }

            //防止超出边界
            if (leftLine < 0) {
                leftLine = 0f
                topLine = bottomLine - (rightLine - leftLine) / proportionWidth * proportionHeight
            }

            //防止超出边界
            if (topLine < 0) {
                topLine = 0f
                leftLine = rightLine - (bottomLine - topLine) / proportionHeight * proportionWidth
            }

            //重绘
            postInvalidate()
            return true
        } else if (focus == RIGHT_BOTTOM) {
            //移动边线
            rightLine = touchX
            bottomLine =
                topLine + (rightLine - leftLine) / proportionWidth * proportionHeight //约束比例

            //限制最小矩形 宽
            if (rightLine - leftLine < 100) {
                rightLine = leftLine + 100
                bottomLine = topLine + (rightLine - leftLine) / proportionWidth * proportionHeight
                //重绘
                postInvalidate()
                return true
            }

            //限制最小矩形 高
            if (bottomLine - topLine < 100) {
                bottomLine = topLine + 100
                rightLine = leftLine + (bottomLine - topLine) / proportionHeight * proportionWidth
                //重绘
                postInvalidate()
                return true
            }

            //防止超出边界
            if (rightLine > widthSize) {
                rightLine = widthSize
                bottomLine = topLine + (rightLine - leftLine) / proportionWidth * proportionHeight
            }

            //防止超出边界
            if (bottomLine > heightSize) {
                bottomLine = heightSize
                rightLine = leftLine + (bottomLine - topLine) / proportionHeight * proportionWidth
            }
            //重绘
            postInvalidate()
            return true
        } else if (focus == BODY) {
            val moveX = touchX - downX
            val moveY = touchY - downY
            leftLine = downLeftLine + moveX
            rightLine = downRightLine + moveX
            topLine = downTopLine + moveY
            bottomLine = downBottomLine + moveY
            if (leftLine < 0) {
                rightLine = rightLine - leftLine
                leftLine = 0f
                if (topLine < 0) {
                    bottomLine = bottomLine - topLine
                    topLine = 0f
                    //重绘
                    postInvalidate()
                    return true
                }
                if (bottomLine > heightSize) {
                    topLine = heightSize - (bottomLine - topLine)
                    bottomLine = heightSize
                    //重绘
                    postInvalidate()
                    return true
                }

                //重绘
                postInvalidate()
                return true
            }
            if (rightLine > widthSize) {
                leftLine = widthSize - (rightLine - leftLine)
                rightLine = widthSize
                if (topLine < 0) {
                    bottomLine = bottomLine - topLine
                    topLine = 0f
                    //重绘
                    postInvalidate()
                    return true
                }
                if (bottomLine > heightSize) {
                    topLine = heightSize - (bottomLine - topLine)
                    bottomLine = heightSize
                    //重绘
                    postInvalidate()
                    return true
                }

                //重绘
                postInvalidate()
                return true
            }
            if (topLine < 0) {
                bottomLine = bottomLine - topLine
                topLine = 0f
                //重绘
                postInvalidate()
                return true
            }
            if (bottomLine > heightSize) {
                topLine = heightSize - (bottomLine - topLine)
                bottomLine = heightSize
                //重绘
                postInvalidate()
                return true
            }
            //重绘
            postInvalidate()
            return true
        }
        return true
    }

    //按下
    private var downX = 0f
    private var downY = 0f
    private var downLeftLine = 0f
    private var downTopLine = 0f
    private var downRightLine = 0f
    private var downBottomLine = 0f
    private fun actionDown(touchX: Float, touchY: Float): Boolean {
        downX = touchX
        downY = touchY
        downLeftLine = leftLine
        downTopLine = topLine
        downRightLine = rightLine
        downBottomLine = bottomLine
        Log.d("fxHou", "按下X=$touchX   touchY=$touchY")
        val condition1 = touchX > leftLine - 40 && touchX < leftLine + 40
        val condition2 = touchY > topLine - 40 && touchY < topLine + 40
        if (condition1 && condition2) {
            Log.d("fxHou", "左上获得焦点")
            focus = LEFT_TOP //左上获得焦点
            return true
        }
        val condition3 = touchX > rightLine - 40 && touchX < rightLine + 40
        val condition4 = touchY > bottomLine - 40 && touchY < bottomLine + 40
        if (condition3 && condition4) {
            Log.d("fxHou", "右下获得焦点")
            focus = RIGHT_BOTTOM //右下获得焦点
            return true
        }
        val condition5 = touchX > leftLine && touchX < rightLine
        val condition6 = touchY > topLine && touchY < bottomLine
        if (condition5 && condition6) {
            Log.d("fxHou", "整体获得焦点")
            focus = BODY //整体获得焦点
            return true
        }
        return true
    }

    /**
     * 设置要裁剪的位图
     *
     * @param bitmap           要裁剪的位图
     * @param proportionWidth  比例：宽  如裁图比例3:4，此处传3
     * @param proportionHeight 比例：高  如裁图比例3:4，此处传4
     */
    fun setBitmap(bitmap: Bitmap, proportionWidth: Int, proportionHeight: Int) {
        this.bitmap = bitmap
        bitmapWidth = bitmap.getWidth().toFloat()
        bimapHeight = bitmap.getHeight().toFloat()
        this.proportionWidth = proportionWidth.toFloat()
        this.proportionHeight = proportionHeight.toFloat()
        initTag = true
        postInvalidate()
    }

    /**
     * 获取裁剪后的位图
     *
     * @param context
     * @param minPixelWidth  限制最小宽度（像素）
     * @param minPixelHeight 限制最小高度（像素）
     * @return 裁切后的位图
     */
    fun getBitmap(context: Context?, minPixelWidth: Int, minPixelHeight: Int): Bitmap? {
        if (bitmap == null) return null
        val startX = (leftLine / scaleStep).toInt()
        val startY = (topLine / scaleStep).toInt()
        val cutWidth = (rightLine / scaleStep - leftLine / scaleStep).toInt()
        val cutHeight = (bottomLine / scaleStep - topLine / scaleStep).toInt()
        val newBitmap =
            Bitmap.createBitmap(bitmap!!, startX, startY, cutWidth, cutHeight, null, false)
        if (newBitmap.getWidth() < minPixelWidth || newBitmap.getHeight() < minPixelHeight) {
            Toast.makeText(context, "图片太模糊了", Toast.LENGTH_SHORT).show()
            return null
        }
        return newBitmap
    }
}
