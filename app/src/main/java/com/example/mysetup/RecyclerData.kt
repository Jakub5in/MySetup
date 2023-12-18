package com.example.mysetup
import java.io.Serializable

data class RecyclerData(
    var name: String,
    var isSelected: Boolean

    ) : Serializable {

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other !is RecyclerData) return false

        return this.name== other.name && this.isSelected == other.isSelected
    }

    override fun hashCode(): Int {
        return 31 * name.hashCode() + isSelected.hashCode()
    }
}