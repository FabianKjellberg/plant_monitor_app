package com.example.learning_android.data.remote.dto

data class GetHomeResponseDto (
  val homes: List<DetailedHomeDto>
)

data class DetailedHomeDto (
  val id: String,
  val name: String,
  val rooms: List<DetailedHomeRoomDto>
)

data class DetailedHomeRoomDto (
  val id: String,
  val name: String,
  val icon: String?,
  val places: List<DetailedHomePlaceDto>
)

data class DetailedHomePlaceDto (
  val id: String,
  val name: String,
  val icon: String?,
  val devices: List<DetailedHomeDeviceDto>,
)

data class DetailedHomeDeviceDto (
  val id: String,
  val name: String?,
  val type: String,
  val batteryMv: Int?,
)



