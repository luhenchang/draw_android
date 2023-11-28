package com.example.draw_android.section05_canvas.f_event

import android.graphics.RectF
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.WindowCompat
import com.example.draw_android.R


class EventCanvasMainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        WindowCompat.setDecorFitsSystemWindows(window, false)
        setContentView(R.layout.activity_event_canvas_main)
    }

    private fun initView() {
        val dragScaleView: DragScaleView = findViewById(R.id.drag_scale_view)
        val drawBoxView: DrawBoxView = findViewById(R.id.draw_box_view)

        dragScaleView.setImageResource(R.drawable.item_bg_2)
        dragScaleView.setRect(
            RectF(
                0f, 0f, SystemUtils.dp2px(baseContext, 300).toFloat(), SystemUtils.dp2px(
                    baseContext, 500
                ).toFloat()
            )
        )
        dragScaleView.setUpdateListener(object :DragScaleView.UpdateListener{
            override fun onChange(posX: Float, posY: Float, scale: Float) {
                drawBoxView.setValues(
                    posX,
                    posY,
                    scale
                )
            }

        })
        drawBoxView.setRect(
            RectF(
                SystemUtils.dp2px(baseContext, 5).toFloat(),
                SystemUtils.dp2px(baseContext, 5).toFloat(),
                (SystemUtils.dp2px(baseContext, 300) + SystemUtils.dp2px(baseContext, 5)).toFloat(),
                (SystemUtils.dp2px(baseContext, 500) + SystemUtils.dp2px(baseContext, 5)).toFloat()
            )
        )
        drawBoxView.setRectOffset(SystemUtils.dp2px(baseContext, 5))
        drawBoxView.setCropListener { positionIndex, scale, xOffset, yOffset, rectF ->
            dragScaleView.doAnimation(
                positionIndex,
                scale,
                xOffset,
                yOffset,
                rectF
            )
        }
    }
}