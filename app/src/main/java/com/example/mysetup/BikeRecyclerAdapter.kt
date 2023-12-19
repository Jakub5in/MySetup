package com.example.mysetup

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class BikeRecyclerAdapter(
    private val dataList: List<Bike>):
    RecyclerView.Adapter<BikeRecyclerAdapter.MyViewHolder>() {

        inner class MyViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
            var tv_design: TextView = itemView.findViewById(R.id.recycler_text)

            fun bind(item: Bike) {
                tv_design.text = item.name
            }
        }

    // Running onItemClick from MainActivity when pressed
    interface OnItemClickListener {
        fun onItemClick(item: Bike)
    }

    var onItemClickListener: OnItemClickListener? = null
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.recycler, parent, false)
        return MyViewHolder(view)
    }

    // Initial setup of RecyclerData
    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val currentItem = dataList[position]
        holder.bind(currentItem)

        holder.itemView.setOnClickListener {
            onItemClickListener?.onItemClick(currentItem)
        }
    }

    // Getter for RecyclerData Id
    override fun getItemId(position: Int): Long {
        return position.toLong()
    }

    override fun getItemViewType(position: Int): Int {
        return position
    }

    // Getter for dataList size
    override fun getItemCount(): Int {
        return dataList.size
    }

}