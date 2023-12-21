package com.example.mysetup

import java.io.Serializable


// Data class representing a setting
data class Setting(
    val name: String,               // Name of the setting
    val help: String,               // Explanation of the setting
    var isSet: Boolean = false,     // Is setting set
    var value: Int = 0              // Value of the setting
) : Serializable {

}
