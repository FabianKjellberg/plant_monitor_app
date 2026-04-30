package com.example.learning_android.data.remote.dto

data class DeviceDTO(
    val deviceId: String,
    val name: String? = null,
    val batteryMv: Float? = null,
    val batteryReadAt: String? = null,
)
