package com.example.learning_android.data.mapper

import com.example.learning_android.data.remote.dto.DeviceReadingDto
import com.example.learning_android.data.remote.dto.GetDeviceReadingsResponseDto
import com.example.learning_android.data.remote.helpers.batteryPercentFromMv
import com.example.learning_android.domain.model.DeviceReading
import com.example.learning_android.domain.model.DeviceReadings
import java.lang.Math.clamp
import java.time.Instant

fun DeviceReadingDto.toDomain(): DeviceReading {
    return DeviceReading(
        lux = lux?.coerceAtMost(2000.0F),
        pressure = pressure,
        humidity = humidity,
        temperature = temperature,
        batteryPercent = batteryPercentFromMv(batteryMv),
        readAt = Instant.parse(readAt)
    )
}

fun GetDeviceReadingsResponseDto.toDomain(): DeviceReadings {
    return DeviceReadings(
        deviceId = deviceId,
        readings = readings.map { it.toDomain() }
    )
}