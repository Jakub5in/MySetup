package com.example.mysetup

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView


// Adapter for Setup recycler
// Adapter for Setup recycler
class SetupRecyclerAdapter(
    private var setupList: List<Setup>
): RecyclerView.Adapter<SetupRecyclerAdapter.MyViewHolder>() {
    var onItemClickListener: OnItemClickListener? = null

    // List to store filtered setups
    private var filteredSetups: List<Setup> = setupList


    // ViewHolder class to hold views
    inner class MyViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var tvDesign: TextView = itemView.findViewById(R.id.recycler_text)
        var imageWet: ImageView = itemView.findViewById(R.id.image_wetness)
        var imageRough: ImageView = itemView.findViewById(R.id.image_terrain)
        var imageClimb: ImageView = itemView.findViewById(R.id.image_direction)

        init {
            // Set click listener for each item
            itemView.setOnClickListener {
                val position = adapterPosition
                if (position != RecyclerView.NO_POSITION) {
                    onItemClickListener?.onItemClick(filteredSetups[position])
                }
            }
        }
    }


    // Interface for item click handling
    interface OnItemClickListener {
        fun onItemClick(item: Setup)
    }


    // Create new ViewHolders
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.recycler_setup, parent, false)
        return MyViewHolder(itemView)
    }


    // Bind data to ViewHolder
    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        if (position < filteredSetups.size) {
            val setup = filteredSetups[position]

            // Clear color filters
            holder.imageWet.clearColorFilter()
            holder.imageRough.clearColorFilter()
            holder.imageClimb.clearColorFilter()

            // Get current setup
            val currentItem = filteredSetups[position]

            // Bind data to views in the item
            holder.tvDesign.text = currentItem.name
            when (setup.wetness) {
                0 -> holder.imageWet.setImageResource(R.drawable.transparent_water_drop_empty)
                1 -> holder.imageWet.setImageResource(R.drawable.transparent_water_drop_half)
                else -> holder.imageWet.setImageResource(R.drawable.transparent_water_drop_full)
            }
            when (setup.terrain) {
                0 -> holder.imageRough.setImageResource(R.drawable.transparent_mellow)
                1 -> holder.imageRough.setImageResource(R.drawable.transparent_mixed)
                else -> holder.imageRough.setImageResource(R.drawable.transparent_rough)
            }
            when (setup.uphills) {
                0 -> holder.imageClimb.setImageResource(R.drawable.transparent_uphill)
                1 -> holder.imageClimb.setImageResource(R.drawable.transparent_allmountain)
                else -> holder.imageClimb.setImageResource(R.drawable.transparent_downhill)
            }

            // Apply color filter to images based on text color
            holder.imageWet.setColorFilter(R.color.TextOnBackground)
            holder.imageRough.setColorFilter(R.color.TextOnBackground)
            holder.imageClimb.setColorFilter(R.color.TextOnBackground)

        }
    }


    // Return number of items in the data set
    override fun getItemCount(): Int {
        return filteredSetups.size
    }


    // Filter setups based on wetness, uphill, and terrain
    fun setFilter(Wetness: Int, Uphill: Int, Terrain: Int) {
        filteredSetups = if (Wetness == -1 && Uphill == -1 && Terrain == -1) {
            setupList
        } else {
            setupList.filter { setup ->
                (Wetness == -1 || setup.wetness == Wetness) &&
                        (Uphill == -1 || setup.uphills == Uphill) &&
                        (Terrain == -1 || setup.terrain == Terrain)
            }
        }

        // If no setups match the filter, show all setups
        if (filteredSetups.isEmpty()) {
            filteredSetups = setupList
        }

        notifyDataSetChanged()
    }
}
