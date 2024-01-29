package com.example.draw_android.section05_canvas

import android.animation.ValueAnimator
import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Canvas
import android.graphics.Matrix
import android.graphics.Paint
import android.graphics.Rect
import android.os.Handler
import android.os.Looper
import android.util.AttributeSet
import android.util.Log
import android.view.View
import com.example.draw_android.R
import java.util.Objects
import kotlin.reflect.jvm.internal.impl.load.kotlin.JvmType

class DrawBitmapView : View {
    //判断是否撞击
    private var strike: Boolean = false
    private var dstJsDow: Rect = Rect()
    val paint = Paint()
    private lateinit var dtBitmap: Bitmap

    private var wdFrameList: ArrayList<Bitmap> = ArrayList()
    private var jsSwFrameList: ArrayList<Bitmap> = ArrayList()

    private lateinit var zwWdBitmap: Bitmap

    private lateinit var zwBitmap: Bitmap
    lateinit var zwBitmapTwo: Bitmap

    private var jsFrameList: ArrayList<Bitmap> = ArrayList()

    private lateinit var ziDanBitmap: Bitmap

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
        dtBitmap = BitmapFactory.decodeResource(resources, R.drawable.zwdzjs_dt)
        ziDanBitmap = BitmapFactory.decodeResource(resources, R.drawable.wandou_zidan)
        //将豌豆帧添加到集合中
        val wd00 = BitmapFactory.decodeResource(resources, R.drawable.wd_zhen_00)
        val wd01 = BitmapFactory.decodeResource(resources, R.drawable.wd_zhen_01)
        val wd02 = BitmapFactory.decodeResource(resources, R.drawable.wd_zhen_02)
        val wd03 = BitmapFactory.decodeResource(resources, R.drawable.wd_zhen_03)
        val wd04 = BitmapFactory.decodeResource(resources, R.drawable.wd_zhen_04)
        val wd05 = BitmapFactory.decodeResource(resources, R.drawable.wd_zhen_03)
        val wd06 = BitmapFactory.decodeResource(resources, R.drawable.wd_zhen_02)
        val wd07 = BitmapFactory.decodeResource(resources, R.drawable.wd_zhen_01)

        wdFrameList.add(wd00)
        wdFrameList.add(wd01)
        wdFrameList.add(wd02)
        wdFrameList.add(wd03)
        wdFrameList.add(wd04)
        wdFrameList.add(wd05)
        wdFrameList.add(wd06)
        wdFrameList.add(wd07)

        val jsSwBitMap00 = BitmapFactory.decodeResource(resources, R.drawable.zwdzjs_sw_02)
        val jsSwBitMap01 = BitmapFactory.decodeResource(resources, R.drawable.zwdzjs_sw_03)
        jsSwFrameList.add(jsSwBitMap00)
        jsSwFrameList.add(jsSwBitMap01)
        jsSwFrameList.add(jsSwBitMap01)
        jsSwFrameList.add(jsSwBitMap01)
        jsSwFrameList.add(jsSwBitMap01)
        jsSwFrameList.add(jsSwBitMap01)


        val jsBitmap = BitmapFactory.decodeResource(resources, R.drawable.zwdzjs_js_one)
        val jsBitmap2 = BitmapFactory.decodeResource(resources, R.drawable.zwdzjs_js_one_2)
        jsFrameList.add(jsBitmap)
        jsFrameList.add(jsBitmap2)

        // 初始启动刷新任务
        handler.postDelayed(refreshRunnable, 500)

        handler.postDelayed(refreshRunnable2, 700)
        handler.postDelayed(jsDistanceRefreshRunnable, 100)
        handler.postDelayed(zdDistanceRefreshRunnable, 10)
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        //一、指定位置绘制
        //drawBitmapOfPoint(canvas)

        //二、指定位置缩放
        //drawBitmapOfRect(canvas)
        //二、绘制豌豆进行非源图等比例缩放
        //drawBitmapWandoOfRect(canvas)

        //三、进行贴地图
        drawGameMap(canvas)
        drawGameWanDoZd(canvas)
        drawGameWanDo(canvas)
        drawGameJs(canvas)
    }


    private fun drawBitmapOfPoint(canvas: Canvas) {
        Log.e("屏幕宽高=", "width=${width},height=${height}")
        canvas.drawBitmap(dtBitmap, 0f, 0f, paint)
        Log.e("地图像素=", "width=${dtBitmap.width},height=${dtBitmap.height}")
        //将坐标(200f,height/2f)作为豌豆左上角绘制到画布上
        canvas.drawBitmap(zwWdBitmap, 200f, height / 2f, paint)
        Log.e("豌豆像素=", "width=${zwWdBitmap.width},height=${zwWdBitmap.height}")
    }

    /**
     * 根据源码注释进行理解，即，将通过src来指定获取bitmap中的矩形部分，然后将获取bitmap的src部分，绘制到画布的dst部分，
     * 如果大小src和dst大小不一致，将会进行缩放，使其最终可以铺满dst矩形部分
     */
    private fun drawBitmapOfRect(canvas: Canvas) {
        //这个src是要获取地图的那部分，下面是获取bitmap左上角为顶点，长和宽分别为100像素
        val src = Rect(0, 0, 400, 400)
        //绘制一个圆点作为参照
        canvas.drawCircle(700f, 400f, 10f, paint)
        //绘制到屏幕左上角（700，400）为定点，长为800-700 = 100像素，宽为=500-400像素
        val dst = Rect(700, 400, 800, 500)
        canvas.drawBitmap(dtBitmap, src, dst, paint)
    }

    private fun drawBitmapWandoOfRect(canvas: Canvas) {
        //1、作为参照物，放在左上角显示
        canvas.drawBitmap(zwWdBitmap, 0f, 0f, paint)

        //2、获取图像区域src,这个src是要获取豌豆整体部分，下面是获取bitmap左上角为顶点，长和宽分别为自身像素
        val src = Rect(0, 0, zwWdBitmap.width, zwWdBitmap.height)
        //3、绘制一个圆点作为参照
        canvas.drawCircle(700f, width / 2f, 10f, paint)
        //4、绘制到矩形宽高为(200px,100px)的矩形上面，左上角定点为width/2,400像素，所以right=width/2+200和bottom=400+100
        val dst = Rect(width / 2, 400, width / 2 + 200, 400 + 100)
        canvas.drawBitmap(zwWdBitmap, src, dst, paint)
    }

    //地图
    private fun drawGameMap(canvas: Canvas) {
        //1、这个src是要获取地图的那部分，下面是获取bitmap左上角为顶点，长和宽分别为自身的宽高，表示获取全部图像内容。
        val src = Rect(0, 0, dtBitmap.width, dtBitmap.height)
        //绘制到屏幕左上角（0，0）为定点，长为屏幕的宽高进行绘制到屏幕大小的区域内。也就是不管地图大小多少我们都进行缩放到屏幕内。让图片的所有像素都缩放到画布上。
        val dst = Rect(0, 0, width, height)
        canvas.drawBitmap(dtBitmap, src, dst, paint)
    }

    private val handler = Handler(Looper.getMainLooper())

    private val refreshRunnable = object : Runnable {
        override fun run() {
            // 更新动画状态
            if (wdFrameIndex < wdFrameList.size - 1) {
                wdFrameIndex++
            } else {
                wdFrameIndex--
            }

            // 请求重绘
            invalidate()

            // 每500毫秒后再次执行
            handler.postDelayed(this, 500)
        }
    }

    private val refreshRunnable2 = object : Runnable {
        override fun run() {
            // 更新动画状态
            if (jsFrameIndex < jsFrameList.size - 1) {
                jsFrameIndex++
            } else {
                jsFrameIndex--
            }

            // 请求重绘
            invalidate()

            // 每500毫秒后再次执行
            handler.postDelayed(this, 800)
        }
    }


    //豌豆
    private var wdFrameIndex = 0
    private fun drawGameWanDo(canvas: Canvas) {
        val wdBitmapFrame = wdFrameList[wdFrameIndex]
        //2、计算比例,因为我需要在地图高度上放5排植物所以大小就这样。
        val scale = 1 / 3f
        //2.1、我们需要豌豆的所有像素
        val srcWanDow = Rect(0, 0, wdBitmapFrame.width, wdBitmapFrame.height)
        //2.2、通过比例计算应该的宽高
        val dstWanDowHeight = (wdBitmapFrame.height * scale).toInt()
        val dstWanDowWidth = (wdBitmapFrame.width * scale).toInt()
        //2.3、设置给最终的destRect.
        //这里height - dstWanDowHeight-20 不难理解，height为屏幕最底部，我需要在能距离屏幕底部20像素的地方看到豌豆。
        val dstWanDow =
            Rect(
                500,
                height - dstWanDowHeight - 20,
                500 + dstWanDowWidth,
                height - dstWanDowHeight - 20 + dstWanDowHeight
            )
        //2.4、绘制结果
        canvas.drawBitmap(wdBitmapFrame, srcWanDow, dstWanDow, paint)
    }

    private var jsFrameIndex = 0

    //小僵尸行走的距离。
    private var jsDistance = 0

    private var zdDistance = 0

    private val zdDistanceRefreshRunnable = object : Runnable {
        override fun run() {
            zdDistance += 1
            // 请求重绘
            postInvalidate()
            // 每500毫秒后再次执行
            handler.postDelayed(this, 10)
        }
    }
    private val jsDistanceRefreshRunnable = object : Runnable {
        override fun run() {
            jsDistance += 10
            // 请求重绘
            postInvalidate()
            // 每500毫秒后再次执行
            handler.postDelayed(this, 500)
        }
    }

    private var jsSwIndex = 0

    //僵尸死亡
    private val jsDeathRefreshRunnable = object : Runnable {
        override fun run() {
            jsSwIndex += 1
            // 请求重绘
            postInvalidate()
            // 每500毫秒后再次执行
            handler.postDelayed(this, 2000)
        }
    }

    private var jSAlive = true

    //僵尸
    private fun drawGameJs(canvas: Canvas) {
        val wdBitmapFrame = jsFrameList[jsFrameIndex]
        val srcJsDow = Rect(0, 0, wdBitmapFrame.width, wdBitmapFrame.height)
        //要根据僵尸的实际大小进行给比例，因为僵尸和豌豆的实际像素不一致，给了一样的比例可能太小或太大。
        val scaleJs = 1 / 2.2f
        val dstJsHeight = (wdBitmapFrame.height * scaleJs).toInt()
        val dstJsWidth = (wdBitmapFrame.width * scaleJs).toInt()
        //这里屏幕宽度减去800=计算屏幕右边距离小僵尸后脚跟的距离。
        dstJsDow =
            Rect(
                width - 800 - jsDistance,
                height - dstJsHeight - 20,
                width - 800 + dstJsWidth - jsDistance,
                height - dstJsHeight - 20 + dstJsHeight
            )
        //绘制僵尸
        if (!strike) {
            canvas.drawBitmap(wdBitmapFrame, srcJsDow, dstJsDow, paint)
        } else {
            handler.postDelayed(jsDeathRefreshRunnable, 1000)
            //碰撞结束执行小僵尸倒地动画。
            Log.e("绘制僵尸距离：：", jsDistance.toString())
            val swJsSrcRect =
                Rect(0, 0, jsSwFrameList[jsSwIndex].width, jsSwFrameList[jsSwIndex].height)
            val dstJsSwHeight = (jsSwFrameList[jsSwIndex].height * scaleJs).toInt()
            val dstJsSwWidth = (jsSwFrameList[jsSwIndex].width * scaleJs).toInt()
            val dstSwJsDow =
                Rect(
                    width - 800 - jsDistance,
                    height - dstJsSwHeight + 100,
                    width - 800 + dstJsSwWidth - jsDistance,
                    height - dstJsSwHeight + 50 + dstJsSwHeight
                )
            //完全死亡需要消失，就不需要绘制僵尸了。默认是活着的。
            if (jSAlive){
                canvas.drawBitmap(jsSwFrameList[jsSwIndex], swJsSrcRect, dstSwJsDow, paint)
            }
            //僵尸死亡根据帧来达到效果，我们案例由于帧比较少，所以击中之后切换两张倒地，2秒后让其消失
            if (jsSwIndex >= 2) {
                handler.removeCallbacks(jsDeathRefreshRunnable)
                //僵尸倒地平躺2秒之后立马消失。
                handler.postDelayed({
                    jSAlive = false
                }, 2000)
            }
            handler.removeCallbacks(refreshRunnable2)
            handler.removeCallbacks(jsDistanceRefreshRunnable)
        }
    }

    //子弹
    private fun drawGameWanDoZd(canvas: Canvas) {
        val scaleJs = 1 / 6f
        val srcJsDow = Rect(0, 0, ziDanBitmap.width, ziDanBitmap.height)
        val zdWidth = (ziDanBitmap.width * scaleJs).toInt()
        val zdHeight = (ziDanBitmap.height * scaleJs).toInt()
        //获取豌豆位置，来计算子弹位置
        val zdLeft = 500 + wdFrameList[0].width * scaleJs + 20
        //根据豌豆的位置计算子弹位置，应该不难，最好自己画图理解。我这里简单计算了大概位置。
        val zdTop = height - wdFrameList[0].height * (1 / 2.2) + zdHeight

        val dstRect = Rect(
            zdLeft.toInt() + zdDistance,
            zdTop.toInt(),
            (zdLeft + zdWidth).toInt() + zdDistance,
            (zdTop + zdHeight).toInt()
        )
        //子弹右边距离屏幕左边的长度如果大于等于僵尸左边到屏幕左边的距离表示碰撞。
        if (dstJsDow.left > 0) {
            strike = dstRect.right >= dstJsDow.left + 120//💥因为涂层的透明度所占比例比较大，所以减去100更小僵尸
        }
        //当碰撞之后子弹消失，未碰撞子弹运行。
        if (!strike) {
            //2.4、绘制结果
            canvas.drawBitmap(ziDanBitmap, srcJsDow, dstRect, paint)

        }
        Log.e("子弹最后位置=", dstRect.right.toString())
        Log.e("僵尸最后位置=", dstJsDow.left.toString())
        canvas.drawBitmap(ziDanBitmap, srcJsDow, dstRect, paint)

    }

}