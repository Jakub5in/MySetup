package com.example.mysetup

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView



class SettingsRecyclerAdapter (
    private val dataList: List<Setting>
    ): RecyclerView.Adapter<SettingsRecyclerAdapter.MyViewHolder>() {


    // ViewHolder class to hold views
    inner class MyViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private var tvName: TextView = itemView.findViewById(R.id.textView_setting_name)
        private var tvValue: TextView = itemView.findViewById(R.id.textView_setting_value)

        // Bind data to views
        fun bind(item: Setting) {
            tvName.text = item.name
            tvValue.text = item.value.toString()
        }
    }


    // Create new ViewHolders
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SettingsRecyclerAdapter.MyViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.recycler_setting, parent, false)
        return MyViewHolder(view)
    }


    // Bind data to ViewHolder
    override fun onBindViewHolder(holder: SettingsRecyclerAdapter.MyViewHolder, position: Int) {
        val currentItem = dataList[position]
        holder.bind(currentItem)
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
