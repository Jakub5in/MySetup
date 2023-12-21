package com.example.mysetup

import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.appcompat.widget.SwitchCompat
import androidx.recyclerview.widget.RecyclerView
import java.lang.NumberFormatException


class SettingsEditRecyclerAdapter (
    private val dataList: List<Setting>
): RecyclerView.Adapter<SettingsEditRecyclerAdapter.MyViewHolder>() {

    // ViewHolder class to hold views
    inner class MyViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private var tvName: TextView = itemView.findViewById(R.id.textView_setting_name)
        private var editValue: TextView = itemView.findViewById(R.id.edit_setting_value)
        private var switchUse: SwitchCompat = itemView.findViewById(R.id.switch_setting)

        private lateinit var settingItem: Setting


        // Bind data to views
        fun bind(item: Setting) {
            settingItem = item
            tvName.text = item.name
            editValue.text = item.value.toString()

            editValue.addTextChangedListener(object: TextWatcher {
                override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                }

                override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                }

                override fun afterTextChanged(s: Editable?) {
                    textChanged(s.toString())
                }
            })
            switchUse.setOnClickListener{useChanged()}
            switchUse.isChecked = item.isSet
        }


        private fun useChanged() {
            settingItem.isSet = !settingItem.isSet
        }

        private fun textChanged(s: String) {
            val sInt: Int = try {
                s.toInt()
            } catch (e: NumberFormatException) {
                0
            }
            settingItem.value = sInt
        }
    }


    // Create new ViewHolders
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SettingsEditRecyclerAdapter.MyViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.recycler_setting_edit, parent, false)
        return MyViewHolder(view)
    }


    // Bind data to ViewHolder
    override fun onBindViewHolder(holder: SettingsEditRecyclerAdapter.MyViewHolder, position: Int) {
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

    fun getSettings(): MutableList<Setting> {
        return dataList.toMutableList()
    }

}
