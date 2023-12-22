package com.example.mysetup

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView



// Adapter for Bike recycler
class BikeRecyclerAdapter(
    private val dataList: List<Bike>
    ): RecyclerView.Adapter<BikeRecyclerAdapter.MyViewHolder>() {
    var onItemClickListener: OnItemClickListener? = null


    // ViewHolder class to hold views
    inner class MyViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private var tvDesign: TextView = itemView.findViewById(R.id.RecyclerBike_TextView_BikeName)

        // Bind data to views
        fun bind(item: Bike) {
            tvDesign.text = item.name
        }
    }


    // Interface for item click handling
    interface OnItemClickListener {
        fun onItemClick(item: Bike)
    }


    // Create new ViewHolders
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.recycler_bike, parent, false)
        return MyViewHolder(view)
    }


    // Bind data to ViewHolder
    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val currentItem = dataList[position]
        holder.bind(currentItem)

        // Set click listener for item
        holder.itemView.setOnClickListener {
            onItemClickListener?.onItemClick(currentItem)
        }
    }


    // Return number of items in the data set
    override fun getItemCount(): Int {
        return dataList.size
    }


    // Return item ID
    override fun getItemId(position: Int): Long {
        return position.toLong()
    }


    // Return item view type
    override fun getItemViewType(position: Int): Int {
        return position
    }
}
