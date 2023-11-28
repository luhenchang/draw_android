package com.example.draw_android

import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.RelativeLayout
import androidx.recyclerview.widget.RecyclerView
import com.example.draw_android.section05_canvas.f_event.EventCanvasMainActivity


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
        holder.view.addView(item)
    }

    override fun getItemCount(): Int {
        return itemList.size
    }

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var view: RelativeLayout
        private var buttonView: Button

        init {
            view = itemView.findViewById(R.id.viewLayout)
            buttonView = itemView.findViewById(R.id.buttonView)

            buttonView.setOnClickListener {
                buttonView.context.startActivity(
                    Intent(
                        view.context,
                        EventCanvasMainActivity::class.java
                    )
                )
            }
        }
    }
}

