package com.example.mysetup
import java.io.Serializable

data class Bike(
    var name: String,
    var isSelected: Boolean,

    ) : Serializable {
    var setups: MutableList<Setup> = mutableListOf()

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other !is Bike) return false

        return this.name== other.name && this.isSelected == other.isSelected
    }

    override fun hashCode(): Int {
        return 31 * name.hashCode() + isSelected.hashCode()
    }
}