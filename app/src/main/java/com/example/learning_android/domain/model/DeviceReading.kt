package com.example.learning_android.domain.model

import java.time.Instant

data class DeviceReading(
    val lux: Float? = null,
    val pressure: Float? = null,
    val humidity: Float? = null,
    val temperature: Float? = null,
    val batteryPercent: Int? = null,
    val readAt: Instant,
)
