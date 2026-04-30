package com.example.learning_android.domain.model

data class DeviceReadings(
    val deviceId: String,
    val readings: List<DeviceReading>,
)
