package com.example.draw_android.section05_canvas

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Canvas
import android.util.AttributeSet
import android.view.View
import com.example.draw_android.R

class BitmapMeshView : View {
    private lateinit var bitmap: Bitmap

    //1、图片宽度分多少格
    private val meshWidth: Int = 4
    private val meshHeight: Int = 4

    //2、交错点的个数
    private val count = (meshWidth + 1) * (meshHeight + 1)
    private val verTs = FloatArray(count * 2)
    private val orIg = FloatArray(count * 2)

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

    //进行加载获取bitmap，获取verTs
    private fun init() {
        var index = 0
        bitmap = BitmapFactory.decodeResource(resources, R.drawable.mash_example)
        //1、图片实际像素宽度
        val bmpWidth = bitmap.width
        //2、图片实际像素高度
        val bmpHeight = bitmap.height
        //3、外层遍历5次，每次遍历将初始化一行格子。
        for (yIndex in 0 until meshHeight + 1) {
            //每一行格子顶点y坐标固定不变的。
            val fy = bmpHeight * yIndex / meshHeight
            //横向遍历格子数。
            for (xIndex in 0 until meshWidth + 1) {
                //计算每行格子左上角的x坐标。
                val fx = bmpWidth * xIndex / meshWidth
                //x轴坐标放偶数位
                verTs[index * 2] = fx.toFloat()
                //y轴的位置放在奇数位置
                verTs[index * 2 + 1] = if (yIndex == meshHeight && xIndex == 2) fy.toFloat() + fy / 7f else fy.toFloat()
                index++
            }
        }
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        //将位图移动到空旷位置显示
        canvas.translate(50f, 200f)
        //将画布整体缩小，避免绘制像素过大，超出屏幕看不到全貌
        canvas.scale(0.5f, 0.5f)
        //规矩绘制位图到画布上。
        canvas.drawBitmapMesh(bitmap, meshWidth, meshHeight, verTs, 0, null, 0, null)
    }
}