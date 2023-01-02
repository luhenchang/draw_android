package com.example.draw_android.section05_canvas.d_save_restore

import java.io.Serializable

class Path : android.graphics.Path(),Serializable
/**
 * Created by wangfei44 on 2022/4/22.
 */
class ViewSerializableObj constructor(var path: Path, var key:String) : Serializable