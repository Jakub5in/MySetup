package com.example.mysetup

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView


class SetupRecyclerAdapter(
    private var setupList: List<Setup>):
    RecyclerView.Adapter<SetupRecyclerAdapter.MyViewHolder>() {

        private var filteredSetups: List<Setup> = setupList

        inner class MyViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
            var tv_design: TextView = itemView.findViewById(R.id.recycler_text)
            var image_wet: ImageView = itemView.findViewById(R.id.image_wetness)
            var image_rough: ImageView = itemView.findViewById(R.id.image_terrain)
            var image_climb: ImageView = itemView.findViewById(R.id.image_direction)

            fun bind(item: Bike) {
                tv_design.text = item.name
            }
        }


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

        if (filteredSetups.isEmpty()) {
            filteredSetups = setupList
        }

        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.recycler_setup, parent, false)
        return MyViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        if (position < filteredSetups.size) {
            val setup = filteredSetups[position]

            holder.image_wet.clearColorFilter()
            holder.image_rough.clearColorFilter()
            holder.image_climb.clearColorFilter()

            val currentItem = filteredSetups[position]
            holder.tv_design.text = currentItem.name
            when (setup.wetness) {
                0 -> holder.image_wet.setImageResource(R.drawable.transparent_water_drop_empty)
                1 -> holder.image_wet.setImageResource(R.drawable.transparent_water_drop_half)
                else -> holder.image_wet.setImageResource(R.drawable.transparent_water_drop_full)
            }
            when (setup.terrain) {
                0 -> holder.image_rough.setImageResource(R.drawable.transparent_mellow)
                1 -> holder.image_rough.setImageResource(R.drawable.transparent_mixed)
                else -> holder.image_rough.setImageResource(R.drawable.transparent_rough)
            }
            when (setup.uphills) {
                0 -> holder.image_climb.setImageResource(R.drawable.transparent_uphill)
                1 -> holder.image_climb.setImageResource(R.drawable.transparent_allmountain)
                else -> holder.image_climb.setImageResource(R.drawable.transparent_downhill)
            }
            holder.image_wet.setColorFilter(R.color.TextOnBackground)
            holder.image_rough.setColorFilter(R.color.TextOnBackground)
            holder.image_climb.setColorFilter(R.color.TextOnBackground)

        } else {
            Log.e("SetupRecyclerAdapter", "Index out of bounds: position = $position, size = ${filteredSetups.size}")
        }
    }


    override fun getItemCount(): Int {
        return filteredSetups.size
    }
}