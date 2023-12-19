package com.example.mysetup

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.res.Configuration
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Gravity
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.PopupWindow
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView

// TODO:
//  Class for bike settings
//  MutableList of this class in RecyclerData
//  Finishing activity_bike.xml, to show bike setting List as recyclerView
//  Filtering options

class MainActivity : AppCompatActivity(), BikeRecyclerAdapter.OnItemClickListener {
    private lateinit var recyclerView: RecyclerView
    private lateinit var recyclerAdapter: BikeRecyclerAdapter
    private var dataList = mutableListOf<Bike>()
    private val  spm = SPManager(this)
    private val REQUEST_CODE_BIKE_ACTIVITY = 1

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        recyclerView = findViewById(R.id.rv_design)
        recyclerView.layoutManager = GridLayoutManager(applicationContext, 1)

        // Deserializing list
        dataList = spm.getList()

        recyclerAdapter = BikeRecyclerAdapter(dataList)
        recyclerAdapter.onItemClickListener = this
        recyclerView.adapter = recyclerAdapter

        // Checking if value is passed and if so, removing it from list
        if (intent.hasExtra("remove")) {
            val receivedItem = intent?.getSerializableExtra("remove") as Bike
            dataList.remove(receivedItem)
        }


        // Setting add new button behaviour
        val buttonShowPopup = findViewById<Button>(R.id.button2)
        buttonShowPopup.setOnClickListener {
            addNew()
        }

        if(isNightModeEnabled()){
            R.color.TextOnBackground = Color.WHITE
        }else{
            R.color.TextOnBackground = Color.BLACK
        }

    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (resultCode == Activity.RESULT_OK) {
            val updatedBike = data?.getSerializableExtra("updatedBike") as? Bike
            updatedBike?.let {
                val index = dataList.indexOfFirst { it.name == updatedBike.name }
                if (index != -1) {
                    dataList[index] = updatedBike
                    recyclerAdapter.notifyItemChanged(index)
                }
            }
        }
    }

    // Deserialization on app closure
    override fun onStop() {
        super.onStop()
        spm.saveList(dataList)
    }

    // Passing clicked RecyclerData to BikeActivity and opening it
    override fun onItemClick(item: Bike) {
        val intent = Intent(this, BikeActivity::class.java)
        intent.putExtra("key", item)
        startActivityForResult(intent, REQUEST_CODE_BIKE_ACTIVITY)
    }

    // Button behaviour function
    private fun addNew() {
        val inflater = getSystemService(LAYOUT_INFLATER_SERVICE) as LayoutInflater
        val view = inflater.inflate(R.layout.popup_add_new, null)
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

        val editText = view.findViewById<EditText>(R.id.editText)

        // Popup window buttons
        val buttonAdd = view.findViewById<Button>(R.id.button_add)
        buttonAdd.setOnClickListener {
            val enteredName = editText.text.toString()
            // Adding new RecyclerData to list
            dataList.add(Bike(enteredName, false))
            popupWindow.dismiss()
        }

        val buttonCancel = view.findViewById<Button>(R.id.button_cancel)
        buttonCancel.setOnClickListener {
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

    private fun isNightModeEnabled(): Boolean {
        val currentNightMode = resources.configuration.uiMode and Configuration.UI_MODE_NIGHT_MASK
        return currentNightMode == Configuration.UI_MODE_NIGHT_YES
    }
}