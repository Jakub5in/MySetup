package com.example.mysetup

import android.content.Context
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.text.Editable
import android.text.TextWatcher
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.PopupWindow
import android.widget.TextView
import androidx.appcompat.widget.SwitchCompat
import androidx.recyclerview.widget.RecyclerView
import java.lang.NumberFormatException


class SettingsEditRecyclerAdapter (
    private val context: Context,
    private val dataList: List<Setting>
): RecyclerView.Adapter<SettingsEditRecyclerAdapter.MyViewHolder>() {

    // ViewHolder class to hold views
    inner class MyViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private var tvName: TextView = itemView.findViewById(R.id.RecyclerSettingEdit_TextView_SettingName)
        private var editValue: TextView = itemView.findViewById(R.id.RecyclerSettingEdit_EditText_SettingValue)
        private var switchUse: SwitchCompat = itemView.findViewById(R.id.RecyclerSettingEdit_SwitchCompat_SwitchSetting)
        private var buttonHelp: Button = itemView.findViewById(R.id.RecyclerSettingEdit_Button_Help)

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

            buttonHelp.setOnClickListener{viewHelp(item)}
        }


        private fun viewHelp(item: Setting) {
            val popupView = LayoutInflater.from(context).inflate(R.layout.popup_help, null)

            val popupWindow = PopupWindow(
                popupView,
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.MATCH_PARENT,
                true
            )

            // Make popup background semitransparent
            popupView.setBackgroundColor(getThemeColor(context, android.R.attr.colorBackground, 0.9f))
            popupWindow.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
            popupWindow.inputMethodMode = PopupWindow.INPUT_METHOD_NEEDED
            popupWindow.showAtLocation(popupView, Gravity.CENTER, 0, 0)

            val head: TextView = popupView.findViewById(R.id.PopupHelp_TextView_SettingName)
            head.text = item.name

            val body: TextView = popupView.findViewById(R.id.PopupHelp_TextView_SettingHelp)
            body.text = item.help

            val buttonOk: Button = popupView.findViewById(R.id.PopupHelp_Button_Ok)
            buttonOk.setOnClickListener{
                popupWindow.dismiss()
            }
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


    // Return theme colors
    private fun getThemeColor(context: Context, attr: Int, alpha: Float = 1.0f): Int {
        val typedArray = context.obtainStyledAttributes(intArrayOf(attr))
        val color = typedArray.getColor(0, 0)
        typedArray.recycle()

        val alphaComponent = (Color.alpha(color) * alpha).toInt()
        return Color.argb(alphaComponent, Color.red(color), Color.green(color), Color.blue(color))
    }
}
