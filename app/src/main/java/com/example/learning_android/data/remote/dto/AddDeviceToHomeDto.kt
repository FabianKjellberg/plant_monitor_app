package com.example.learning_android.data.remote.dto

data class AddDeviceToHomeDto (
  val mac: String,
  val name: String,
  val homeId: String,
  val placeId: String?
)
