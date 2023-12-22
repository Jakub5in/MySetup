package com.example.mysetup

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.PopupWindow
import android.widget.TextView
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView

class BikePartActivity : AppCompatActivity() {
    private lateinit var receivedItem: BikePart         // BikePart data received from SetupActivity
    lateinit var recyclerView: RecyclerView             // recycler for displaying settings

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_bike_part)

        // Receive passed data
        val receivedIntent = intent
        if (intent.hasExtra("key")) {
            receivedItem = receivedIntent.getSerializableExtra("key") as? BikePart ?: return
        }

        // Set text displayed at the top
        val partName: TextView = findViewById(R.id.ActivityBikePart_TextView_PartName)
        val partModel: TextView = findViewById(R.id.ActivityBikePart_TextView_PartModel)
        partName.text = receivedItem.name
        partModel.text = receivedItem.model

        // Initialize and set up the RecyclerView
        recyclerView = findViewById(R.id.ActivityBikePart_RecyclerView_Settings)
        recyclerView.layoutManager = GridLayoutManager(applicationContext, 1)
        recyclerView.adapter = run {
            // Filter out settings where isSet is not true
            val filteredSettings = receivedItem.settings.filter {it.isSet}

            val adapter = SettingsRecyclerAdapter(filteredSettings)
            adapter
        }

        // Set edit button behaviour
        val buttonEdit = findViewById<Button>(R.id.ActivityBikePart_Button_Edit)
        buttonEdit.setBackgroundColor(getThemeColor(this, android.R.attr.colorAccent, 1.0f))
        buttonEdit.setTextColor(Color.BLACK)
        buttonEdit.setOnClickListener {
            edit()
        }
    }


    // Called when the activity has detected the user's press of the back key
    @Deprecated("Deprecated in Java")
    override fun onBackPressed() {
        val intent = Intent().putExtra(receivedItem.name, receivedItem)
        setResult(Activity.RESULT_OK, intent)
        super.onBackPressed()
    }


    private fun edit(){
        val inflater = LayoutInflater.from(this)
        val rootView = findViewById<View>(android.R.id.content)
        val popupView = inflater.inflate(R.layout.popup_edit_settings, rootView as ViewGroup, false)

        // Get the dimensions of the screen
        val displayMetrics = resources.displayMetrics
        val screenWidth = displayMetrics.widthPixels
        val screenHeight = displayMetrics.heightPixels

        val popupWindow = PopupWindow(
            popupView,
            screenWidth,
            screenHeight,
            true
        )

        // Make popup background semitransparent
        popupView.setBackgroundColor(getThemeColor(this, android.R.attr.colorBackground, 0.9f))
        popupWindow.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        popupWindow.inputMethodMode = PopupWindow.INPUT_METHOD_NEEDED
        popupWindow.showAtLocation(popupView, Gravity.CENTER, 0, 0)

        // Initialize and set up the RecyclerView in the popup
        val popupRecyclerView: RecyclerView = popupView.findViewById(R.id.PopupEditSettings_RecyclerView_Settings)
        popupRecyclerView.layoutManager = GridLayoutManager(applicationContext, 1)
        popupRecyclerView.adapter = run {
            val adapter = SettingsEditRecyclerAdapter(this, receivedItem.settings)
            adapter
        }

        // Set up the dismiss listener to dismiss the popup when touched outside
        popupWindow.setBackgroundDrawable(null)
        popupWindow.isOutsideTouchable = true

        // Popup window button
        val buttonAccept = popupView.findViewById<Button>(R.id.PopupEditSettings_Button_Accept)
        buttonAccept.setOnClickListener {
            // Get the updated settings from the adapter in the popup RecyclerView
            val popupAdapter = popupRecyclerView.adapter as? SettingsEditRecyclerAdapter
            val updatedSettings = popupAdapter?.getSettings() ?: mutableListOf()
            val popupTextEdit = popupView.findViewById<TextView>(R.id.PopupEditSettings_EditText_PartModel)

            // Update receivedItem.settings with the updated settings
            receivedItem.settings = updatedSettings
            receivedItem.model = popupTextEdit.text.toString()

            // Dismiss the popup window
            popupWindow.dismiss()

            // Optionally, notify the main RecyclerView adapter that the data has changed
            recyclerView.adapter = run {
                // Filter out settings where isSet is not true
                val filteredSettings = receivedItem.settings.filter {it.isSet}

                val adapter = SettingsRecyclerAdapter(filteredSettings)
                adapter
            }
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