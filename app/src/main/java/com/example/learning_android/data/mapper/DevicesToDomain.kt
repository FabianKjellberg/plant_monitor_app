package com.example.learning_android.data.mapper

import com.example.learning_android.data.remote.dto.DeviceDTO
import com.example.learning_android.data.remote.dto.GetDevicesResponseDTO
import com.example.learning_android.domain.model.PotDevice
import java.time.Instant

private const val OFFSET_MV = 180

fun DeviceDTO.toDomain(): PotDevice{
    val batteryPercentage = batteryMv?.let { mv ->
        val corrected = mv + OFFSET_MV
        ((corrected - 3300f) / (4200f - 3300f))
            .coerceIn(0f, 1f) * 100f
    }

    return PotDevice(
        deviceId = deviceId,
        deviceName = deviceName ?: "Unnamed Device",
        batteryPercentage = batteryPercentage,
        batteryReadAt = batteryReadAt?.let { Instant.parse(batteryReadAt) }
    )
}

fun GetDevicesResponseDTO.toDomain(): List<PotDevice>{
    return devices.map { it.toDomain() }
}
