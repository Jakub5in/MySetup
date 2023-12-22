package com.example.mysetup

import java.io.Serializable


// Data class representing a Bike
data class Bike(
    var name: String,                                   // Name of the bike
    var setups: MutableList<Setup> = mutableListOf()    // List of setups associated with the bike
    ) : Serializable {

    // Override equals to compare bikes
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other !is Bike) return false

        return (this.name == other.name) && (this.setups == other.setups)
    }

    // Override hashCode to generate a unique hash for each bike
    override fun hashCode(): Int {
        return 31 * name.hashCode() + setups.hashCode()
    }
}