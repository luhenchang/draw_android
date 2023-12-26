package com.example.draw_android.section05_canvas.f_event

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Canvas
import android.graphics.Paint
import android.graphics.Rect
import android.util.AttributeSet
import android.util.Log
import android.view.MotionEvent
import android.view.View
import android.widget.Toast
import com.example.draw_android.R

class ScreenImageView : View {
    lateinit var  bitmap:Bitmap
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
        bitmap = BitmapFactory.decodeResource(resources, R.drawable.cap_images)
        Log.e("bitmap::",bitmap.width.toString())
        Log.e("bitmap::",bitmap.height.toString())
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        val srcRect = Rect(0,0,bitmap.width,bitmap.height)
        //1080 是原先大小
        //2352 是高度原先大小
        val destRect = Rect(0,0,1080,2352)
        canvas.drawBitmap(bitmap,srcRect,destRect, Paint())
    }

    override fun onTouchEvent(event: MotionEvent): Boolean {
        when (event.action) {
            MotionEvent.ACTION_DOWN -> {
                // 按下时的处理
                Log.e("坐标：：==",""+event.x+","+event.y)
            }

            MotionEvent.ACTION_UP -> {
                // 抬起时的处理
                Toast.makeText(context, "actiono_up", Toast.LENGTH_SHORT).show()
            }
        }
        return true
    }
}