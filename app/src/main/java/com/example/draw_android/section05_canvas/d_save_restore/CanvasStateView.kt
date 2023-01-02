package com.example.draw_android.section05_canvas.d_save_restore

import Decoder.BASE64Decoder
import Decoder.BASE64Encoder
import android.content.Context
import android.content.Context.MODE_PRIVATE
import android.content.SharedPreferences
import android.graphics.*
import android.util.AttributeSet
import android.util.Base64
import android.util.Log
import android.view.View
import androidx.compose.ui.geometry.Offset
import java.io.*

/**
 * Created by wangfei44 on 2022/4/21.
 */
class CanvasStateView constructor(context: Context, attributeSet: AttributeSet) :
    View(context, attributeSet) {
    private var mWidth = 0
    private var mHeight = 0
    val paint = Paint().apply {
        color = Color.BLACK
        style = Paint.Style.STROKE
        isAntiAlias = true
        textSize = 24f
    }

    private val paintEye = Paint().apply {
        color = Color.BLACK
        strokeWidth = 5f
        style = Paint.Style.FILL
        isAntiAlias = true
    }

    private val paintEyeRed = Paint().apply {
        color = Color.RED
        strokeWidth = 5f
        style = Paint.Style.FILL
        isAntiAlias = true
    }

    private val paintEar = Paint().apply {
        color = Color.RED
        strokeWidth = 5f
        style = Paint.Style.FILL
        isAntiAlias = true
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
//        canvas.save()
//        //1.绘制眼睛
//        drawEye(canvas)
//        //2.绘制耳朵
//        drawEar(canvas)
//        drawEyebrow(canvas)


        val step = 0.0001f
        val path = Path()
        path.moveTo(100f, 600f)
        path.cubicTo(400f, 200f, 700f, 300f, 800f, 700f)
        path.lineTo(800f, 100f)
        path.lineTo(300f, 900f)
        val measure = PathMeasure(path, false)
        val len = measure.length
        val point = FloatArray(2)
        var t = 0f
        var minX = 800f
        var maxX = 0f
        var minY = 700f
        var maxY = 0f

        var minXPoint = Offset(0f,0f)
        var maxXPoint = Offset(0f,0f)
        var minYPoint = Offset(0f,0f)
        var maxYPoint = Offset(0f,0f)
        while (t <= 1) {
            val dis = t * len
            measure.getPosTan(dis, point, null)
            //获取
            if (minX>point[0]){
               minX = point[0]
               minXPoint = Offset(point[0], point[1])
            }

            if (maxX<point[0]){
                maxX = point[0]
                maxXPoint = Offset(point[0], point[1])
            }

            if (minY>point[1]){
                minY = point[1]
                minYPoint = Offset(point[0], point[1])
            }

            if (maxY<point[1]){
                maxY = point[1]
                maxYPoint = Offset(point[0], point[1])
            }

            canvas.drawPoint(point[0], point[1], paintEye)
            t += step
        }

        canvas.drawLine(minXPoint.x,maxYPoint.y,maxXPoint.x,maxYPoint.y,paintEyeRed)
        canvas.drawLine(maxXPoint.x,minYPoint.y,maxXPoint.x,maxYPoint.y,paintEyeRed)
        canvas.drawLine(minXPoint.x,minYPoint.y,maxXPoint.x,minYPoint.y,paintEyeRed)
        canvas.drawLine(minXPoint.x,minYPoint.y,minXPoint.x,maxYPoint.y,paintEyeRed)



//        canvas.drawLine(minXPoint.x,maxYPoint.y,maxXPoint.x,maxYPoint.y,paintEye)
//        canvas.drawLine(minXPoint.x,maxYPoint.y,maxXPoint.x,maxYPoint.y,paintEye)


    }
    private fun ObjectToString(o: Any?): String? {
        try {
            val byteArrayOutputStream = ByteArrayOutputStream()
            val objectOutputStream = ObjectOutputStream(byteArrayOutputStream)
            objectOutputStream.writeObject(o)
            objectOutputStream.close()
            byteArrayOutputStream.close()
            return BASE64Encoder().encode(byteArrayOutputStream.toByteArray())
        } catch (e: IOException) {
            Log.e("pathResult erro",e.toString())
            e.printStackTrace()
        }
        return null
    }

   private fun StringToObject(str: String?): Any? {
       Log.e("pathResult str=",str.toString())
       try {
            val bs: ByteArray = BASE64Decoder().decodeBuffer(str)
           Log.e("pathResult str=",str.toString())
           val byteArrayInputStream = ByteArrayInputStream(bs)
            val inputStream = ObjectInputStream(byteArrayInputStream)
            val o: Any = inputStream.readObject()
            inputStream.close()
            byteArrayInputStream.close()
            return o
        } catch (e: Exception) {
            Log.e("pathResult erro=",e.toString())
            e.printStackTrace()
        }
        return null
    }


    private fun drawEye(canvas: Canvas) {
        canvas.translate(400f, 400f)
        canvas.drawText("眼睛", 30f, 30f, paint)
        canvas.drawCircle(0f, 0f, 20f, paintEye)
        //1.接下来绘制耳朵，所以保存好当前眼睛绘制场景的矩阵
        canvas.save()
    }


    private fun drawEar(canvas: Canvas) {
        canvas.translate(150f, 0f)
        canvas.drawText("耳朵", 30f, 30f, paint)
        canvas.drawCircle(0f, 0f, 20f, paintEar)
        //2.接下来其他部分，所以保存好当前耳朵绘制场景的矩阵
        canvas.save()
    }

    private fun drawEyebrow(canvas: Canvas) {
        //3.我们需要回到眼睛绘制状态下的矩阵，可以轻松简单的绘制眉毛，因为有眼睛的参考。
        //通过restore 返回到堆栈STACK最顶层的矩阵状态
        canvas.restore()
        //回到了第一次保存的矩阵状态
        canvas.restore()
        //绘制眼眉毛
        canvas.drawLine(-30f, -30f, 20f, -30f, paintEar)
    }
}