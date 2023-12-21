package com.example.mysetup

import java.io.Serializable


// Data class representing a bike part
class BikePart(
    val name: String,       // Name of the part
) : Serializable {
    var model: String = ""                                 // Model of the part
    var settings: MutableList<Setting> = mutableListOf()   // List of settings associated with the part

    fun setPossibleSettings(possible: MutableList<String>, help: MutableList<String>) {
        for(i in 0 until possible.size) {
            settings.add(Setting(possible[i], help[i]))
        }
    }
}
