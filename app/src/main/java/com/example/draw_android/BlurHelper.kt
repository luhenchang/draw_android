package com.example.draw_android

import android.content.Context
import android.graphics.Bitmap
import android.renderscript.Allocation
import android.renderscript.RenderScript
import android.renderscript.ScriptIntrinsicBlur

object BlurHelper {
    fun blurBitmap(context: Context?, inputBitmap: Bitmap, radius: Float): Bitmap {
        val outputBitmap = Bitmap.createBitmap(
            inputBitmap.getWidth(),
            inputBitmap.getHeight(),
            Bitmap.Config.ARGB_8888
        )
        val renderScript = RenderScript.create(context)
        val inputAllocation = Allocation.createFromBitmap(renderScript, inputBitmap)
        val outputAllocation = Allocation.createFromBitmap(renderScript, outputBitmap)
        val blurScript = ScriptIntrinsicBlur.create(renderScript, inputAllocation.element)
        blurScript.setRadius(radius)
        blurScript.setInput(inputAllocation)
        blurScript.forEach(outputAllocation)
        outputAllocation.copyTo(outputBitmap)
        inputAllocation.destroy()
        outputAllocation.destroy()
        blurScript.destroy()
        renderScript.destroy()
        return outputBitmap
    }
}
