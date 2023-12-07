package com.example.draw_android

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.draw_android.section05_canvas.f_event.BitmapClippingView
import com.example.draw_android.section05_canvas.f_event.EventCanvas
import com.example.draw_android.section05_canvas.f_event.EventExampleView
import com.example.draw_android.section05_canvas.f_event.EventRotateView
import com.example.draw_android.section05_canvas.f_event.EventScaleCanvas
import com.example.draw_android.section05_canvas.f_event.EventXYCanvas
import com.example.draw_android.section05_canvas.f_event.PageTurnView
import com.example.draw_android.section05_canvas.f_event.PorterDuffDstOutView

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val recyclerView: RecyclerView = findViewById(R.id.rvView)
        val eventCanvas = EventCanvas(this)
        val eventXYCanvas = EventXYCanvas(this)
        val eventScaleCanvas = EventScaleCanvas(this)
        val eventRotateCanvas = EventRotateView(this)
        val pageTurnView = PageTurnView(this)
        val eventExample = EventExampleView(this)
        val eventPorterDuffDstOutView = PorterDuffDstOutView(this)
        val eventBitmap = BitmapClippingView(this)
        val itemList = arrayListOf(
            eventCanvas,
            eventXYCanvas,
            eventScaleCanvas,
            eventRotateCanvas,
            eventPorterDuffDstOutView,
            pageTurnView,
            eventExample,
            eventBitmap
        )
        // 创建并设置适配器
        val adapter = CustomViewAdapter(itemList)
        recyclerView.setAdapter(adapter)
        // 设置布局管理器
        recyclerView.setLayoutManager(LinearLayoutManager(this))
    }
}