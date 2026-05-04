package com.example.learning_android.domain.model

import java.time.Instant

data class PotDevice(
    val deviceId: String,
    val deviceName: String,
    val batteryPercentage: Float?,
    val batteryReadAt: Instant?
)
