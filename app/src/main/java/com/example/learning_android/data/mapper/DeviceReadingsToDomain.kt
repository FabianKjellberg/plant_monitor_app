package com.example.learning_android.data.mapper

import com.example.learning_android.data.remote.dto.DeviceReadingDto
import com.example.learning_android.data.remote.dto.GetDeviceReadingsResponseDto
import com.example.learning_android.domain.model.DeviceReading
import com.example.learning_android.domain.model.DeviceReadings
import java.lang.Math.clamp
import java.time.Instant

private const val OFFSET_MV = 180;
fun DeviceReadingDto.toDomain(): DeviceReading {

    val batteryPercentage = batteryMv?.let { mv ->
        val corrected = mv + OFFSET_MV
        ((corrected - 3300f) / (4200f - 3300f))
            .coerceIn(0f, 1f) * 100f
    }


    return DeviceReading(
        lux = lux?.coerceAtMost(2000.0F),
        pressure = pressure,
        humidity = humidity,
        temperature = temperature,
        batteryMv = batteryPercentage,
        readAt = Instant.parse(readAt)
    )
}

fun GetDeviceReadingsResponseDto.toDomain(): DeviceReadings {
    return DeviceReadings(
        deviceId = deviceId,
        readings = readings.map { it.toDomain() }
    )
}