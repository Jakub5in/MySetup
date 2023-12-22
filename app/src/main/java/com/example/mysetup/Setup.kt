package com.example.mysetup

import java.io.Serializable


// Data class representing a bike setup
data class Setup(
    var name: String,                                       // Name of the setup
    var wetness: Int = 1,                                   // Track wetness, expected 0-2
    var terrain: Int = 1,                                   // Track terrain roughness, expected 0-2
    var uphills: Int = 1,                                   // Track uphill amount, expected 0-2
    var rearTire: BikePart = BikePart("Rear tire"),
    var frontTire: BikePart = BikePart("Front tire"),
    var damper: BikePart = BikePart("Rear suspension"),
    var fork: BikePart = BikePart("Front suspension")
) : Serializable {


    // Add all possible settings on init
    init {
        val tirePossible: MutableList<String> = mutableListOf("Pressure [psi]")
        val tireHelp: MutableList<String> = mutableListOf("Increase for better rolling efficiency and when you damage tires or rims too often. Decrease for more traction and comfort.")

        val suspensionPossible: MutableList<String> = mutableListOf("Travel [mm]", "Air pressure [psi]", "Coil force [lbs]", "SAG [%]", "Volume spacers [#]", "Rebound [#]", "LS Compression [#]", "HS Compression [#]", "LS Rebound [#]", "HS Rebound [#]", "Coil Preload [#]")
        val suspensionHelp: MutableList<String> = mutableListOf("Suspension travel is the distance that it can compress and extend. The more travel a bike has, the better it can handle larger and more aggressive obstacles.", "Air pressure defines the stiffness of the spring. Increase for better support when riding aggressively in rough terrain. Decrease for a more comfortable and compliant ride.", "Coil force defines the stiffnes of the spring. Increase for better support when riding aggressively in rough terrain. Decrease for more comfortable and compliant ride.", "SAG is the amount a suspension compressess under static weight of a rider. Increase for more comfortable and compliant ride. Decrease for better support when riding aggressively in rought terrain.", "Volume spacers adjust the air spring's volume. Add more spacers for more progressive suspension and less bottoming out. Take them out for more linear suspension.", "Rebound damping is the resistance applied to extension of the suspension. Increase for more controlled extension, especially when hitting jumps or drops. Decrease for faster extension and better traction on fast, back to back impacts.", "Low-speed compression damping is the resistance applied to suspension when compressed at low speeds. Increase for better pedal efficiency and improved support in berms and rollers. Decrease for more responsiveness and smoother ride.", "High-speed compression damping is the resistance applied to suspension when compressed at high speeds. Increase for more control on big drops and jumps. Decrease for smoother ride and more compliance on landing.", "Low-speed rebound damping is the resistance applied to extension of the suspension at low speeds. Increase for more stable ride. Decrease for smoother ride over small bumps.", "High-speed rebound damping is the resistance applied to extension of the suspension at high speeds. Increase for more control on big drops and jumps. Decrease will speed up suspension response.", "Coil preload is the amount of compression force applied to the coil when static. Increase for more initial stiffness. Decrease for softer initial feel.")

        rearTire.setPossibleSettings(tirePossible, tireHelp)
        frontTire.setPossibleSettings(tirePossible, tireHelp)
        damper.setPossibleSettings(suspensionPossible, suspensionHelp)
        fork.setPossibleSettings(suspensionPossible, suspensionHelp)
    }
}
