package com.example.mysetup

class RecyclerData(
    var name: String,
    var img: Int,
    var isSelected: Boolean
) {

    fun setIsSelected(value: Boolean) {
        isSelected = value
    }

}