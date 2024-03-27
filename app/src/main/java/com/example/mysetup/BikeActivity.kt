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

    private lateinit var receivedBike: Bike         // receivedBike contains the chosen Bike
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
            receivedBike = receivedIntent.getSerializableExtra("key") as Bike
        }

        // Set text displayed at the top
        val bikeName: TextView = findViewById(R.id.ActivityBike_TextView_BikeName)
        bikeName.text = receivedBike.name

        // Set delete button behaviour
        val buttonDelete = findViewById<Button>(R.id.ActivityBike_Button_Delete)
        buttonDelete.setBackgroundColor(getThemeColor(this, android.R.attr.colorAccent, 1.0f))
        buttonDelete.setTextColor(Color.BLACK)
        buttonDelete.setOnClickListener {
            delete()
        }

        // Set add new button behaviour
        val buttonShowPopupAddNew = findViewById<Button>(R.id.ActivityBike_Button_Add)
        buttonShowPopupAddNew.setOnClickListener {
            addNew()
        }

        // Set filter button behaviour
        val buttonShowPopupFilter = findViewById<Button>(R.id.ActivityBike_Button_Filter)
        buttonShowPopupFilter.setOnClickListener {
            filter()
        }

        // Initialize and set up the RecyclerView
        recyclerView = findViewById(R.id.ActivityBike_RecyclerView_Setups)
        recyclerView.layoutManager = GridLayoutManager(applicationContext, 1)
        recyclerView.adapter = run {
            val adapter = SetupRecyclerAdapter(receivedBike.setups)
            adapter.onItemClickListener = this
            adapter
        }

    }


    // Handle the result from SetupActivity
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == REQUEST_CODE_SETUP_ACTIVITY && resultCode == Activity.RESULT_OK) {

            // Remove setup if marked for deletion
            if (data?.hasExtra("remove") == true) {
                val removedSetup = data.getSerializableExtra("remove") as? Setup
                removedSetup?.let {
                    val index = receivedBike.setups.indexOfFirst { it.name == removedSetup.name }
                    if (index != -1) {
                        receivedBike.setups.removeAt(index)
                        recyclerView.adapter?.notifyItemRemoved(index)
                    }
                }
            }

            if (data?.hasExtra("mirror") == true ){
                val copiedSetup = data.getSerializableExtra("mirror") as? Setup
                copiedSetup?.let {
                    val index = receivedBike.setups.indexOfFirst { it.name == copiedSetup.name }
                    if (index != -1) {
                        receivedBike.setups.add(index+1, copiedSetup)
                        receivedBike.setups[index+1].name = receivedBike.setups[index+1].newName
                        recyclerView.adapter?.notifyItemInserted(index+1)
                    }
                }
            }

            // Update setup if modified
            if (data?.hasExtra("updatedSetup") == true) {
                val updatedSetup = data.getSerializableExtra("updatedSetup") as? Setup
                updatedSetup?.let {
                    val index = receivedBike.setups.indexOfFirst { it.name == updatedSetup.name }
                    if (index != -1) {
                        receivedBike.setups[index] = updatedSetup
                        recyclerView.adapter?.notifyItemChanged(index)
                    }
                }
            }
        }
    }


    // Called when the activity has detected the user's press of the back key
    override fun onBackPressed() {
        val intent = Intent().putExtra("updatedBike", receivedBike)
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
        val buttonYes = view.findViewById<Button>(R.id.PopupDelete_Button_Yes)
        buttonYes.setOnClickListener {
            popupDelete.dismiss()

            // Check if setups is not null before attempting to remove an item
            receivedBike.setups.let {
                // Passing the object back to MainActivity, so it can be removed from List
                val intent = Intent(this, MainActivity::class.java).apply {
                    putExtra("remove", receivedBike)
                }
                intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP
                startActivity(intent)
                finish()
            }
        }

        // Popup window button
        val buttonCancel = view.findViewById<Button>(R.id.PopupDelete_Button_Cancel)
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
        view.findViewById<ImageView>(R.id.PopupAddNewSetup_ImageView_GradeLevel0).setColorFilter(R.color.TextOnBackground)
        view.findViewById<ImageView>(R.id.PopupAddNewSetup_ImageView_GradeLevel1).setColorFilter(R.color.TextOnBackground)
        view.findViewById<ImageView>(R.id.PopupAddNewSetup_ImageView_GradeLevel2).setColorFilter(R.color.TextOnBackground)
        view.findViewById<ImageView>(R.id.PopupAddNewSetup_ImageView_WetnessLevel2).setColorFilter(R.color.TextOnBackground)
        view.findViewById<ImageView>(R.id.PopupAddNewSetup_ImageView_WetnessLevel1).setColorFilter(R.color.TextOnBackground)
        view.findViewById<ImageView>(R.id.PopupAddNewSetup_ImageView_WetnessLevel0).setColorFilter(R.color.TextOnBackground)/*
        view.findViewById<ImageView>(R.id.PopupAddNewSetup_ImageView_RoughnessLevel0).setColorFilter(R.color.TextOnBackground)
        view.findViewById<ImageView>(R.id.PopupAddNewSetup_ImageView_RoughnessLevel1).setColorFilter(R.color.TextOnBackground)*/
        view.findViewById<ImageView>(R.id.PopupAddNewSetup_ImageView_RoughnessLevel2).setColorFilter(R.color.TextOnBackground)

        // Set references to UI elements
        val editText = view.findViewById<EditText>(R.id.PopupAddNewSetup_EditText_SetupName)
        val seekBarWetness = view.findViewById<SeekBar>(R.id.PopupAddNewSetup_SeekBar_Wetness)
        val seekBarUphill = view.findViewById<SeekBar>(R.id.PopupAddNewSetup_SeekBar_Grade)
        val seekBarTerrain = view.findViewById<SeekBar>(R.id.PopupAddNewSetup_SeekBar_Roughness)

        // Popup window button
        val buttonAdd = view.findViewById<Button>(R.id.PopupAddNewSetup_Button_Add)
        buttonAdd.setOnClickListener {
            val enteredName = editText.text.toString()
            val wetness = seekBarWetness.progress
            val uphill = seekBarUphill.progress
            val terrain = seekBarTerrain.progress

            // Add new setup
            receivedBike.setups.add(Setup(enteredName, wetness, uphill, terrain))
            val newItemPosition = receivedBike.setups.size - 1
            recyclerView.adapter?.notifyItemInserted(newItemPosition)
            popupWindow.dismiss()
        }

        // Popup window button
        val buttonCancel = view.findViewById<Button>(R.id.PopupAddNewSetup_Button_Cancel)
        buttonCancel.setOnClickListener {
            popupWindow.dismiss()
        }
    }


    //  Function to handle filtering setups
    private fun filter() {
        val inflater = getSystemService(LAYOUT_INFLATER_SERVICE) as LayoutInflater
        val rootView = findViewById<View>(android.R.id.content)
        val view = inflater.inflate(R.layout.popup_filter_setups, rootView as ViewGroup, false)

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
        view.findViewById<ImageView>(R.id.PopupFilterSetups_ImageView_GradeLevel0).setColorFilter(R.color.TextOnBackground)
        view.findViewById<ImageView>(R.id.PopupFilterSetups_ImageView_GradeLevel1).setColorFilter(R.color.TextOnBackground)
        view.findViewById<ImageView>(R.id.PopupFilterSetups_ImageView_GradeLevel2).setColorFilter(R.color.TextOnBackground)
        view.findViewById<ImageView>(R.id.PopupFilterSetups_ImageView_WetnessLevel2).setColorFilter(R.color.TextOnBackground)
        view.findViewById<ImageView>(R.id.PopupFilterSetups_ImageView_WetnessLevel1).setColorFilter(R.color.TextOnBackground)
        view.findViewById<ImageView>(R.id.PopupFilterSetups_ImageView_WetnessLevel0).setColorFilter(R.color.TextOnBackground)/*
        view.findViewById<ImageView>(R.id.PopupFilterSetups_ImageView_RoughnessLevel0).setColorFilter(R.color.TextOnBackground)
        view.findViewById<ImageView>(R.id.PopupFilterSetups_ImageView_RoughnessLevel1).setColorFilter(R.color.TextOnBackground)*/
        view.findViewById<ImageView>(R.id.PopupFilterSetups_ImageView_RoughnessLevel2).setColorFilter(R.color.TextOnBackground)

        // Set references to UI elements
        val seekBarWetness = view.findViewById<SeekBar>(R.id.PopupFilterSetups_SeekBar_Wetness)
        val seekBarUphill = view.findViewById<SeekBar>(R.id.PopupFilterSetups_SeekBar_Grade)
        val seekBarTerrain = view.findViewById<SeekBar>(R.id.PopupFilterSetups_SeekBar_Roughness)
        val switchWetness = view.findViewById<SwitchCompat>(R.id.PopupFilterSetups_SwitchCompat_TrackWetness)
        val switchUphill = view.findViewById<SwitchCompat>(R.id.PopupFilterSetups_SwitchCompat_TrackGrade)
        val switchTerrain = view.findViewById<SwitchCompat>(R.id.PopupFilterSetups_SwitchCompat_TrackRoughness)

        // Popup window button
        val buttonAdd = view.findViewById<Button>(R.id.PopupFilterSetups_Button_Accept)
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
