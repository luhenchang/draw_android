package com.example.draw_android.section05_canvas.e_curve

import android.os.Bundle
import android.widget.ImageView
import androidx.activity.ComponentActivity
import androidx.core.view.WindowCompat
import com.example.draw_android.R

class CurveViewActivity : ComponentActivity() {
    private lateinit var colorViews: List<ImageView>
    private lateinit var curveImageColorView: CurveImageColorView
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        WindowCompat.setDecorFitsSystemWindows(window, false)
        setContentView(R.layout.activity_canvas_curve_main)
        curveImageColorView = findViewById(R.id.curveImgView)
        colorViews = listOf(
            findViewById(R.id.redImg),
            findViewById(R.id.greenImg),
            findViewById(R.id.blueImg)
        )

        colorViews.forEachIndexed { index, view ->
            view.alpha = if (index == 0) 1f else 0.1f
            view.setOnClickListener {
                curveImageColorView.setTypeMode(index)
                view.alpha = 1f
                colorViews.filterIndexed { i, _ -> i != index }.forEach { it.alpha = 0.1f }
            }
        }
    }
}