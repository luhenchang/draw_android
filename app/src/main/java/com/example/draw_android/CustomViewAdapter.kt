package com.example.draw_android

import android.app.Activity
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.RelativeLayout
import androidx.recyclerview.widget.RecyclerView
import com.example.draw_android.section05_canvas.f_event.EventCanvasMainActivity
import com.example.draw_android.section05_canvas.f_event.EventDisallowInterceptListener


class CustomViewAdapter(itemList: List<View>) :
    RecyclerView.Adapter<CustomViewAdapter.ViewHolder>() {
    private val itemList: List<View>

    init {
        this.itemList = itemList
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view =
            LayoutInflater.from(parent.context).inflate(R.layout.custom_view_layout, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item: View = itemList[position]
        if (item.parent!=null){
            (item.parent as ViewGroup).removeView(item)
        }
        holder.view.addView(item)
        var checkBox = false
        holder.checkInterButton.setOnClickListener {
            if (item is EventDisallowInterceptListener) {
                checkBox = !checkBox
                item.requestDisallowInterceptTouchEvent(checkBox)
            }
        }
        holder.buttonView.setOnClickListener {
            holder.buttonView.context.startActivity(
                Intent(
                    holder.view.context,
                    EventCanvasMainActivity::class.java
                ).apply {
                    putExtra("nameValue", position)
                }
            )
        }
    }

    override fun getItemCount(): Int {
        return itemList.size
    }

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var view: RelativeLayout
        var buttonView: Button
        var checkInterButton: ImageView


        init {
            view = itemView.findViewById(R.id.viewLayout)
            buttonView = itemView.findViewById(R.id.buttonView)
            checkInterButton = itemView.findViewById(R.id.checkInterButton)
        }
    }
}

