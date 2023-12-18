package com.example.mysetup

import android.content.Intent
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

        dataList = spm.getList()

        recyclerAdapter = RecyclerAdapter(this, dataList)
        recyclerAdapter.onItemClickListener = this
        recyclerView.adapter = recyclerAdapter

        val buttonShowPopup = findViewById<Button>(R.id.button2)
        buttonShowPopup.setOnClickListener {
            addNew()
        }
    }

    override fun onStop() {
        super.onStop()
        spm.saveList(dataList)
    }

    override fun onItemClick(item: RecyclerData) {
        val intent = Intent(this, BikeActivity::class.java)
        intent.putExtra("key", item)
        startActivity(intent)
    }

    private fun addNew() {
        val inflater = getSystemService(LAYOUT_INFLATER_SERVICE) as LayoutInflater
        val view = inflater.inflate(R.layout.popup, null)

        val popupWindow = PopupWindow(
            view,
            ViewGroup.LayoutParams.WRAP_CONTENT,
            ViewGroup.LayoutParams.WRAP_CONTENT,
            true
        )

        popupWindow.inputMethodMode = PopupWindow.INPUT_METHOD_NEEDED
        popupWindow.showAtLocation(view, Gravity.CENTER, 0, 0)

        val editText = view.findViewById<EditText>(R.id.editText)

        val buttonAdd = view.findViewById<Button>(R.id.add)
        buttonAdd.setOnClickListener {
            val enteredName = editText.text.toString()
            dataList.add(RecyclerData(enteredName, false))
            popupWindow.dismiss()
        }

        val buttonCancel = view.findViewById<Button>(R.id.cancel)
        buttonCancel.setOnClickListener {
            popupWindow.dismiss()
        }
    }
}