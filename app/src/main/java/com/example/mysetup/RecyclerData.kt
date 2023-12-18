package com.example.mysetup
import java.io.Serializable

data class RecyclerData(
    var name: String,
    var isSelected: Boolean) : Serializable {
    fun setIsSelected(value: Boolean) {
        isSelected = value
    }



}