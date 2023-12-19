package com.example.mysetup

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.Gravity
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.PopupWindow
import android.widget.SeekBar
import android.widget.Switch
import android.widget.TextView
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView

class BikeActivity : AppCompatActivity() {
    private lateinit var receivedItem: Bike
    private lateinit var filteredSetups: MutableList<Setup>
    lateinit var recyclerView: RecyclerView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_bike)

        // Receiving Bike data class passed from MainActivity
        val receivedIntent = intent
        receivedItem = receivedIntent.getSerializableExtra("key") as Bike

        // Setting text displayed at the top to the name from data class
        val bikeName: TextView = findViewById(R.id.bikeName)
        bikeName.text = receivedItem.name

        // Setting delete button behaviour
        val buttonDelete = findViewById<Button>(R.id.button_delete)
        buttonDelete.setBackgroundColor(getThemeColor(this, android.R.attr.colorAccent, 1.0f))
        buttonDelete.setTextColor(Color.BLACK)
        buttonDelete.setOnClickListener {
            delete()
        }

        // Setting add new button behaviour
        val buttonShowPopupAddNew = findViewById<Button>(R.id.button_add_setup)
        buttonShowPopupAddNew.setOnClickListener {
            addNew()
        }

        // Setting add new button behaviour
        val buttonShowPopupFilter = findViewById<Button>(R.id.button_filter)
        buttonShowPopupFilter.setOnClickListener {
            filter()
        }

        recyclerView = findViewById(com.example.mysetup.R.id.recycler_setups)
        recyclerView.layoutManager = GridLayoutManager(applicationContext, 1)
        recyclerView.adapter = if(receivedItem.setups != null) {
            SetupRecyclerAdapter(receivedItem.setups)
        }else{
            SetupRecyclerAdapter(emptyList())
        }


    }

    // Button behaviour function
    private fun delete() {
        val inflater = getSystemService(LAYOUT_INFLATER_SERVICE) as LayoutInflater
        val view = inflater.inflate(R.layout.popup_delete, null)
        view.setBackgroundColor(getThemeColor(this, android.R.attr.colorBackground, 0.7f)) // Setting semitransparent background to popup

        val popupDelete = PopupWindow(
            view,
            ViewGroup.LayoutParams.WRAP_CONTENT,
            ViewGroup.LayoutParams.WRAP_CONTENT,
            true
        )

        popupDelete.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        popupDelete.inputMethodMode = PopupWindow.INPUT_METHOD_NEEDED
        popupDelete.showAtLocation(view, Gravity.CENTER, 0, 0)

        // Popup window buttons
        val buttonYes = view.findViewById<Button>(R.id.button_yes)
        buttonYes.setOnClickListener {
            popupDelete.dismiss()
            // Passing the object back to MainActivity, so it can be removed from List
            val intent = Intent(this, MainActivity::class.java).apply{
                putExtra("remove", receivedItem)
            }
            intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP
            startActivity(intent)
            finish()
        }

        val buttonCancel = view.findViewById<Button>(R.id.button_cancel)
        buttonCancel.setOnClickListener {
            popupDelete.dismiss()
        }
    }

    override fun onBackPressed() {
        val intent = Intent()
        intent.putExtra("updatedBike", receivedItem)
        setResult(Activity.RESULT_OK, intent)

        super.onBackPressed()
    }

    private fun addNew() {
        val inflater = getSystemService(LAYOUT_INFLATER_SERVICE) as LayoutInflater
        val view = inflater.inflate(R.layout.popup_add_new_setup, null)
        view.setBackgroundColor(getThemeColor(this, android.R.attr.colorBackground, 0.7f)) // Setting semitransparent background to popup

        val popupWindow = PopupWindow(
            view,
            ViewGroup.LayoutParams.WRAP_CONTENT,
            ViewGroup.LayoutParams.WRAP_CONTENT,
            true
        )

        popupWindow.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        popupWindow.inputMethodMode = PopupWindow.INPUT_METHOD_NEEDED
        popupWindow.showAtLocation(view, Gravity.CENTER, 0, 0)

        view.findViewById<ImageView>(R.id.image_uphill).setColorFilter(R.color.TextOnBackground)
        view.findViewById<ImageView>(R.id.image_allmountain).setColorFilter(R.color.TextOnBackground)
        view.findViewById<ImageView>(R.id.image_downhill).setColorFilter(R.color.TextOnBackground)
        view.findViewById<ImageView>(R.id.image_wet).setColorFilter(R.color.TextOnBackground)
        view.findViewById<ImageView>(R.id.image_semiwet).setColorFilter(R.color.TextOnBackground)
        view.findViewById<ImageView>(R.id.image_dry).setColorFilter(R.color.TextOnBackground)
        view.findViewById<ImageView>(R.id.image_mellow).setColorFilter(R.color.TextOnBackground)
        view.findViewById<ImageView>(R.id.image_mixed).setColorFilter(R.color.TextOnBackground)
        view.findViewById<ImageView>(R.id.image_rough).setColorFilter(R.color.TextOnBackground)

        val editText = view.findViewById<EditText>(R.id.editText)
        val seekBarWetness = view.findViewById<SeekBar>(R.id.seekBar_wetness)
        val seekBarUphill = view.findViewById<SeekBar>(R.id.seekBar_uphills)
        val seekBarTerrain = view.findViewById<SeekBar>(R.id.seekBar_terrain)

        // Popup window buttons
        val buttonAdd = view.findViewById<Button>(R.id.button_add_setup_popup)
        buttonAdd.setOnClickListener {
            val enteredName = editText.text.toString()
            val wetness = seekBarWetness.progress
            val uphill = seekBarUphill.progress
            val terrain = seekBarTerrain.progress

            // Adding new RecyclerData to list
            if (receivedItem.setups == null) {
                receivedItem.setups = mutableListOf()
            }

            receivedItem.setups.add(Setup(enteredName, wetness, uphill, terrain))
            recyclerView.adapter?.notifyDataSetChanged()
            popupWindow.dismiss()
        }

        val buttonCancel = view.findViewById<Button>(R.id.button_cancel_setup_popup)
        buttonCancel.setOnClickListener {
            popupWindow.dismiss()
        }
    }

    private fun filter() {
        val inflater = getSystemService(LAYOUT_INFLATER_SERVICE) as LayoutInflater
        val view = inflater.inflate(R.layout.popup_filter_setup, null)
        view.setBackgroundColor(getThemeColor(this, android.R.attr.colorBackground, 0.9f)) // Setting semitransparent background to popup

        val popupWindow = PopupWindow(
            view,
            ViewGroup.LayoutParams.WRAP_CONTENT,
            ViewGroup.LayoutParams.WRAP_CONTENT,
            true
        )

        popupWindow.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        popupWindow.inputMethodMode = PopupWindow.INPUT_METHOD_NEEDED
        popupWindow.showAtLocation(view, Gravity.CENTER, 0, 0)

        view.findViewById<ImageView>(R.id.image_uphill2).setColorFilter(R.color.TextOnBackground)
        view.findViewById<ImageView>(R.id.image_allmountain2).setColorFilter(R.color.TextOnBackground)
        view.findViewById<ImageView>(R.id.image_downhill2).setColorFilter(R.color.TextOnBackground)
        view.findViewById<ImageView>(R.id.image_wet2).setColorFilter(R.color.TextOnBackground)
        view.findViewById<ImageView>(R.id.image_semiwet2).setColorFilter(R.color.TextOnBackground)
        view.findViewById<ImageView>(R.id.image_dry2).setColorFilter(R.color.TextOnBackground)
        view.findViewById<ImageView>(R.id.image_mellow2).setColorFilter(R.color.TextOnBackground)
        view.findViewById<ImageView>(R.id.image_mixed2).setColorFilter(R.color.TextOnBackground)
        view.findViewById<ImageView>(R.id.image_rough2).setColorFilter(R.color.TextOnBackground)

        val seekBarWetness = view.findViewById<SeekBar>(R.id.seekBar_wetness_filter)
        val seekBarUphill = view.findViewById<SeekBar>(R.id.seekBar_slope_filter)
        val seekBarTerrain = view.findViewById<SeekBar>(R.id.seekBar_roughness_filter)
        val switchWetness = view.findViewById<Switch>(R.id.switch_wetness_filter)
        val switchUphill = view.findViewById<Switch>(R.id.switch_slope_filter)
        val switchTerrain = view.findViewById<Switch>(R.id.switch_roughness_filter)

        // Popup window buttons
        val buttonAdd = view.findViewById<Button>(R.id.button_filter_accept)
        buttonAdd.setOnClickListener {
            var wetness = -1
            var uphill = -1
            var terrain = -1

            if (switchWetness.isChecked) {
                wetness = seekBarWetness.progress
            }
            if (switchUphill.isChecked) {
                uphill = seekBarUphill.progress
            }
            if (switchTerrain.isChecked) {
                terrain = seekBarTerrain.progress
            }

            Log.d("Filter", "Before setFilter - Wetness: $wetness, Uphill: $uphill, Terrain: $terrain")

            (recyclerView.adapter as? SetupRecyclerAdapter)?.setFilter(wetness, uphill, terrain)

            Log.d("Filter", "After setFilter - Wetness: $wetness, Uphill: $uphill, Terrain: $terrain")

            popupWindow.dismiss()
        }
    }

    // Getter for theme colors
    private fun getThemeColor(context: Context, attr: Int, alpha: Float): Int {
        val typedArray = context.obtainStyledAttributes(intArrayOf(attr))
        val color = typedArray.getColor(0, 0)
        typedArray.recycle()

        val alphaComponent = (Color.alpha(color) * alpha).toInt()
        return Color.argb(alphaComponent, Color.red(color), Color.green(color), Color.blue(color))
    }
}
