package com.example.mysetup

import android.content.Context
import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
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
        }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        var view = LayoutInflater.from(parent.context).inflate(R.layout.recycler, parent, false)
        return MyViewHolder(view)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        holder.tv_design.setText(dataList[position].name)
        holder.itemView.setOnClickListener{
            if(dataList[position].isSelected) {
                dataList[position].setIsSelected(false)
                holder.ll_design.setBackgroundColor(Color.rgb(13, 19, 33))
            }else{
                dataList[position].setIsSelected(true)
                holder.ll_design.setBackgroundColor(Color.rgb(29, 45, 68))
            }
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