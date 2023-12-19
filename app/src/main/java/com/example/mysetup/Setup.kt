package com.example.mysetup

import java.io.Serializable

data class Setup(
    var name: String,
    var wetness: Int = 1,
    var terrain: Int = 1,
    var uphills: Int = 1
) : Serializable {
    var frontTirePressure: Float = 0.0F
    var rearTirePressure: Float = 0.0F


}