package com.example.draw_android.section05_canvas.a3_canvas_scale

import android.graphics.Color
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Window
import androidx.annotation.ColorRes
import androidx.core.content.ContextCompat
import androidx.core.view.WindowCompat
import com.example.draw_android.R
import java.lang.Exception

open class CanvasScaleViewActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_canvas_scale_view)
        WindowCompat.setDecorFitsSystemWindows(window, false)

    }
}