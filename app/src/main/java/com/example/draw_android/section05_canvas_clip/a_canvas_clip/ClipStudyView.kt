package com.example.draw_android.section05_canvas_clip.a_canvas_clip

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.Path
import android.util.AttributeSet
import android.view.View
import android.widget.TextView
import com.example.draw_android.R


class ClipStudyView : androidx.appcompat.widget.AppCompatTextView {
    private var scale: Float = 1f
    private var radius = 150f
    private lateinit var bitmap: Bitmap
    val paint = Paint().apply {
        color = Color.parseColor("#FFBB86FC")
        style = Paint.Style.STROKE
        strokeWidth = 11f
    }

    private val paintFill = Paint().apply {
        color = Color.parseColor("#F3E5F5")
        style = Paint.Style.FILL
        strokeWidth = 11f
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

    private fun init() {
        bitmap = BitmapFactory.decodeResource(resources, R.drawable.img_length)
    }

    private val clipPath = Path()
    private val bgPath = Path()

    override fun draw(canvas: Canvas) {
        super.draw(canvas)
        //1.创建圆圈路径区域
        clipPath.reset()
        clipPath.addCircle(
            width / 2f,
            height / 2f,
            200f - paint.strokeWidth / 2f,
            Path.Direction.CCW
        )
        //2.通过裁剪API进行裁剪出路径区域
        canvas.clipPath(clipPath)
        //3.绘制背景颜色
        //canvas.drawColor(Color.RED)
        canvas.drawBitmap(bitmap,0f,0f,paint)
    }
}







