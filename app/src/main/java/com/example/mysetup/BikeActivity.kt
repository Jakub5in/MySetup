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
import android.widget.PopupWindow
import android.widget.TextView

class BikeActivity : AppCompatActivity() {
    lateinit var receivedItem: RecyclerData

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_bike)

        // Receiving RecyclerData data class passed from MainActivity
        val receivedIntent = intent
        receivedItem = receivedIntent.getSerializableExtra("key") as RecyclerData

        // Setting text displayed at the top to the name from data class
        val bikeName: TextView = findViewById(R.id.bikeName)
        bikeName.text = receivedItem.name

        // Setting delete button behaviour
        val buttonDelete = findViewById<Button>(R.id.button_delete)
        buttonDelete.setOnClickListener {
            delete()
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
            val intent = Intent(this, MainActivity::class.java)
            intent.putExtra("remove", receivedItem)
            intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP
            startActivity(intent)
            finish()
        }

        val buttonCancel = view.findViewById<Button>(R.id.button_cancel)
        buttonCancel.setOnClickListener {
            popupDelete.dismiss()
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
