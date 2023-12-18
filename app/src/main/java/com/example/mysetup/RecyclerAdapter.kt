package com.example.mysetup

import android.content.Context
import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class RecyclerAdapter(
    val context: Context,
    private val dataList: List<RecyclerData>):
    RecyclerView.Adapter<RecyclerAdapter.MyViewHolder>() {

        inner class MyViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
            var ll_design: LinearLayout = itemView.findViewById(R.id.ll_design)
            var tv_design: TextView = itemView.findViewById(R.id.tv_design)

            fun bind(item: RecyclerData) {
                tv_design.text = item.name
                ll_design.setBackgroundColor(Color.rgb(29, 45, 68))
            }
        }

    interface OnItemClickListener {
        fun onItemClick(item: RecyclerData)
    }

    var onItemClickListener: OnItemClickListener? = null
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.recycler, parent, false)
        return MyViewHolder(view)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val currentItem = dataList[position]
        holder.bind(currentItem)

        holder.itemView.setOnClickListener {
            onItemClickListener?.onItemClick(currentItem)
        }
    }

    override fun getItemId(position: Int): Long {
        return position.toLong()
    }

    override fun getItemViewType(position: Int): Int {
        return position
    }

    override fun getItemCount(): Int {
        return dataList.size
    }

}