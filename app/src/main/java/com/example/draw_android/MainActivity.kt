package com.example.draw_android

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.draw_android.section05_canvas.f_event.EventCanvas
import com.example.draw_android.section05_canvas.f_event.EventExampleView
import com.example.draw_android.section05_canvas.f_event.EventRotateView
import com.example.draw_android.section05_canvas.f_event.EventScaleCanvas
import com.example.draw_android.section05_canvas.f_event.EventXYCanvas

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val recyclerView: RecyclerView = findViewById(R.id.rvView)
        val eventCanvas = EventCanvas(this)
        val eventXYCanvas = EventXYCanvas(this)
        val eventScaleCanvas = EventScaleCanvas(this)
        val eventRotateCanvas = EventRotateView(this)
        val eventExample = EventExampleView(this)
        val itemList = arrayListOf(
            eventCanvas,
            eventXYCanvas,
            eventScaleCanvas,
            eventRotateCanvas,
            eventExample
        )
        // 创建并设置适配器
        val adapter = CustomViewAdapter(itemList)
        recyclerView.setAdapter(adapter)
        // 设置布局管理器
        recyclerView.setLayoutManager(LinearLayoutManager(this))
    }
}