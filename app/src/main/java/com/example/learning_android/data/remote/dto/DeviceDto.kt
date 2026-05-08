package com.example.learning_android.data.remote.dto

data class DeviceDto(
    val id: String,
    val name: String? = null,
    val batteryMv: Int? = null,
    val batteryReadAt: String? = null,
    val placeId: String? = null,
    val deviceType: String? = null
)
