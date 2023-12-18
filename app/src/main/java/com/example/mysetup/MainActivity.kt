package com.example.mysetup

import android.content.Context
import android.content.Intent
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

class MainActivity : AppCompatActivity(), RecyclerAdapter.OnItemClickListener {
    private lateinit var recyclerView: RecyclerView
    private lateinit var recyclerAdapter: RecyclerAdapter
    private var dataList = mutableListOf<RecyclerData>()
    private val  spm = SPManager(this)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        recyclerView = findViewById(R.id.rv_design)
        recyclerView.layoutManager = GridLayoutManager(applicationContext, 1)

        // Deserializing list
        dataList = spm.getList()

        recyclerAdapter = RecyclerAdapter(dataList)
        recyclerAdapter.onItemClickListener = this
        recyclerView.adapter = recyclerAdapter

        // Checking if value is passed and if so, removing it from list
        if (intent.hasExtra("remove")) {
            val receivedItem = intent?.getSerializableExtra("remove") as RecyclerData
            dataList.remove(receivedItem)
        }

        // Setting add new button behaviour
        val buttonShowPopup = findViewById<Button>(R.id.button2)
        buttonShowPopup.setOnClickListener {
            addNew()
        }
    }

    // Deserialization on app closure
    override fun onStop() {
        super.onStop()
        spm.saveList(dataList)
    }

    // Passing clicked RecyclerData to BikeActivity and opening it
    override fun onItemClick(item: RecyclerData) {
        val intent = Intent(this, BikeActivity::class.java)
        intent.putExtra("key", item)
        startActivity(intent)
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
            dataList.add(RecyclerData(enteredName, false))
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
}