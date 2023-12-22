package com.example.mysetup

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.PopupWindow
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.WindowCompat
import com.example.mysetup.databinding.ActivitySetupBinding


// Activity representing chosen Setup
class SetupActivity : AppCompatActivity() {

    private lateinit var binding: ActivitySetupBinding  // View binding for activity layout
    private lateinit var receivedItem: Setup            // Setup data received from BikeActivity
    private val REQUEST_CODE_BPART_ACTIVITY = 1         // Code to launch BikePartActivity


    // Called when the activity is first created
    override fun onCreate(savedInstanceState: Bundle?) {
        WindowCompat.setDecorFitsSystemWindows(window, false)
        super.onCreate(savedInstanceState)

        // Receive Setup data class passed from BikeActivity
        val receivedIntent = intent
        receivedItem = receivedIntent.getSerializableExtra("key") as Setup

        // Inflate the layout using ViewBinding
        binding = ActivitySetupBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Set text displayed at the top
        val setupName: TextView = findViewById(R.id.ActivitySetup_TextView_SetupName)
        setupName.text = receivedItem.name

        // Set delete button behaviour
        val buttonDelete = findViewById<Button>(R.id.ActivitySetup_Button_Delete)
        buttonDelete.setBackgroundColor(getThemeColor(this, android.R.attr.colorAccent, 1.0f))
        buttonDelete.setTextColor(Color.BLACK)
        buttonDelete.setOnClickListener {
            delete()
        }

        // Set damper button behaviour
        val buttonDamper = findViewById<Button>(R.id.ActivitySetup_Button_RearSuspension)
        buttonDamper.setOnClickListener {
            damper()
        }

        // Set fork button behaviour
        val buttonFork = findViewById<Button>(R.id.ActivitySetup_Button_FrontSuspension)
        buttonFork.setOnClickListener {
            fork()
        }

        // Set rearTire button behaviour
        val buttonRearTire = findViewById<Button>(R.id.ActivitySetup_Button_RearTire)
        buttonRearTire.setOnClickListener {
            rTire()
        }

        // Set frontTire button behaviour
        val buttonFrontTire = findViewById<Button>(R.id.ActivitySetup_Button_FrontTire)
        buttonFrontTire.setOnClickListener {
            fTire()
        }
    }


    // Called when the activity has detected the user's press of the back key
    @Deprecated("Deprecated in Java")
    override fun onBackPressed() {
        val intent = Intent()
        intent.putExtra("updatedSetup", receivedItem)
        setResult(Activity.RESULT_OK, intent)

        super.onBackPressed()
    }


    // Handle the result from SetupActivity
    @Deprecated("Deprecated in Java")
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == REQUEST_CODE_BPART_ACTIVITY && resultCode == Activity.RESULT_OK) {

            // Update setup if modified
            if (data?.hasExtra("Rear tire") == true) {
                val updatedBikePart = data.getSerializableExtra("Rear tire") as? BikePart
                if (updatedBikePart != null) {
                    receivedItem.rearTire = updatedBikePart
                }
            }

            if (data?.hasExtra("Front tire") == true) {
                val updatedBikePart = data.getSerializableExtra("Front tire") as? BikePart
                if (updatedBikePart != null) {
                    receivedItem.frontTire = updatedBikePart
                }
            }

            if (data?.hasExtra("Rear suspension") == true) {
                val updatedBikePart = data.getSerializableExtra("Rear suspension") as? BikePart
                if (updatedBikePart != null) {
                    receivedItem.damper = updatedBikePart
                }
            }

            if (data?.hasExtra("Front suspension") == true) {
                val updatedBikePart = data.getSerializableExtra("Front suspension") as? BikePart
                if (updatedBikePart != null) {
                    receivedItem.fork = updatedBikePart
                }
            }
        }
    }


    // Function to handle delete popup
    private fun delete() {
        val inflater = getSystemService(LAYOUT_INFLATER_SERVICE) as LayoutInflater
        val rootView = findViewById<View>(android.R.id.content)
        val view = inflater.inflate(R.layout.popup_delete, rootView as ViewGroup, false)

        val popupDelete = PopupWindow(
            view,
            ViewGroup.LayoutParams.WRAP_CONTENT,
            ViewGroup.LayoutParams.WRAP_CONTENT,
            true
        )

        // Make popup background semitransparent
        view.setBackgroundColor(getThemeColor(this, android.R.attr.colorBackground, 0.9f))
        popupDelete.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        popupDelete.inputMethodMode = PopupWindow.INPUT_METHOD_NEEDED
        popupDelete.showAtLocation(view, Gravity.CENTER, 0, 0)

        // Popup window button
        val buttonYes = view.findViewById<Button>(R.id.PopupDelete_Button_Yes)
        buttonYes.setOnClickListener {
            popupDelete.dismiss()
            // Passing the object back to BikeActivity, so it can be removed from List
            val intent = Intent(this, BikeActivity::class.java)
            intent.putExtra("remove", receivedItem)
            setResult(Activity.RESULT_OK, intent)
            finish()
        }

        // Popup window button
        val buttonCancel = view.findViewById<Button>(R.id.PopupDelete_Button_Cancel)
        buttonCancel.setOnClickListener {
            popupDelete.dismiss()
        }
    }


    // Function to handle damper button
    private fun damper(){
        val item = receivedItem.damper
        val intent = Intent(this, BikePartActivity::class.java)
        intent.putExtra("key", item)
        startActivityForResult(intent, REQUEST_CODE_BPART_ACTIVITY)
    }


    // Function to handle fork button
    private fun fork(){
        val item = receivedItem.fork
        val intent = Intent(this, BikePartActivity::class.java)
        intent.putExtra("key", item)
        startActivityForResult(intent, REQUEST_CODE_BPART_ACTIVITY)
    }


    // Function to handle rearTire button
    private fun rTire(){
        val item = receivedItem.rearTire
        val intent = Intent(this, BikePartActivity::class.java)
        intent.putExtra("key", item)
        startActivityForResult(intent, REQUEST_CODE_BPART_ACTIVITY)
    }


    // Function to handle frontTire button
    private fun fTire(){
        val item = receivedItem.frontTire
        val intent = Intent(this, BikePartActivity::class.java)
        intent.putExtra("key", item)
        startActivityForResult(intent, REQUEST_CODE_BPART_ACTIVITY)
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