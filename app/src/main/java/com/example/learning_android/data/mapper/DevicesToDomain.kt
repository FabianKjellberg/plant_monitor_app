package com.example.learning_android.data.mapper

import com.example.learning_android.data.remote.dto.DeviceDto
import com.example.learning_android.data.remote.dto.GetDevicesResponseDto
import com.example.learning_android.data.remote.helpers.batteryPercentFromMv
import com.example.learning_android.domain.model.Device
import com.example.learning_android.domain.model.DeviceHome
import com.example.learning_android.domain.model.DeviceType
import java.time.Instant

fun DeviceDto.toDomain(): Device {
  return Device(
    id = id,
    name = name ?: "Unnamed Device",
    batteryPercentage = batteryPercentFromMv(batteryMv),
    batteryReadAt = batteryReadAt?.let { Instant.parse(batteryReadAt) },
    placeId = placeId,
    type = DeviceType.fromString(deviceType)
  )
}

fun GetDevicesResponseDto.toDomain(): List<DeviceHome>{
  return homes.map {
    DeviceHome(
      id = it.id,
      name = it.name,
      devices = it.devices.map { it.toDomain() }
    )
  }
}
