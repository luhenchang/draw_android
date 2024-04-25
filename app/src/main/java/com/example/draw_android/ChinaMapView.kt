package com.example.draw_android

import MapPoint
import android.animation.ValueAnimator
import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.BlurMaskFilter
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.DashPathEffect
import android.graphics.Paint
import android.graphics.Path
import android.graphics.PathMeasure
import android.graphics.Rect
import android.graphics.RectF
import android.graphics.Region
import android.util.AttributeSet
import android.util.Log
import android.view.MotionEvent
import android.view.ScaleGestureDetector
import android.view.View
import android.view.animation.LinearInterpolator
import androidx.annotation.ColorInt
import androidx.core.graphics.blue
import androidx.core.graphics.green
import androidx.core.graphics.red
import bgRect
import com.example.draw_android.section05_canvas.f_event.EventDisallowInterceptListener
import com.example.draw_android.section12_particle.GestureParticle
import com.google.gson.Gson
import jsonMap
import mapAreaNameJson
import mapTuJson
import nmgJson
import kotlin.math.absoluteValue
import kotlin.math.max
import kotlin.math.min
import kotlin.math.sqrt
import kotlin.random.Random


class ChinaMapView : View, EventDisallowInterceptListener {
    private var distanceLenght = 0f
    private var caizhiFlg: Int = 0
    private lateinit var pathImg0: Path
    private lateinit var pathImg1: Path
    private var mapAreaNameBean: MapAreaNameBean? = null
    private lateinit var mapTuPath: Path
    private var dtBitmap: Bitmap? = null
    private var dtBitmap2: Bitmap? = null

    private var difY: Float = 0f
    private var difX: Float = 0f
    private var curScale: Float = 1f
    private var eventModeType: Int = 1
    private lateinit var mScaleGestureDetector: ScaleGestureDetector

    private var oriDis: Float = 0f
    private var preScale = 1f //之前的伸缩值
    private val SCALE_FACTOR = .5f

    private var mapPointBean: MapPoint? = null
    private var mapNmgPointBean: MapNmgBean? = null
    private var mapBgRect: MapBgBean? = null
    private var mapTuBean: MapTuBean? = null


    val paintStroke = Paint().apply {
        color = Color.RED
        style = Paint.Style.STROKE
        strokeWidth = 11f
    }
    val paint = Paint().apply {
        color = Color.RED
        style = Paint.Style.STROKE
        strokeWidth = 0.2f
        textSize = 1f
    }
    val bitMapPaint = Paint().apply {
        maskFilter = BlurMaskFilter(30f, BlurMaskFilter.Blur.NORMAL)
    }
    val paintDash = Paint().apply {
        color = Color.GRAY
        style = Paint.Style.STROKE
        strokeWidth = 0.03f
        pathEffect = DashPathEffect(floatArrayOf(0.01f, 0.01f), 0.0045f)
    }
    val paintText = Paint().apply {
        color = Color.WHITE
        style = Paint.Style.FILL
        textSize = 2f
    }

    val paintTextArea = Paint().apply {
        color = Color.BLACK
        style = Paint.Style.FILL
        textSize = 2f
    }

    val paintFill = Paint().apply {
        color = Color.GREEN
        style = Paint.Style.FILL
        strokeWidth = 0.1f
    }

    val paintTuFill = Paint().apply {
        color = Color.BLUE
        style = Paint.Style.FILL
        strokeWidth = 0.1f
    }


    val paintCircleFill = Paint().apply {
        color = Color.YELLOW
        style = Paint.Style.FILL
        strokeWidth = 0.1f
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

    data class PathBean(val path: Path, @ColorInt val color: Int)

    private var pathList = ArrayList<PathBean>()
    private var pathNmgList = ArrayList<PathBean>()

    var centerX = 0f
    var centerY = 0f
    private var animator = ValueAnimator.ofFloat(0f, 1f)//时间是变化的

    fun init() {
        dtBitmap = BitmapFactory.decodeResource(resources, R.drawable.img_yinse)
        dtBitmap2 = BitmapFactory.decodeResource(resources, R.drawable.img_goldd)


        mapPointBean = Gson().fromJson(jsonMap, MapPoint::class.java)
        mapNmgPointBean = Gson().fromJson(nmgJson, MapNmgBean::class.java)
        mapBgRect = Gson().fromJson(bgRect, MapBgBean::class.java)
        mapTuBean = Gson().fromJson(mapTuJson, MapTuBean::class.java)
        mapAreaNameBean = Gson().fromJson(mapAreaNameJson, MapAreaNameBean::class.java)


        val string = mapPointBean!!.features[0].geometry.coordinates[0][0][0].toString()
        Log.e("string==", string)
        centerX = mapPointBean!!.features[0].properties.center[0].toFloat()
        centerY = mapPointBean!!.features[0].properties.center[1].toFloat()
        mapPointBean!!.features.forEach { feature ->
            val path = Path()
            path.moveTo(
                feature.geometry.coordinates[0][0][0][0].toFloat(),
                feature.geometry.coordinates[0][0][0][1].toFloat()
            )
            feature.geometry.coordinates[0][0].forEachIndexed { index, doubles ->
                Log.e("string==", doubles.toString())
                path.lineTo(
                    doubles[0].toFloat(),
                    doubles[1].toFloat()
                )
            }
            path.close()
            pathList.add(
                PathBean(
                    path, Color.argb(
                        225,
                        Random.nextInt(255),
                        Random.nextInt(255),
                        Random.nextInt(255)
                    )
                )
            )
        }

        mapNmgPointBean!!.features.forEach { feature ->
            val path = Path()
            path.moveTo(
                feature.geometry.coordinates[0][0][0][0].toFloat(),
                feature.geometry.coordinates[0][0][0][1].toFloat()
            )
            feature.geometry.coordinates[0][0].forEachIndexed { index, doubles ->
                Log.e("string==", doubles.toString())
                path.lineTo(
                    doubles[0].toFloat(),
                    doubles[1].toFloat()
                )
            }
            path.close()
            pathNmgList.add(
                PathBean(
                    path, Color.argb(
                        225,
                        Random.nextInt(255),
                        Random.nextInt(255),
                        Random.nextInt(255)
                    )
                )
            )
        }

        //白色区域覆盖路径
        mapTuPath = Path()
        mapTuPath.moveTo(
            mapTuBean!!.features[0].geometry.coordinates[0][0].toFloat(),
            mapTuBean!!.features[0].geometry.coordinates[0][1].toFloat()
        )
        mapTuBean?.features!![0].geometry.coordinates.forEach { point ->
            mapTuPath.lineTo(
                point[0].toFloat(),
                point[1].toFloat()
            )

        }
        mapTuPath.close()

        animator.duration = 2000
        animator.repeatCount = -1
        animator.interpolator = LinearInterpolator()
        animator.addUpdateListener {
            updateGestureParticle()
            invalidate()
        }
    }

    private var selectedIndex = 0
    private var smallSelectedIndex = 0

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        canvas.save()

        canvas.drawLine(0f, 0f, 300f, 600f, paintDash)
        //testTranslate(canvas)
        drawMap(canvas)
        canvas.restore()

        canvas.save()
        pathImg0 = Path()
        pathImg0.addRect(width - 300f, 100f, width - 100f, 300f, Path.Direction.CCW)
        val srcRect = Rect(0, 0, dtBitmap!!.width, dtBitmap!!.height)
        val dctRect = Rect(width - 300, 100, width - 100, 300)
        canvas.clipPath(pathImg0)
        canvas.drawBitmap(dtBitmap!!, srcRect, dctRect, bitMapPaint)
        canvas.restore()

        canvas.save()
        pathImg1 = Path()
        pathImg1.addRect(width - 300f, 310f, width - 100f, 510f, Path.Direction.CCW)
        val srcRect1 = Rect(0, 0, dtBitmap2!!.width, dtBitmap2!!.height)
        val dctRect1 = Rect(width - 300, 310, width - 100, 510)
        canvas.clipPath(pathImg1)
        canvas.drawBitmap(dtBitmap2!!, srcRect1, dctRect1, bitMapPaint)
        canvas.restore()
    }

    private fun testTranslate(canvas: Canvas) {
        //绘制圆
        //100,200
        canvas.drawCircle(100 * 2 + width / 2f, 200 * 3 + height / 2f, 20f, paintFill)
        canvas.drawLine(0f, 0f, 100f, 200f, paint)
        canvas.translate(width / 2f, height / 2f)
        canvas.drawLine(-width.toFloat() / 2f, 0f, width.toFloat(), 0f, paintStroke)
        canvas.drawLine(0f, -height.toFloat() / 2f, 0f, height.toFloat() / 2f, paintStroke)

        canvas.drawLine(0f, 0f, 100f, 200f, paint)
        canvas.scale(2f, 3f)
        canvas.drawLine(0f, 0f, 100f, 200f, paint)
    }

    private fun drawMap(canvas: Canvas) {
        //122.51865306, 23.46078502
        canvas.translate(width / 2f + difX, height / 2f + difY)
        canvas.scale(6.5f * 4 * curScale, -7.5f * 4 * curScale)
        canvas.translate(-centerX, -centerY)

        //绘制底部蓝色海洋
        canvas.drawColor(Color.parseColor("#A4CDFF"))

        canvas.save()
        canvas.drawPath(mapTuPath, paintTuFill)
        val bitMap = if (caizhiFlg == 0) dtBitmap else dtBitmap2
        val srcRect = Rect(0, 0, bitMap!!.width, bitMap.height)
        val dctRect = Rect(-1000, -1000, bitMap.width, bitMap.height)
        canvas.clipPath(mapTuPath)
        canvas.drawBitmap(bitMap, srcRect, dctRect, bitMapPaint)
        canvas.restore()



        drawMapAll(canvas)

        if (curScale > 1.5) {
            //绘制市区背景
            pathNmgList[smallSelectedIndex].let { pathBean ->
                canvas.drawPath(pathBean.path, paintFill.apply {
                    color = pathBean.color
                })
            }
            //绘制区域
            drawNmgMap(canvas)
            //绘制label
            drawMapSmallLabel(canvas)


        } else {
            drawSelectedBg(canvas)
            particleList.forEach {
                Log.e("it.pointX=", it.pointX.toString())
                Log.e("it.pointY=", it.pointY.toString())
                canvas.drawCircle(
                    it.pointX,
                    it.pointY,
                    it.radius,
                    paint.apply { color = it.color })
            }
        }

        drawMapLiable(canvas)

    }

    private fun drawSelectedBg(canvas: Canvas) {
        pathList[selectedIndex].let { pathBean ->
            canvas.drawPath(pathBean.path, paintFill.apply {
                color = pathBean.color
            })
            canvas.drawPath(pathBean.path,
                paint.apply {
                    color = pathBean.color
                    setShadowLayer(0.01f, 0.05f, 0.05f, Color.BLACK)
                }
            )
            val posStart = FloatArray(2)
            val posTan = FloatArray(2)
            val pathMeasure = PathMeasure()
            pathMeasure.setPath(pathBean.path, false)
            pathMeasure.getPosTan(0f, posStart, posTan)
            //根据距离获取其坐标点
            Log.e("pathMeasure.length=", pathMeasure.length.toString())
            if (distanceLenght <= pathMeasure.length) {
                val pos = FloatArray(2)
                val tan = FloatArray(2)
                pathMeasure.getPosTan(distanceLenght, pos, tan)
                createGestureParticle(pos[0], pos[1])
                distanceLenght += 0.1f
            } else {
                distanceLenght = 0f
            }
        }
    }

    private var particleList = ArrayList<GestureParticle>()

    //更新所有的粒子位置
    private fun createGestureParticle(x: Float, y: Float) {
        repeat(3) {
            val particle = GestureParticle(
                radius = (java.util.Random().nextInt(10).toFloat() + 3) / 100,
                pointX = x,
                pointY = y,
                angle = java.util.Random().nextInt(360).toFloat(),
                velocityX = (java.util.Random().nextFloat() * 2 - 1) / 10,
                velocityY = (java.util.Random().nextFloat() * 2 - 1) / 10,
                acceleration = 0.02f,
                color = Color.argb(
                    255,
                    java.util.Random().nextInt(255),
                    java.util.Random().nextInt(255),
                    java.util.Random().nextInt(255)
                ),
                recycleDistance = 500,
                t = 0
            )
            particleList.add(particle)
        }
        animator.start()
    }

    private fun updateGestureParticle() {
        val iterator = particleList.iterator()
        if (!iterator.hasNext()) {
            Log.e("粒子消失？", "yes")
            animator.cancel()
        }
        while (iterator.hasNext()) {
            val particle = iterator.next()
            particle.pointX += particle.velocityX
            particle.pointY += particle.velocityY
            particle.t += 1
            val alpha = (255 * (1 - min(particle.t, 50) / 50f))
            particle.color = Color.argb(
                alpha.toInt(),
                particle.color.red,
                particle.color.green,
                particle.color.blue
            )
            if (particle.t > 50) {
                iterator.remove()
            }
        }
    }

    private fun drawMapAll(canvas: Canvas) {
        pathList.forEachIndexed { index, path ->
            canvas.drawPath(
                path.path,
                paint.apply {
                    color = path.color
                    setShadowLayer(0.01f, 0.05f, 0.05f, Color.BLACK)
                })
        }
    }

    private fun drawNmgMap(canvas: Canvas) {

        pathNmgList.forEachIndexed { _, path ->
            canvas.drawPath(
                path.path,
                paintDash
            )
        }


    }

    private fun drawMapSmallLabel(canvas: Canvas) {
        mapNmgPointBean?.features?.forEachIndexed { index, feature ->
            if (feature.properties != null && feature.properties.center != null) {
                val translateXDiff = feature.properties.center[0].toFloat()
                val translateYDiff = feature.properties.center[1].toFloat()
                canvas.save()
                canvas.translate(translateXDiff, translateYDiff)
                canvas.drawCircle(0f, 0f, 0.1f, paintCircleFill)
                canvas.scale(0.3f, -0.3f)
                val rect = Rect()
                val contextText = feature.properties.name
                paint.getTextBounds(contextText, 0, contextText.length, rect)
                val translateX = (rect.width())
                canvas.drawText(
                    contextText,
                    -translateX.toFloat() * 0.5f,
                    -rect.height() / 2f,
                    paintText
                )
                canvas.restore()
            }
        }

    }

    private fun drawMapLiable(canvas: Canvas) {
        //亚洲、欧洲、非洲
        mapAreaNameBean?.features?.forEach { feature ->
            val translateXDiff = feature.geometry.coordinates[0].toFloat()
            val translateYDiff = feature.geometry.coordinates[1].toFloat()
            canvas.save()
            canvas.translate(translateXDiff, translateYDiff)
            canvas.drawCircle(0f, 0f, 0.1f, paintCircleFill)
            canvas.scale(1f, -1f)
            val rect = Rect()
            val contextText = feature.properties.name
            paintTextArea.getTextBounds(contextText, 0, contextText.length, rect)
            val translateX = (rect.width())
            canvas.drawText(
                contextText,
                -translateX.toFloat() * 0.5f,
                -rect.height() / 2f,
                paintTextArea
            )
            canvas.restore()
        }
        mapPointBean?.features?.forEachIndexed { index, feature ->
            if (feature.properties != null && feature.properties.center != null) {
                val translateXDiff = feature.properties.center[0].toFloat()
                val translateYDiff = feature.properties.center[1].toFloat()
                canvas.save()
                canvas.translate(translateXDiff, translateYDiff)
                canvas.drawCircle(0f, 0f, 0.1f, paintCircleFill)
                canvas.scale(0.3f, -0.3f)
                val rect = Rect()
                val contextText = feature.properties.name
                paint.getTextBounds(contextText, 0, contextText.length, rect)
                val translateX = (rect.width())
                canvas.drawText(
                    contextText,
                    -translateX.toFloat() * 0.5f,
                    -rect.height() / 2f,
                    paintText
                )
                canvas.restore()
            }
        }

    }


    override fun performClick(): Boolean {
        return super.performClick()
    }

    private var disallowIntercept: Boolean = false
    override fun requestDisallowInterceptTouchEvent(disallowIntercept: Boolean) {
        this.disallowIntercept = disallowIntercept
    }

    private var lastX = 0f
    private var lastY = 0f
    private var lastTime = 0L
    override fun onTouchEvent(event: MotionEvent): Boolean {
        // mScaleGestureDetector.onTouchEvent(event)
        when (event.action and MotionEvent.ACTION_MASK) {
            MotionEvent.ACTION_DOWN -> {
                performClick()
                parent?.requestDisallowInterceptTouchEvent(disallowIntercept)
                //1.表示单点事件
                eventModeType = 1
                lastX = event.x
                lastY = event.y
            }

            MotionEvent.ACTION_POINTER_DOWN -> {
                //多点触控
                oriDis = distance(event)
                if (oriDis > 10 && event.pointerCount >= 2) {
                    //2.表示多点触碰类型
                    eventModeType = 2
                }

            }

            MotionEvent.ACTION_MOVE -> {
                if (eventModeType == 2) {
                    // 获取两个手指缩放时候的之间距离
                    val newDist = distance(event)
                    if (newDist > 10) {
                        //通过当前的距离除以上一手指按下两趾头之间的距离就为实时的缩放
                        curScale = (newDist / oriDis)
                        if (curScale >= 1) {
                            curScale = preScale + (curScale - 1) * SCALE_FACTOR//有助于理解，简写如下：
                            //curScale += preScale-1
                        } else {
                            curScale = preScale - (1 - curScale) * SCALE_FACTOR
                        }
                        curScale = minOf(maxOf(curScale, 0.3f), 120f)
                        Log.e("curScale==", curScale.toString())
                        preScale = curScale
                        //通知刷新View
                        invalidate()
                    }
                } else {
                    //防止缩放结束之后，触发拖动事件，导致地图位移
                    if (System.currentTimeMillis() - lastTime < 200) {
                        return true
                    }
                    difX += (event.x - lastX)
                    difY += (event.y - lastY)
                    //限制可见
                    Log.e("diffXX=", difX.toString())
                    Log.e("diffYY=", difY.toString())

                    if ((event.x - lastX).absoluteValue > 5 || (event.y - lastY).absoluteValue > 5) {
                        eventModeType = 3
                        //通知刷新View
                        lastX = event.x
                        lastY = event.y
                        invalidate()
                    }

                }

            }

            MotionEvent.ACTION_CANCEL,
            MotionEvent.ACTION_UP,
            MotionEvent.ACTION_POINTER_UP -> {
                if (eventModeType == 1) {
                    // 将点击事件的坐标映射到画布坐标系中
                    val clickX = event.x
                    val clickY = event.y

                    //x*2+width/2f = eventx,
                    //x = (clickX - width/2f)/2
                    //y*3+height/2f = eventy,
                    //y = (clickY - height/2f)/3

                    // 逆向的坐标系平移操作
                    val mappedX = (clickX - width / 2f - difX) / (6.5f * 4 * curScale) + centerX
                    val mappedY = (clickY - height / 2f - difY) / (-7.5f * 4 * curScale) + centerY


                    if (curScale > 1.5) {//局部省市操作
                        pathNmgList.forEachIndexed { index, path ->
                            if (PointIsInPath(mappedX, mappedY, path.path)) {
                                smallSelectedIndex = index
                                invalidate()
                                return true
                            }
                        }
                    } else {//省特别行政区
                        // 遍历路径列表，检查点击位置是否在某个路径内部
                        pathList.forEachIndexed { index, path ->
                            if (PointIsInPath(mappedX, mappedY, path.path)) {
                                selectedIndex = index
                                invalidate()
                                return true
                            }
                        }
                    }
                    if (PointIsInPath(clickX, clickY, pathImg0)) {
                        caizhiFlg = 0
                    } else if (PointIsInPath(clickX, clickY, pathImg1)) {
                        caizhiFlg = 1
                    }
                    invalidate()
                } else if (eventModeType == 3) {
                    lastX = 0f
                    lastY = 0f
                }
                eventModeType = 0
                lastTime = System.currentTimeMillis()
            }
        }
        return true
    }

    fun PointIsInPath(x: Float, y: Float, path: Path): Boolean {
        val bounds = RectF()
        path.computeBounds(bounds, true)
        val region = Region()
        region.setPath(
            path,
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

    /**
     * 计算两个手指间的距离
     *
     * @param event 触摸事件
     * @return 放回两个手指之间的距离
     */
    private fun distance(event: MotionEvent): Float {
        if (event.size < 2) {
            return 0f
        }
        val pointerIndex1 = 0
        val pointerIndex2 = 1
        // 确保指定的索引在有效范围内
        if (pointerIndex1 >= event.pointerCount || pointerIndex2 >= event.pointerCount) {
            return 0f
        }

        val x = event.getX(0) - event.getX(1)
        val y = event.getY(0) - event.getY(1)
        return sqrt((x * x + y * y).toDouble()).toFloat() //两点间距离公式
    }
}