package com.example.draw_android.section05_canvas.e_curve

import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.PointF
import android.graphics.RadialGradient
import android.graphics.Rect
import android.graphics.Shader
import kotlin.math.max

object BaseCanvasView {
    private var gridWidth = 80
    private var hCount: Int = 0
    private var wCount: Int = 0


    //网格的宽度
    private var mWidth = 0
    private var mHeight = 0
    fun drawBaseBg(canvas: Canvas, screenWidth: Int, screenHeight: Int) {
        this.mWidth = screenWidth
        this.mHeight = screenHeight

        //宽的格子个数
        this.wCount = screenWidth / gridWidth
        //高的格子个数
        this.hCount = screenHeight / gridWidth
        drawGridLine(canvas)
        drawTextXAndY(canvas)
    }



    private fun drawTextXAndY(canvas: Canvas) {
        val gPaint = Paint()
        gPaint.color = Color.BLUE
        gPaint.strokeWidth = 2f
        gPaint.isAntiAlias = true
        gPaint.style = Paint.Style.STROKE
        gPaint.textSize = 26f
        gPaint.color = Color.argb(255, 75, 151, 79)
        canvas.scale(-1f, 1f)


        canvas.save()

        canvas.scale(1f, -1f)

        //x轴正方形文字
        for (index in 1 until wCount / 2) {
            val rectText = Rect()
            canvas.translate(160f, 0f)
            gPaint.getTextBounds(
                (80 * index * 2).toString(),
                0,
                (80 * index * 2).toString().length,
                rectText
            )
            canvas.drawText(
                (80 * index * 2).toString(),
                -(rectText.width() / 2).toFloat(), rectText.height().toFloat() * 2f, gPaint
            )
        }
        canvas.restore()
        canvas.save()
        //x轴负方向文字绘制
        canvas.scale(1f, -1f)
        for (index in 1 until wCount / 2) {
            val rectText = Rect()
            canvas.translate(-160f, 0f)
            gPaint.getTextBounds(
                "-${(80 * index * 2)}",
                0,
                (80 * index * 2).toString().length,
                rectText
            )
            canvas.drawText(
                "-${(80 * index * 2)}",
                -(rectText.width() / 2).toFloat(), rectText.height().toFloat() * 2f, gPaint
            )
        }
        canvas.restore()

        canvas.save()
        //x轴负方向文字绘制
        canvas.scale(1f, -1f)
        canvas.translate(20f, 0f)
        //y轴负方向
        for (index in 1 until hCount / 2) {
            val rectText = Rect()
            canvas.translate(0f, 160f)
            gPaint.getTextBounds(
                "-${(80 * index * 2)}",
                0,
                (80 * index * 2).toString().length,
                rectText
            )
            canvas.drawText(
                "-${(80 * index * 2)}",
                0f, rectText.height().toFloat(), gPaint
            )
        }
        canvas.restore()

        canvas.save()
        canvas.scale(1f, 1f)
        canvas.translate(20f, 0f)
        //y轴正方向
        for (index in 1 until hCount / 2) {
            val rectText = Rect()
            canvas.translate(0f, 160f)
            canvas.save()
            canvas.scale(1f, -1f)
            gPaint.getTextBounds(
                "${(80 * index * 2)}",
                0,
                (80 * index * 2).toString().length,
                rectText
            )
            canvas.drawText(
                "${(80 * index * 2)}",
                0f, rectText.height().toFloat(), gPaint
            )
            canvas.restore()
        }

        canvas.restore()


    }

    private fun drawGridLine(canvas: Canvas) {
        //初始化一个画笔
        val gPaint = Paint()
        gPaint.color = Color.BLUE
        gPaint.strokeWidth = 2f
        gPaint.isAntiAlias = true
        gPaint.style = Paint.Style.FILL
        gPaint.shader = RadialGradient(
            0f,
            0f,
            max(mWidth, mHeight) / 2f,
            Color.BLUE,
            Color.YELLOW,
            Shader.TileMode.CLAMP
        )
        //onDraw中已经知道屏幕宽度和高度
        val screenWidth = mWidth
        val screenHeight = mHeight
        //宽的格子个数
        wCount = screenWidth / gridWidth
        //高的格子个数
        hCount = screenHeight / gridWidth

        //1.将坐标点移动到屏幕的中点
        canvas.translate((screenWidth / 2).toFloat(), (screenHeight / 2).toFloat())
        //整体坐标系方向顺时针进行变化
        //2.修改y轴上方为正方向。
        canvas.scale(1f, -1f)
        //绘制x轴和y轴
        canvas.drawLine(-screenWidth / 2f, 0f, screenWidth / 2f, 0f, gPaint)
        canvas.drawLine(0f, -screenHeight / 2f, 0f, screenHeight / 2f, gPaint)
        gPaint.color = Color.argb(61, 111, 111, 111)
        drawGridCode(canvas, screenWidth, gPaint, hCount, screenHeight, wCount)
        //2.修改y轴下方为正方向。
        canvas.scale(1f, -1f)
        drawGridCode(canvas, screenWidth, gPaint, hCount, screenHeight, wCount)
        //3.修改x轴左正方向。
        canvas.scale(-1f, 1f)
        drawGridCode(canvas, screenWidth, gPaint, hCount, screenHeight, wCount)
        //4.修改x作为正y上为正
        canvas.scale(1f, -1f)
        drawGridCode(canvas, screenWidth, gPaint, hCount, screenHeight, wCount)


    }

    private fun drawGridCode(
        canvas: Canvas,
        screenWidth: Int,
        gPaint: Paint,
        hCount: Int,
        screenHeight: Int,
        wCount: Int
    ) {
        //这里保存好坐标圆点为屏幕中心的快照到堆栈里面。方便后期操作。
        canvas.save()
        //绘制一条横着的线条,重圆点(0,0)开始
        //canvas.drawLine(0f, 0f, (screenWidth / 2).toFloat(), 0f, gPaint)

        //3.绘制完成第一象限的平行x轴的线
        for (index in 0 until hCount) {
            //坐标系圆点不断向上平移gridWidth的高度
            canvas.translate(0f, gridWidth.toFloat())
            //在平移完的圆点直接画直线即可
            canvas.drawLine(0f, 0f, (screenWidth / 2).toFloat(), 0f, gPaint)
        }
        //恢复到快照状态。即圆点在中心
        canvas.restore()
        canvas.save()
        //4.绘制平行y轴的
        //canvas.drawLine(0f, 0f, 0f, screenHeight / 2f, gPaint)
        for (index in 0 until wCount) {
            //坐标系圆点不断向上平移gridWidth的高度
            canvas.translate(gridWidth.toFloat(), 0f)
            //在平移完的圆点直接画直线即可
            canvas.drawLine(0f, 0f, 0f, screenHeight / 2f, gPaint)
        }
        //恢复到快照状态。即圆点在中心
        canvas.restore()
    }
}