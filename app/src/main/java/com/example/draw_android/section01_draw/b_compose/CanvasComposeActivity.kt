package com.example.draw_android.section01_draw.b_compose

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Paint
import androidx.compose.ui.graphics.PaintingStyle
import androidx.compose.ui.graphics.drawscope.drawIntoCanvas
import androidx.compose.ui.tooling.preview.Preview

class CanvasComposeActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
          CanvasView()
        }
    }
    @Preview
    @Composable
    fun CanvasView() {
        //绘制载体
        Canvas(
            modifier = Modifier
                .fillMaxWidth()
                .fillMaxHeight()
                .background(Color.White),
            onDraw = {
                val mWidth = size.width
                val mHeight = size.height
                val mRadius = 160f
                val paint = Paint().apply {
                    color = Color.Red
                    style = PaintingStyle.Fill
                }
                drawIntoCanvas { canvas ->
                    canvas.drawCircle(
                        Offset(
                            mWidth / 2f,
                            mHeight / 2f
                        ),
                        mRadius,
                        paint
                    )
                }
            })
    }
}