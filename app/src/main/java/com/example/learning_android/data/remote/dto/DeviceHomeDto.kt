package com.example.learning_android.data.remote.dto

data class DeviceHomeDto(
  val id: String,
  val name: String,
  val devices: List<DeviceDto>
)
