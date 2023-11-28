package com.example.draw_android.section05_canvas.f_event

import android.animation.AnimatorSet
import android.animation.TypeEvaluator
import android.animation.ValueAnimator
import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Canvas
import android.graphics.Matrix
import android.graphics.Paint
import android.graphics.Point
import android.graphics.RectF
import android.util.AttributeSet
import android.util.Log
import android.view.GestureDetector
import android.view.GestureDetector.SimpleOnGestureListener
import android.view.MotionEvent
import android.view.ScaleGestureDetector
import android.view.ScaleGestureDetector.SimpleOnScaleGestureListener
import android.view.View
import kotlin.math.max
import kotlin.math.min

class DragScaleView @JvmOverloads constructor(context: Context, attrs: AttributeSet? = null) :
    View(context, attrs) {
    private var RECT_MIN_WIDTH = 0f
    private var RECT_MIN_HEIGHT = 0f
    private val mActionMovePoint = Point()
    private var mScaleDetector: ScaleGestureDetector? = null //监听图片缩放
    private var mGestureDetector: GestureDetector? = null //监听图片移动
    private var mRect: RectF? = null
    private var mInitRect: RectF? = null //最开始的裁剪框
    private val mPressPointIndex = -1
    private val mPaint = Paint()
    private var mSourceBitmap: Bitmap? = null //原图
    private var mPosX = 0f
    private var mPosY = 0f //绘制图片的起始位置
    private val actionDownRectLeft = 0f
    private val actionDownRectTop = 0f
    private val actionDownRectRight = 0f
    private val actionDownRectBottom = 0f
    private var xOffset = 0f
    private var yOffset = 0f
    private var mNewBitmap: Bitmap? = null //原图适应控件大小后的新Bitmap对象
    private var mNewWidth = 0f
    private var mNewHeight = 0f
    private var mScaleFactor = 1f
    private var hasGetViewSize = false //组件尺寸只需要获取一次
    private var mUpdateListener: UpdateListener? = null
    fun setImage(bitmap: Bitmap?) {
        mSourceBitmap = bitmap
        initViewSize()
        invalidate()
    }

    fun setImageResource(id: Int) {
        mSourceBitmap = BitmapFactory.decodeResource(resources, id)
        initViewSize()
        invalidate()
    }

    fun setUpdateListener(updateListener: UpdateListener?) {
        mUpdateListener = updateListener
    }

    //截取矩形框中的图片
    //    public Uri getImageUri() {
    //        VivoStorageManager.getInstance().setStorageRootDir(VivoStorageManager.getAvailableSDCardPath());
    //        Matrix matrix = new Matrix();
    //        matrix.postScale(mScaleFactor, mScaleFactor);
    //        Bitmap bitmap = null;
    //        float ox = 0, oy = 0;
    //        if (mNewBitmap != null && !mNewBitmap.isRecycled()) {
    //            bitmap = Bitmap.createBitmap(mNewBitmap, 0, 0, (int) mNewWidth, (int) mNewHeight, matrix, true);
    //            ox = (bitmap.getWidth() - mInitRect.width()) / 2 - mPosX + mRect.left;
    //            oy = (bitmap.getHeight() - mInitRect.height()) / 2 - mPosY + mRect.top;
    //        }
    //        if (bitmap!=null && !bitmap.isRecycled()) {
    //            Bitmap cutBitmap = Bitmap.createBitmap(bitmap, (int) ox, (int) oy, (int) mRect.width(), (int) mRect.height());
    //            return SmartShotUtil.saveImage(cutBitmap, 0, 0, DataCollectConstant.PARAM_FASTSHOT, getContext());
    //        } else {
    //            return null;
    //        }
    //    }
    fun setRect(rectF: RectF?) {
        mRect = RectF(rectF)
        mInitRect = RectF(rectF)
    }

    private fun init(context: Context) {
        mScaleDetector = ScaleGestureDetector(context, SimpleScaleListenerImpl())
        mGestureDetector = GestureDetector(context, SimpleGestureListenerImpl())
    }

    private fun initViewSize() {
        if (width > 0 && height > 0) {
            hasGetViewSize = true
            val widthScale = 1.0f * mInitRect!!.width() / mSourceBitmap!!.getWidth()
            val heightScale = 1.0f * mInitRect!!.height() / mSourceBitmap!!.getHeight()
            val matrix = Matrix()
            matrix.postScale(widthScale, heightScale)
            mNewBitmap = Bitmap.createBitmap(
                mSourceBitmap!!,
                0,
                0,
                mSourceBitmap!!.getWidth(),
                mSourceBitmap!!.getHeight(),
                matrix,
                true
            )
            mNewWidth = mNewBitmap!!.getWidth().toFloat()
            mNewHeight = mNewBitmap!!.getHeight().toFloat()

            //初始时图片居中绘制
            mPosX = 0f
            mPosY = 0f
        }
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        if (mSourceBitmap == null) {
            return
        }
        if (!hasGetViewSize) {
            initViewSize()
        }
        RECT_MIN_WIDTH = mInitRect!!.width() / 4 * mScaleFactor
        RECT_MIN_HEIGHT = mInitRect!!.height() / 4 * mScaleFactor
        canvas.save()
        Log.d(TAG, "qqq$mPosX:$mPosY--$mScaleFactor--$xOffset:$yOffset-01")
        checkBounds()
        if (mUpdateListener != null) {
            mUpdateListener!!.onChange(mPosX, mPosY, mScaleFactor)
        }
        //以图片的中心为基点进行缩放
        canvas.scale(mScaleFactor, mScaleFactor, mPosX + mNewWidth / 2, mPosY + mNewHeight / 2)
        canvas.drawBitmap(mNewBitmap!!, mPosX, mPosY, null)
        Log.d(TAG, "qqq$mPosX:$mPosY--$mScaleFactor--$xOffset:$yOffset-02")
        canvas.restore()
    }

    var xMaxLeftOffset = 0f //裁剪框往左方向(包括左上、左、左下)可滑动到的最大偏移量
    var yMaxTopOffset = 0f //裁剪框往上方向(包括左上、上、右上)可滑动到的最大偏移量
    var xMaxRightOffset = 0f //裁剪框往右方向(包括右上、右、右下)可滑动到的最大偏移量
    var yMaxBottomOffset = 0f //裁剪框往下方向(包括左下、下、右下)可滑动到的最大偏移量
    override fun onTouchEvent(event: MotionEvent): Boolean {
        //双指缩放
        mScaleDetector!!.onTouchEvent(event)
        //单指移动
        mGestureDetector!!.onTouchEvent(event)
        return true
    }

    private var mMaxLeft = 0f
    private var mMaxRight = 0f
    private var mMaxTop = 0f
    private var mMaxBottom = 0f //截图最大可滑动区域在四个方向上的增量
    fun doAnimation(index: Int, scale: Float, x: Float, y: Float, rectF: RectF) {
        var xFlow = 0f
        var yFlow = 0f
        var px = mPosX
        var py = mPosY
        val dx = x / 2 - rectF.width() * (scale - 1) / 2
        val dy = y / 2 - rectF.height() * (scale - 1) / 2
        when (index) {
            0 -> {
                px -= dx
                py -= dy
                val xOffset = mPosX - mMaxLeftWithScale
                val yOffset = mPosY - mMaxTopWithScale
                xFlow =
                    -(mScaleFactor * scale - mScaleFactor) * (mNewWidth - xOffset * 2 / mScaleFactor) * 0.5f
                yFlow =
                    -(mScaleFactor * scale - mScaleFactor) * (mNewHeight - yOffset * 2 / mScaleFactor) * 0.5f
            }

            1 -> {
                px -= dx
                py -= dy
                xOffset = max(
                    (mPosX - mMaxLeftWithScale).toDouble(),
                    (mPosX - mMaxRightWithScale).toDouble()
                )
                    .toFloat()
                yOffset = mPosY - mMaxTopWithScale
                xFlow =
                    if (xOffset - (mPosX - mMaxLeftWithScale) < 0.0000001) -(mScaleFactor * scale - mScaleFactor) * (mNewWidth - xOffset * 2 / mScaleFactor) * 0.5f else (mScaleFactor * scale - mScaleFactor) * (mNewWidth + xOffset * 2 / mScaleFactor) * 0.5f
                yFlow =
                    -(mScaleFactor * scale - mScaleFactor) * (mNewHeight - yOffset * 2 / mScaleFactor) * 0.5f
            }

            2 -> {
                px += dx
                py -= dy
                xOffset = mPosX - mMaxRightWithScale
                yOffset = mPosY - mMaxTopWithScale
                xFlow =
                    (mScaleFactor * scale - mScaleFactor) * (mNewWidth + xOffset * 2 / mScaleFactor) * 0.5f
                yFlow =
                    -(mScaleFactor * scale - mScaleFactor) * (mNewHeight - yOffset * 2 / mScaleFactor) * 0.5f
            }

            3 -> {
                px -= dx
                py -= dy
                xOffset = mPosX - mMaxLeftWithScale
                yOffset = max(
                    (mPosY - mMaxTopWithScale).toDouble(),
                    (mPosY - mMaxBottomWithScale).toDouble()
                )
                    .toFloat()
                xFlow =
                    -(mScaleFactor * scale - mScaleFactor) * (mNewWidth - xOffset * 2 / mScaleFactor) * 0.5f
                yFlow =
                    if (yOffset - (mPosY - mMaxTopWithScale) < 0.0000001) -(mScaleFactor * scale - mScaleFactor) * (mNewHeight - yOffset * 2 / mScaleFactor) * 0.5f else (mScaleFactor * scale - mScaleFactor) * (mNewHeight + yOffset * 2 / mScaleFactor) * 0.5f
            }

            4 -> {
                px += dx
                py -= dy
                xOffset = mPosX - mMaxRightWithScale
                yOffset = max(
                    (mPosY - mMaxTopWithScale).toDouble(),
                    (mPosY - mMaxBottomWithScale).toDouble()
                )
                    .toFloat()
                xFlow =
                    (mScaleFactor * scale - mScaleFactor) * (mNewWidth + xOffset * 2 / mScaleFactor) * 0.5f
                yFlow =
                    if (yOffset - (mPosY - mMaxTopWithScale) < 0.0000001) -(mScaleFactor * scale - mScaleFactor) * (mNewHeight - yOffset * 2 / mScaleFactor) * 0.5f else (mScaleFactor * scale - mScaleFactor) * (mNewHeight + yOffset * 2 / mScaleFactor) * 0.5f
            }

            5 -> {
                px -= dx
                py += dy
                xOffset = mPosX - mMaxLeftWithScale
                yOffset = mPosY - mMaxBottomWithScale
                xFlow =
                    -(mScaleFactor * scale - mScaleFactor) * (mNewWidth - xOffset * 2 / mScaleFactor) * 0.5f
                yFlow =
                    (mScaleFactor * scale - mScaleFactor) * (mNewHeight + yOffset * 2 / mScaleFactor) * 0.5f
            }

            6 -> {
                px -= dx
                py += dy
                xOffset = max(
                    (mPosX - mMaxLeftWithScale).toDouble(),
                    (mPosX - mMaxRightWithScale).toDouble()
                )
                    .toFloat()
                yOffset = mPosY - mMaxBottomWithScale
                xFlow =
                    if (xOffset - (mPosX - mMaxLeftWithScale) < 0.0000001) -(mScaleFactor * scale - mScaleFactor) * (mNewWidth - xOffset * 2 / mScaleFactor) * 0.5f else (mScaleFactor * scale - mScaleFactor) * (mNewWidth + xOffset * 2 / mScaleFactor) * 0.5f
                yFlow =
                    (mScaleFactor * scale - mScaleFactor) * (mNewHeight + yOffset * 2 / mScaleFactor) * 0.5f
            }

            7 -> {
                px += dx
                py += dy
                xOffset = mPosX - mMaxRightWithScale
                yOffset = mPosY - mMaxBottomWithScale
                xFlow =
                    (mScaleFactor * scale - mScaleFactor) * (mNewWidth + xOffset * 2 / mScaleFactor) * 0.5f
                yFlow =
                    (mScaleFactor * scale - mScaleFactor) * (mNewHeight + yOffset * 2 / mScaleFactor) * 0.5f
            }
        }
        val maxPosAnimator = ValueAnimator.ofObject(
            MaxPosEvaluator(), MaxPosition(mMaxLeft, mMaxRight, mMaxTop, mMaxBottom),
            MaxPosition(mMaxLeft - dx, mMaxRight + dx, mMaxTop - dy, mMaxBottom + dy)
        )
        val posAnimator = ValueAnimator.ofObject(
            PosEvaluator(),
            Position(mPosX, mPosY),
            Position(px + xFlow, py + yFlow)
        )
        //        ValueAnimator rectAnimator = ValueAnimator.ofObject(new RectEvaluator(), mRect, new RectF(left, top, right, bottom));
        val scaleAnimator = ValueAnimator.ofFloat(mScaleFactor, mScaleFactor * scale)
        maxPosAnimator.addUpdateListener { animation ->
            mMaxLeft = (animation.getAnimatedValue() as MaxPosition).left
            mMaxRight = (animation.getAnimatedValue() as MaxPosition).right
            mMaxTop = (animation.getAnimatedValue() as MaxPosition).top
            mMaxBottom = (animation.getAnimatedValue() as MaxPosition).bottom
        }
        posAnimator.addUpdateListener { animation ->
            mPosX = (animation.getAnimatedValue() as Position).px
            mPosY = (animation.getAnimatedValue() as Position).py
        }
        scaleAnimator.addUpdateListener { animation ->
            mScaleFactor = animation.getAnimatedValue() as Float
            invalidate()
        }
        val animatorSet = AnimatorSet()
        animatorSet.playTogether(maxPosAnimator, posAnimator, scaleAnimator)
        animatorSet.setDuration(500)
        animatorSet.start()
    }

    /**
     * 不能超出边界
     */
    private var mMaxRightWithScale = 0f //算上图片放大后多出来的宽高，图片向右平移所能移到的最远距离
    private var mMaxLeftWithScale = 0f //算上图片放大后多出来的宽高，图片向左平移所能移到的最远距离
    private var mMaxBottomWithScale = 0f //算上图片放大后多出来的宽高，图片向下平移所能移到的最远距离
    private var mMaxTopWithScale = 0f //算上图片放大后多出来的宽高，图片向上平移所能移到的最远距离

    init {
        init(context)
    }

    private fun checkBounds() {
        if (mScaleFactor >= width / mNewWidth) {
            //宽度方向已经填满
            mPosX =
                min(mPosX.toDouble(), ((mScaleFactor - 1) * (mNewWidth / 2) + mMaxRight).toDouble())
                    .toFloat() //最右 mPosX<=
            mPosX = max(
                mPosX.toDouble(),
                (width - mNewWidth - (mScaleFactor - 1) * (mNewWidth / 2) + mMaxLeft).toDouble()
            ).toFloat() //最左 mPosX>=
        } else {
            mPosX = max(mPosX.toDouble(), ((mScaleFactor - 1) * (mNewWidth / 2)).toDouble())
                .toFloat()
            mPosX = min(
                mPosX.toDouble(),
                (width - mNewWidth - (mScaleFactor - 1) * (mNewWidth / 2)).toDouble()
            ).toFloat()
        }
        mMaxRightWithScale = (mScaleFactor - 1) * (mNewWidth / 2) + mMaxRight
        mMaxLeftWithScale = width - mNewWidth - (mScaleFactor - 1) * (mNewWidth / 2) + mMaxLeft
        if (mScaleFactor >= height / mNewHeight) {
            //高度方向已经填满
            mPosY = min(
                mPosY.toDouble(),
                ((mScaleFactor - 1) * (mNewHeight / 2) + mMaxBottom).toDouble()
            ).toFloat() //最下 mPosY<=
            mPosY = max(
                mPosY.toDouble(),
                (height - mNewHeight - (mScaleFactor - 1) * (mNewHeight / 2) + mMaxTop).toDouble()
            ).toFloat() //最上 mPosY>=
        } else {
            mPosY = max(mPosY.toDouble(), ((mScaleFactor - 1) * (mNewHeight / 2)).toDouble())
                .toFloat()
            mPosY = min(
                mPosY.toDouble(),
                (height - mNewHeight - (mScaleFactor - 1) * (mNewHeight / 2)).toDouble()
            ).toFloat()
        }
        mMaxBottomWithScale = (mScaleFactor - 1) * (mNewHeight / 2) + mMaxBottom
        mMaxTopWithScale = height - mNewHeight - (mScaleFactor - 1) * (mNewHeight / 2) + mMaxTop
    }

    //缩放
    private inner class SimpleScaleListenerImpl : SimpleOnScaleGestureListener() {
        override fun onScale(detector: ScaleGestureDetector): Boolean {
            mScaleFactor *= detector.getScaleFactor()
            mScaleFactor = max(1.0, min(mScaleFactor.toDouble(), 4.0)).toFloat() //缩放倍数范围：1～4
            invalidate()
            return true
        }
    }

    //移动
    private inner class SimpleGestureListenerImpl : SimpleOnGestureListener() {
        override fun onScroll(
            e1: MotionEvent?,
            e2: MotionEvent,
            distanceX: Float,
            distanceY: Float
        ): Boolean {
            mPosX -= distanceX
            mPosY -= distanceY
            invalidate()
            return true
        }
    }

    interface UpdateListener {
        fun onChange(posX: Float, posY: Float, scale: Float)
    }

    internal inner class PosEvaluator : TypeEvaluator<Position> {
        override fun evaluate(fraction: Float, startValue: Position, endValue: Position): Position {
            val x: Float = startValue.px + (endValue.px - startValue.px) * fraction
            val y: Float = startValue.py + (endValue.py - startValue.py) * fraction
            return Position(x, y)
        }
    }

    internal inner class MaxPosEvaluator : TypeEvaluator<MaxPosition> {
        override fun evaluate(
            fraction: Float,
            startValue: MaxPosition,
            endValue: MaxPosition
        ): MaxPosition {
            val left: Float = startValue.left + (endValue.left - startValue.left) * fraction
            val right: Float = startValue.right + (endValue.right - startValue.right) * fraction
            val top: Float = startValue.top + (endValue.top - startValue.top) * fraction
            val bottom: Float = startValue.bottom + (endValue.bottom - startValue.bottom) * fraction
            return MaxPosition(left, right, top, bottom)
        }
    }

    inner class MaxPosition internal constructor(
        val left: Float,
        val right: Float,
        val top: Float,
        val bottom: Float
    )

    inner class Position internal constructor(val px: Float, val py: Float)
    companion object {
        private const val TAG = "DragScaleView"
    }
}
