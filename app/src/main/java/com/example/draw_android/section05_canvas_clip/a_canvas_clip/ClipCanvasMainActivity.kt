package com.example.draw_android.section05_canvas_clip.a_canvas_clip

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.core.view.WindowCompat
import com.example.draw_android.R
import com.example.draw_android.section05_canvas_clip.b_canvas_clip_examples.ProgressBallView
import com.example.draw_android.section05_canvas_clip.b_canvas_clip_examples.ProgressBarView

class ClipCanvasMainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        WindowCompat.setDecorFitsSystemWindows(window, false)
        setContentView(R.layout.activity_clip_canvas_main)
        val progressBar = findViewById<ProgressBarView>(R.id.progressBar)
        progressBar.setOnClickListener {
            progressBar.setProgressScale(0.8f)
        }

        val progressBar1 = findViewById<ProgressBarView>(R.id.progressBar2)
        progressBar1.setOnClickListener {
            progressBar1.setProgressScale(1f)
        }

        val progressBall = findViewById<ProgressBallView>(R.id.progressBall)
        progressBall.setOnClickListener {
            progressBall.setProgressScale(1f)
        }

    }
}