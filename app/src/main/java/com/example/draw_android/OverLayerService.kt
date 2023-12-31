package com.example.draw_android

import android.app.Service
import android.content.Intent
import android.os.IBinder
import android.provider.Settings
import android.util.Log
import com.example.draw_android.section05_canvas.TitleTextWindow

class OverLayerService : Service() {
    override fun onBind(intent: Intent?): IBinder? {
        return null
    }

    override fun onCreate() {
        super.onCreate()
        Log.e("OverLayerService::","onCreate")
        if (Settings.canDrawOverlays(this)) {
            val textWindow = TitleTextWindow(this)
            textWindow.show()
        }
    }
}