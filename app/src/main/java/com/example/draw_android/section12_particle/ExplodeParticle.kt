package com.example.draw_android.section12_particle


data class ExplodeParticle(
    var radius: Float = 0f,//圆形粒子，大小
    var pointX: Float,//坐标x
    var pointY: Float,//坐标y
    var angle: Float,//角度
    var velocity: Float,//速度
    var acceleration: Float,//加速度
    var color: Int,//纹理、颜色
    var recycleDistance: Int,//超出最大距离回收。
    var t: Int//t记录运动的时间单位  秒相对于屏幕刷新很大了
)