package com.example.draw_android.section05_canvas.c_scale

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.core.view.WindowCompat
import com.example.draw_android.R

open class CanvasScaleViewActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_canvas_scale_view)
        WindowCompat.setDecorFitsSystemWindows(window, false)

    }
}