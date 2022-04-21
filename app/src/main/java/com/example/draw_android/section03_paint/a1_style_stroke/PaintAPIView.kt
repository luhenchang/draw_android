package com.example.draw_android.section03_paint.a1_style_stroke
/*/Users/wangfeiwangfei/wangfei/GitHub/draw_android/app/src/main/java/com/example/draw_android/section03_paint/PaintAPIView.kt*/
import android.content.Context
import android.graphics.*
import android.os.Build
import android.util.AttributeSet
import android.util.Log
import android.view.View
import androidx.annotation.RequiresApi
import com.example.draw_android.R

/**
 * Created by wangfei44 on 2022/2/14.
 */
@RequiresApi(Build.VERSION_CODES.Q)
class PaintAPIView constructor(context: Context, attributeSet: AttributeSet) :
    View(context, attributeSet) {
    private val titleList =
        arrayListOf("color = Color.RED", "color = Color.GREEN", "color = Color.BLUE")
    private val textList = arrayListOf("strokeWidth = 0f", "strokeWidth = 30f", "strokeWidth = 60f")
    private var mTouchListener: ((x: Float, y: Float) -> Unit)? = null
    private lateinit var dataList: List<String>

    //1.画笔-宽度颜色和样式
    private val strokeWidthList = arrayOf(Paint().apply {
        strokeWidth = 0f
        color = Color.RED
        style = Paint.Style.STROKE
    }, Paint().apply {
        strokeWidth = 30f
        color = Color.GREEN
        style = Paint.Style.STROKE
    }, Paint().apply {
        strokeWidth = 60f
        color = Color.BLUE
        style = Paint.Style.STROKE
    })

    //2.画笔-宽度一直，填充样式不一样
    private val styleWidthList = arrayOf(Paint().apply {
        strokeWidth = 30f
        color = Color.RED
        style = Paint.Style.STROKE
    }, Paint().apply {
        strokeWidth = 30f
        color = Color.YELLOW
        style = Paint.Style.FILL
        alpha = 122
    })
    private val titleList1 =
        arrayListOf("Paint.Style.STROKE", "Paint.Style.FILL", "Paint.Style.FILL_AND_STROKE")
    private val textList1 =
        arrayListOf("strokeWidth = 30f", "strokeWidth = 30f", "strokeWidth = 30f")
    private val srcBitmap: Bitmap = BitmapFactory.decodeResource(resources, R.drawable.circle_bg)
    private val destBitmap: Bitmap = BitmapFactory.decodeResource(resources, R.drawable.rect_fg)

    private var printFunction: PrintLogEventPosition = { x, y->
        Log.e("PrintLogEventPosition", "event(x=$x,y=$y)")
    }

    init {

    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        canvas.save()
        studyStrokeColor(canvas)
        studyStyle(canvas)
    }


    //2.画笔线宽和颜色透明度
    private fun studyStrokeColor(canvas: Canvas) {
        for (index in strokeWidthList.indices) {
            canvas.translate(width / 4.6f, 0f)
            canvas.drawCircle(150f, height / 2f, 100f, strokeWidthList[index])
            val subTitle = textList[index]
            val title = titleList[index]
            canvas.drawText(
                subTitle,
                0,
                subTitle.length,
                50f,
                height / 2f + 100f + 100f,
                Paint().apply {
                    strokeWidth = 3f
                    color = Color.BLACK
                    style = Paint.Style.FILL
                    textSize = 30f
                })
            canvas.drawText(title, 0, title.length, 50f, height / 2f + 100f + 150f,
                Paint().apply {
                    strokeWidth = 3f
                    color = Color.BLACK
                    style = Paint.Style.FILL
                    textSize = 30f
                })
        }
        canvas.restore()
    }

    //2.填充类型
    private fun studyStyle(canvas: Canvas) {
        canvas.save()
        canvas.translate(width / 4.6f, 0f)
        canvas.drawCircle(150f, height / 2f, 100f, styleWidthList[0])
        canvas.translate(width / 4.6f, 0f)
        canvas.drawCircle(150f, height / 2f, 100f, styleWidthList[1])
        canvas.translate(width / 4.6f, 0f)
        for (index in styleWidthList.indices) {
            canvas.drawCircle(150f, height / 2f, 100f, styleWidthList[index])
        }
        canvas.restore()
    }



}

typealias PrintLogEventPosition = (x:Float,y:Float) -> Unit