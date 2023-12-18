package com.example.mysetup

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.TextView

class BikeActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_bike)

        val receivedIntent = intent
        val receivedItem = receivedIntent.getSerializableExtra("key") as RecyclerData

        val bikeName: TextView = findViewById(R.id.bikeName)
        bikeName.text = receivedItem.name

    }
}
