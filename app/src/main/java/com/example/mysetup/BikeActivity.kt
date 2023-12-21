package com.example.mysetup

import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.appcompat.app.AppCompatActivity
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
import android.widget.*
import androidx.appcompat.widget.SwitchCompat


// Activity representing chosen Bike
class BikeActivity : AppCompatActivity(), SetupRecyclerAdapter.OnItemClickListener  {

    private lateinit var receivedItem: Bike         // receivedItem contains the chosen Bike
    lateinit var recyclerView: RecyclerView         // recycler for displaying setups
    private val REQUEST_CODE_SETUP_ACTIVITY = 1     // Code to launch SetupActivity


    // Called when the activity is first created
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_bike)

        // Receive passed data
        val receivedIntent = intent

        // Get the Bike if passed from MainActivity
        if (intent.hasExtra("key")) {
            receivedItem = receivedIntent.getSerializableExtra("key") as Bike
        }

        // Set text displayed at the top
        val bikeName: TextView = findViewById(R.id.bikeName)
        bikeName.text = receivedItem.name

        // Set delete button behaviour
        val buttonDelete = findViewById<Button>(R.id.button_delete)
        buttonDelete.setBackgroundColor(getThemeColor(this, android.R.attr.colorAccent, 1.0f))
        buttonDelete.setTextColor(Color.BLACK)
        buttonDelete.setOnClickListener {
            delete()
        }

        // Set add new button behaviour
        val buttonShowPopupAddNew = findViewById<Button>(R.id.button_add_setup)
        buttonShowPopupAddNew.setOnClickListener {
            addNew()
        }

        // Set filter button behaviour
        val buttonShowPopupFilter = findViewById<Button>(R.id.button_filter)
        buttonShowPopupFilter.setOnClickListener {
            filter()
        }

        // Initialize and set up the RecyclerView
        recyclerView = findViewById(R.id.recycler_setups)
        recyclerView.layoutManager = GridLayoutManager(applicationContext, 1)
        recyclerView.adapter = run {
            val adapter = SetupRecyclerAdapter(receivedItem.setups)
            adapter.onItemClickListener = this
            adapter
        }

    }


    // Handle the result from SetupActivity
    @Deprecated("Deprecated in Java")
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == REQUEST_CODE_SETUP_ACTIVITY && resultCode == Activity.RESULT_OK) {

            // Remove setup if marked for deletion
            if (data?.hasExtra("remove") == true) {
                val removedSetup = data.getSerializableExtra("remove") as? Setup
                removedSetup?.let {
                    val index = receivedItem.setups.indexOfFirst { it.name == removedSetup.name }
                    if (index != -1) {
                        receivedItem.setups.removeAt(index)
                        recyclerView.adapter?.notifyItemRemoved(index)
                    }
                }
            }

            // Update setup if modified
            if (data?.hasExtra("updatedSetup") == true) {
                val updatedSetup = data.getSerializableExtra("updatedSetup") as? Setup
                updatedSetup?.let {
                    val index = receivedItem.setups.indexOfFirst { it.name == updatedSetup.name }
                    if (index != -1) {
                        receivedItem.setups[index] = updatedSetup
                        recyclerView.adapter?.notifyItemChanged(index)
                    }
                }
            }
        }
    }


    // Called when the activity has detected the user's press of the back key
    @Deprecated("Deprecated in Java")
    override fun onBackPressed() {
        val intent = Intent().putExtra("updatedBike", receivedItem)
        setResult(Activity.RESULT_OK, intent)
        super.onBackPressed()
    }


    // Passing clicked Setup in recycler to SetupActivity and opening it
    override fun onItemClick(item: Setup) {
        val intent = Intent(this, SetupActivity::class.java)
        intent.putExtra("key", item)
        startActivityForResult(intent, REQUEST_CODE_SETUP_ACTIVITY)
    }


    // Function to handle delete popup
    private fun delete() {
        val inflater = getSystemService(LAYOUT_INFLATER_SERVICE) as LayoutInflater
        val rootView = findViewById<View>(android.R.id.content)
        val view = inflater.inflate(R.layout.popup_delete, rootView as ViewGroup, false)
        view.setBackgroundColor(getThemeColor(this, android.R.attr.colorBackground, 0.9f)) // Setting semitransparent background to popup

        val popupDelete = PopupWindow(
            view,
            ViewGroup.LayoutParams.WRAP_CONTENT,
            ViewGroup.LayoutParams.WRAP_CONTENT,
            true
        )

        // Make popup background semitransparent
        popupDelete.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        popupDelete.inputMethodMode = PopupWindow.INPUT_METHOD_NEEDED
        popupDelete.showAtLocation(view, Gravity.CENTER, 0, 0)

        // Popup window button
        val buttonYes = view.findViewById<Button>(R.id.button_yes)
        buttonYes.setOnClickListener {
            popupDelete.dismiss()

            // Check if setups is not null before attempting to remove an item
            receivedItem.setups.let {
                // Passing the object back to MainActivity, so it can be removed from List
                val intent = Intent(this, MainActivity::class.java).apply {
                    putExtra("remove", receivedItem)
                }
                intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP
                startActivity(intent)
                finish()
            }
        }

        // Popup window button
        val buttonCancel = view.findViewById<Button>(R.id.button_cancel)
        buttonCancel.setOnClickListener {
            popupDelete.dismiss()
        }
    }


    // Function to handle add new popup
    private fun addNew() {
        val inflater = getSystemService(LAYOUT_INFLATER_SERVICE) as LayoutInflater
        val rootView = findViewById<View>(android.R.id.content)
        val view = inflater.inflate(R.layout.popup_add_new_setup, rootView as ViewGroup, false)

        // Get the dimensions of the screen
        val displayMetrics = resources.displayMetrics
        val screenWidth = displayMetrics.widthPixels
        val screenHeight = displayMetrics.heightPixels

        val popupWindow = PopupWindow(
            view,
            screenWidth,
            screenHeight,
            true
        )

        // Make popup background semitransparent
        view.setBackgroundColor(getThemeColor(this, android.R.attr.colorBackground, 0.9f))
        popupWindow.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        popupWindow.inputMethodMode = PopupWindow.INPUT_METHOD_NEEDED
        popupWindow.showAtLocation(view, Gravity.CENTER, 0, 0)

        // Set icon colors to matching the theme
        view.findViewById<ImageView>(R.id.image_uphill).setColorFilter(R.color.TextOnBackground)
        view.findViewById<ImageView>(R.id.image_allmountain).setColorFilter(R.color.TextOnBackground)
        view.findViewById<ImageView>(R.id.image_downhill).setColorFilter(R.color.TextOnBackground)
        view.findViewById<ImageView>(R.id.image_wet).setColorFilter(R.color.TextOnBackground)
        view.findViewById<ImageView>(R.id.image_semiwet).setColorFilter(R.color.TextOnBackground)
        view.findViewById<ImageView>(R.id.image_dry).setColorFilter(R.color.TextOnBackground)
        view.findViewById<ImageView>(R.id.image_mellow).setColorFilter(R.color.TextOnBackground)
        view.findViewById<ImageView>(R.id.image_mixed).setColorFilter(R.color.TextOnBackground)
        view.findViewById<ImageView>(R.id.image_rough).setColorFilter(R.color.TextOnBackground)

        // Set references to UI elements
        val editText = view.findViewById<EditText>(R.id.editText)
        val seekBarWetness = view.findViewById<SeekBar>(R.id.seekBar_wetness)
        val seekBarUphill = view.findViewById<SeekBar>(R.id.seekBar_uphills)
        val seekBarTerrain = view.findViewById<SeekBar>(R.id.seekBar_terrain)

        // Popup window button
        val buttonAdd = view.findViewById<Button>(R.id.button_add_setup_popup)
        buttonAdd.setOnClickListener {
            val enteredName = editText.text.toString()
            val wetness = seekBarWetness.progress
            val uphill = seekBarUphill.progress
            val terrain = seekBarTerrain.progress

            // Add new setup
            receivedItem.setups.add(Setup(enteredName, wetness, uphill, terrain))
            val newItemPosition = receivedItem.setups.size - 1
            recyclerView.adapter?.notifyItemInserted(newItemPosition)
            popupWindow.dismiss()
        }

        // Popup window button
        val buttonCancel = view.findViewById<Button>(R.id.button_cancel_setup_popup)
        buttonCancel.setOnClickListener {
            popupWindow.dismiss()
        }
    }


    //  Function to handle filtering setups
    private fun filter() {
        val inflater = getSystemService(LAYOUT_INFLATER_SERVICE) as LayoutInflater
        val rootView = findViewById<View>(android.R.id.content)
        val view = inflater.inflate(R.layout.popup_filter_setup, rootView as ViewGroup, false)

        // Get the dimensions of the screen
        val displayMetrics = resources.displayMetrics
        val screenWidth = displayMetrics.widthPixels
        val screenHeight = displayMetrics.heightPixels

        val popupWindow = PopupWindow(
            view,
            screenWidth,
            screenHeight,
            true
        )

        // Make popup background semitransparent
        view.setBackgroundColor(getThemeColor(this, android.R.attr.colorBackground, 0.9f))
        popupWindow.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        popupWindow.inputMethodMode = PopupWindow.INPUT_METHOD_NEEDED
        popupWindow.showAtLocation(view, Gravity.CENTER, 0, 0)

        // Set icon colors to matching the theme
        view.findViewById<ImageView>(R.id.image_uphill2).setColorFilter(R.color.TextOnBackground)
        view.findViewById<ImageView>(R.id.image_allmountain2).setColorFilter(R.color.TextOnBackground)
        view.findViewById<ImageView>(R.id.image_downhill2).setColorFilter(R.color.TextOnBackground)
        view.findViewById<ImageView>(R.id.image_wet2).setColorFilter(R.color.TextOnBackground)
        view.findViewById<ImageView>(R.id.image_semiwet2).setColorFilter(R.color.TextOnBackground)
        view.findViewById<ImageView>(R.id.image_dry2).setColorFilter(R.color.TextOnBackground)
        view.findViewById<ImageView>(R.id.image_mellow2).setColorFilter(R.color.TextOnBackground)
        view.findViewById<ImageView>(R.id.image_mixed2).setColorFilter(R.color.TextOnBackground)
        view.findViewById<ImageView>(R.id.image_rough2).setColorFilter(R.color.TextOnBackground)

        // Set references to UI elements
        val seekBarWetness = view.findViewById<SeekBar>(R.id.seekBar_wetness_filter)
        val seekBarUphill = view.findViewById<SeekBar>(R.id.seekBar_slope_filter)
        val seekBarTerrain = view.findViewById<SeekBar>(R.id.seekBar_roughness_filter)
        val switchWetness = view.findViewById<SwitchCompat>(R.id.switch_wetness_filter)
        val switchUphill = view.findViewById<SwitchCompat>(R.id.switch_slope_filter)
        val switchTerrain = view.findViewById<SwitchCompat>(R.id.switch_roughness_filter)

        // Popup window button
        val buttonAdd = view.findViewById<Button>(R.id.button_filter_accept)
        buttonAdd.setOnClickListener {
            // By default nothing filtered
            var wetness = -1
            var uphill = -1
            var terrain = -1

            // When switch checked, add to the filter
            if (switchWetness.isChecked) {
                wetness = seekBarWetness.progress
            }
            if (switchUphill.isChecked) {
                uphill = seekBarUphill.progress
            }
            if (switchTerrain.isChecked) {
                terrain = seekBarTerrain.progress
            }

            // Set the filter
            (recyclerView.adapter as? SetupRecyclerAdapter)?.setFilter(wetness, uphill, terrain)
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
