package com.example.learning_android.domain.model

import java.time.Instant

data class PotDevice(
    val deviceId: String,
    val name: String,
    val batteryPercentage: Float?,
    val batteryReadAt: Instant?
)
