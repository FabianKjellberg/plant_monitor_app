package com.example.learning_android.data.mapper

import com.example.learning_android.data.remote.dto.DetailedHomeDeviceDto
import com.example.learning_android.data.remote.dto.DetailedHomeDto
import com.example.learning_android.data.remote.dto.DetailedHomePlaceDto
import com.example.learning_android.data.remote.dto.DetailedHomeRoomDto
import com.example.learning_android.domain.model.DetailedHome
import com.example.learning_android.domain.model.DetailedHomeDevice
import com.example.learning_android.domain.model.DetailedHomePlace
import com.example.learning_android.domain.model.DetailedHomeRoom
import com.example.learning_android.domain.model.DeviceType

fun DetailedHomeDto.toDomain(): DetailedHome {
  return DetailedHome(
    id = id,
    name = name,
    rooms = rooms.map { it.toDomain() }
  )
}

fun DetailedHomeRoomDto.toDomain(): DetailedHomeRoom {
  return DetailedHomeRoom(
    id = id,
    name = name,
    icon = icon,
    places = places.map { it.toDomain() }
  )
}

fun DetailedHomePlaceDto.toDomain(): DetailedHomePlace {
  return DetailedHomePlace(
    id = id,
    name = name,
    icon = icon,
    devices = devices.map { it.toDomain() }
  )
}

fun DetailedHomeDeviceDto.toDomain(): DetailedHomeDevice {
  return DetailedHomeDevice(
    id = id,
    name = name ?: "Unnamed Device",
    type = DeviceType.fromString(type),
    batteryMv = batteryMv
  )
}