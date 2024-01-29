package com.example.clip_img

import java.io.Closeable
import java.io.File
import java.io.IOException

/**
 * @author 黄浩杭 (huanghaohang@parkingwang.com)
 * @version 2016-01-18
 * @since 2016-01-18
 */
object IOUtils {
    fun close(c: Closeable?) {
        if (c != null) {
            try {
                c.close()
            } catch (e: IOException) {
                e.printStackTrace()
            }
        }
    }

    fun deleteFile(path: String?): Boolean {
        val file = File(path)
        return file.exists() && file.isFile() && file.delete()
    }
}
