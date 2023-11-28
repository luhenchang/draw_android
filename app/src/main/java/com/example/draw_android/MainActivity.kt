package com.example.draw_android

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.draw_android.section05_canvas.f_event.EventMeasurePathView

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val recyclerView: RecyclerView = findViewById(R.id.rvView)
        val view = EventMeasurePathView(this)
        val itemList = arrayListOf(view)
        // 创建并设置适配器
        val adapter = CustomViewAdapter(itemList)
        recyclerView.setAdapter(adapter)
        // 设置布局管理器
        recyclerView.setLayoutManager(LinearLayoutManager(this))
    }
}