package com.example.learning_android.data.remote.dto

data class DeviceReadingDto(
    val lux: Float? = null,
    val pressure: Float? = null,
    val humidity: Float? = null,
    val temperature: Float? = null,
    val batteryMv: Int? = null,
    val readAt: String,
)
