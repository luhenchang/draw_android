package com.example.draw_android.section05_canvas_clip.a_canvas_clip

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.WindowCompat
import com.example.draw_android.R
import com.example.draw_android.section05_canvas_clip.b_canvas_clip_examples.BarChart
import com.example.draw_android.section05_canvas_clip.b_canvas_clip_examples.BarChart.BarInfo
import com.example.draw_android.section05_canvas_clip.b_canvas_clip_examples.EchartsCubicView
import com.example.draw_android.section05_canvas_clip.b_canvas_clip_examples.ProgressBallView
import com.example.draw_android.section05_canvas_clip.b_canvas_clip_examples.ProgressBarView
import java.util.Random


class ClipCanvasMainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        WindowCompat.setDecorFitsSystemWindows(window, false)
        setContentView(R.layout.activity_clip_canvas_main)
        val echartsCubicView = findViewById<EchartsCubicView>(R.id.echarts_cubic)
        val progressBar = findViewById<ProgressBarView>(R.id.progressBar)
        progressBar.setProgressListener {
            echartsCubicView.setCubicCircle(it)
        }
        progressBar.setOnClickListener {
            progressBar.setProgressScale(0.8f)
        }

        val progressBar1 = findViewById<ProgressBarView>(R.id.progressBar2)
        progressBar1.setOnClickListener {
            progressBar1.setProgressScale(1f)
        }

        val progressBall = findViewById<ProgressBallView>(R.id.progressBall)
        progressBall.setOnClickListener {
            progressBall.setProgressScale(0.6f)
        }

        val barchart = findViewById<BarChart>(R.id.barChart)
        mRandom = Random()
        barchart.setBarInfoList(createBarInfo());barchart.setBarInfoList(createBarInfo())
    }
    private val DATA_COUNT_INTERVAL = 10
    private val DATA_COUNT_MAX = 300
    private val mDataCount = 100
    private var mRandom: Random? = null
    private fun createBarInfo(): List<BarInfo> {
        val data: MutableList<BarInfo> = ArrayList()
        for (i in 1..mDataCount) {
            data.add(BarInfo(i.toString() + "日", mRandom!!.nextFloat().toDouble()))
        }
        return data
    }
}