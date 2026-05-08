package com.example.learning_android.domain.model

data class DetailedHome(
  val id: String,
  val name: String,
  val rooms: List<DetailedHomeRoom>
)

data class DetailedHomeRoom(
  val id: String,
  val name: String,
  val icon: String?,
  val places: List<DetailedHomePlace>
)

data class DetailedHomePlace(
  val id: String,
  val name: String,
  val icon: String?,
  val devices: List<DetailedHomeDevice>
)

data class DetailedHomeDevice(
  val id: String,
  val name: String,
  val type: DeviceType,
  val batteryMv: Int?
)
