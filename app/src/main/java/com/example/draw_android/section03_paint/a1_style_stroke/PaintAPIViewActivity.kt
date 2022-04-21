package com.example.draw_android.section03_paint.a1_style_stroke

import android.annotation.SuppressLint
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.core.view.WindowCompat
import com.example.draw_android.R
import com.example.draw_android.databinding.ActivityPaintApiviewBinding

class PaintAPIViewActivity : AppCompatActivity() {
    private lateinit var mBinding: ActivityPaintApiviewBinding

    @SuppressLint("NewApi")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        WindowCompat.setDecorFitsSystemWindows(window, false)
        setContentView(R.layout.activity_paint_apiview)
    }
}