package com.example.draw_android

import android.Manifest
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.Rect
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.provider.Settings
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.ViewStub
import android.widget.LinearLayout
import android.widget.TextView
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.OnScrollListener
import com.example.draw_android.section05_canvas.e_curve.CurveExampleView
import com.example.draw_android.section05_canvas.e_curve.CurveView
import com.example.draw_android.section05_canvas.f_event.BitmapClippingView
import com.example.draw_android.section05_canvas.f_event.EventCanvas
import com.example.draw_android.section05_canvas.f_event.EventExampleView
import com.example.draw_android.section05_canvas.f_event.EventRotateView
import com.example.draw_android.section05_canvas.f_event.EventScaleCanvas
import com.example.draw_android.section05_canvas.f_event.EventXYCanvas
import com.example.draw_android.section05_canvas.f_event.PageTurnView
import com.example.draw_android.section05_canvas.f_event.PorterDuffDstOutView
import com.example.draw_android.section05_canvas.g_clip.CircleImageView
import com.example.draw_android.section12_particle.CustomGestureExplodeView
import com.example.draw_android.section16_line_chart.TaskManagement
import com.example.draw_android.section16_line_chart.objectMapper
import java.io.File
import java.io.FileOutputStream
import java.io.IOException


class MainActivity : AppCompatActivity() {
    private val requestReadMediaPermissionLauncher =
        registerForActivityResult(ActivityResultContracts.RequestMultiplePermissions()) { _ ->

        }
    lateinit var mRecyclerView: RecyclerView
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val taskManagement = TaskManagement(id = 1, taskName = "Example Task")
        val jsonString = objectMapper.writeValueAsString(taskManagement)
        println(jsonString)
        val viewSub :ViewStub
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M && !Settings.canDrawOverlays(this)) {
            val intent = Intent(
                Settings.ACTION_MANAGE_OVERLAY_PERMISSION,
                Uri.parse("package:" + this.packageName)
            )
            //this.startActivity(intent)
        }

        mRecyclerView = findViewById<RecyclerView>(R.id.rv)
        mRecyclerView.setLayoutManager(LinearLayoutManager(this))
        mRecyclerView.setAdapter(MyAdapter())
        mRecyclerView.addOnScrollListener(object : OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)
                Log.e("onScrolled:dy=", dy.toString())
            }
        })
        mRecyclerView.setOnScrollChangeListener { v, scrollX, scrollY, oldScrollX, oldScrollY ->
            Log.e("OnScrollChange:dy=", scrollY.toString())
        }
        mRecyclerView.setMaxFlingVelocity(1000)
        val topView: LinearLayout = findViewById(R.id.topView)
        topView.post {
            val rect = Rect()
            topView.getLocalVisibleRect(rect)
            Log.e("topView:Top=", topView.top.toString())
            Log.e("topView:Bottom=", topView.bottom.toString())
            Log.e("topView:height=", topView.height.toString())
            Log.e("topView:Bottom=", topView.bottom.toString())
            Log.e("topView:Left=", topView.left.toString())
            Log.e("topView:right=", topView.right.toString())
            Log.e("topView:width=", topView.width.toString())
            val rectLx = Rect(0, 0, 1080, 90)
//            File(externalCacheDir, "demo.jpg").path
//            ClipImageMainActivity.prepare()
//                .aspectX(3).aspectY(2)
//                .inputPath("").outputPath("")
//                .startForResult(this, 10001)
        }
        requestReadMediaPermissionLauncher.launch(
            arrayOf(
                Manifest.permission.READ_EXTERNAL_STORAGE,
                Manifest.permission.WRITE_EXTERNAL_STORAGE
            )
        )

        val recyclerView: RecyclerView = findViewById(R.id.rvView)
        val eventCanvas = EventCanvas(this)
        val eventXYCanvas = EventXYCanvas(this)
        val eventScaleCanvas = EventScaleCanvas(this)
        val eventRotateCanvas = EventRotateView(this)
        val pageTurnView = PageTurnView(this)
        val eventExample = EventExampleView(this)
        val eventPorterDuffDstOutView = PorterDuffDstOutView(this)
        val eventBitmap = BitmapClippingView(this)
        val curveView = CurveView(this)
        val curveViewExample = CurveExampleView(this)
        val clipAPIStudyView = CircleImageView(this)


        val itemList = arrayListOf(
            eventCanvas,
            eventXYCanvas,
            eventScaleCanvas,
            eventRotateCanvas,
            eventPorterDuffDstOutView,
            pageTurnView,
            eventExample,
            eventBitmap,
            curveView,
            curveViewExample,
            clipAPIStudyView
        )
        // 创建并设置适配器
        val adapter = CustomViewAdapter(itemList)
        recyclerView.setAdapter(adapter)
        // 设置布局管理器
        recyclerView.setLayoutManager(LinearLayoutManager(this))

        recyclerView.postDelayed({
            screenCaptureNoStatusBar()
        }, 6000)

        val imgView = findViewById<CustomGestureExplodeView>(R.id.loadProgress)
        // 开启一个线程，模拟进度更新
        Thread {
            var progress = 0
            while (progress <= 100) {
                // 更新进度条
                handler.post {
                    imgView.setProgress(progress)
                }

                // 模拟耗时操作
                try {
                    Thread.sleep(100) // 延迟100毫秒
                } catch (e: InterruptedException) {
                    e.printStackTrace()
                }

                progress++ // 增加进度
            }
        }.start()

       val pieChartView = findViewById<MyPieChartView>(R.id.pie_chart)

        pieChartView.addItemType(MyPieChartView.ItemType("小米", 13f, -0x74a600))
        pieChartView.addItemType(MyPieChartView.ItemType("华为", 23f, -0x606000))
        pieChartView.addItemType(MyPieChartView.ItemType("苹果", 43f, -0x808400))
        pieChartView.addItemType(MyPieChartView.ItemType("Net", 93f, -0x805700))
        pieChartView.addItemType(MyPieChartView.ItemType("AirSp", 33f, -0x208400))
    }
    private val handler = Handler(Looper.getMainLooper())
    override fun onResume() {
        super.onResume()
        startForegroundService(Intent(this, OverLayerService::class.java))
    }

    /**
     * 截取当前可见范围屏幕（不包含状态栏）
     */
    private fun screenCaptureNoStatusBar() {
        val view = window.decorView
        view.setDrawingCacheEnabled(true)
        view.buildDrawingCache()

        // 获取状态栏高度
        val rect = Rect()
        view.getWindowVisibleDisplayFrame(rect)
        val statusBarH: Int = rect.top
        // 获取屏幕宽高
        val w = view.width
        val h = view.height
        Log.e("screen:width=", w.toString())//1080
        Log.e("screen:height=", (h - statusBarH).toString())//2352


        // 去掉状态栏
        val bitmap = Bitmap.createBitmap(view.drawingCache, 0, statusBarH, w, h - statusBarH)
        // 销毁缓存信息
        view.destroyDrawingCache()
        updateImageCapture(bitmap)
    }

    private fun updateImageCapture(bitmap: Bitmap) {
        // 指定文件路径和名称
        val filePath =
            getExternalFilesDir(null)?.absolutePath + "/cap_images.jpg"
        Log.e("filePath==", filePath.toString())
        val file = File(filePath)
        // 创建输出流
        var outputStream: FileOutputStream? = null
        try {
            // 打开文件输出流
            outputStream = FileOutputStream(file)

            // 将Bitmap压缩并写入输出流
            // 第一个参数是压缩的格式，第二个参数是压缩质量，第三个参数是输出流
            // 这里将Bitmap以JPEG格式写入，可以根据需要选择其他格式
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, outputStream)

            // 刷新输出流
            outputStream.flush()

            // 文件保存成功
        } catch (e: IOException) {
            Log.e("e::", e.message.toString())
            e.printStackTrace()
            // 文件保存失败
        } finally {
            // 确保关闭输出流
            try {
                outputStream?.close()
            } catch (e: IOException) {
                e.printStackTrace()
            }
        }
    }

    private class MyAdapter : RecyclerView.Adapter<MyViewHolder>() {
        private val data = ArrayList<String>()

        init {
            for (i in 0..10000) {
                data.add("item index =$i")
            }
        }

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
            val itemView: View =
                LayoutInflater.from(parent.context).inflate(R.layout.item_text_view, parent, false)
            return MyViewHolder(itemView)
        }

        override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
            holder.textView.text = data[position]
        }

        override fun getItemCount(): Int {
            return data.size
        }
    }


    private class MyViewHolder internal constructor(itemView: View) :
        RecyclerView.ViewHolder(itemView) {
        var textView: TextView

        init {
            textView = itemView.findViewById<TextView>(R.id.textView)
        }
    }

    private fun RecyclerView.setMaxFlingVelocity(velocity: Int) {
        val field = this.javaClass.getDeclaredField("mMaxFlingVelocity")
        field.isAccessible = true
        field.set(this, velocity)
    }

}