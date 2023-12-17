package com.example.mysetup

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView

class MainActivity : AppCompatActivity() {
    private lateinit var recyclerView: RecyclerView
    private lateinit var recyclerAdapter: RecyclerAdapter
    private var dataList = mutableListOf<RecyclerData>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        recyclerView = findViewById(R.id.rv_design)
        recyclerView.layoutManager = GridLayoutManager(applicationContext, 2)

        dataList.add(RecyclerData("Norco Range", R.drawable.ic_bike, false))
        dataList.add(RecyclerData("YT Capra", R.drawable.ic_bike, false))
        dataList.add(RecyclerData("Raaw Madonna", R.drawable.ic_bike, false))
        dataList.add(RecyclerData("Prime Thunderflash", R.drawable.ic_bike, false))

        recyclerAdapter = RecyclerAdapter(this, dataList)
        recyclerView.adapter = recyclerAdapter

    }
}