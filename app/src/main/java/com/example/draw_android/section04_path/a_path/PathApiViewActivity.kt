package com.example.draw_android.section04_path.a_path

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.WindowCompat
import com.example.draw_android.R

/**
 * Created by wangfei44 on 2022/12/30.
 */
class PathApiViewActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        WindowCompat.setDecorFitsSystemWindows(window, false)
        setContentView(R.layout.activity_path_api_view)
    }
}