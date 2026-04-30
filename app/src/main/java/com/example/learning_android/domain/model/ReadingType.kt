package com.example.learning_android.domain.model

enum class ReadingType(val displayName: String) {
    LUX("Light (lux)"),
    TEMPERATURE("Temperature (°C)"),
    BATTERY_PERCENTAGE("Battery (%)"),
    HUMIDITY("Humidity (%RH)"),
    PRESSURE("Pressure (Pa)"),
}