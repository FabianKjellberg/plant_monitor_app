package com.example.learning_android.domain.model

import java.time.Instant

data class Device(
    val id: String,
    val name: String,
    val batteryPercentage: Int?,
    val batteryReadAt: Instant?,
    val placeId: String?,
    val type: DeviceType
)
