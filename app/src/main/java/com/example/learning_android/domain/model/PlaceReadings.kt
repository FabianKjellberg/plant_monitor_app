package com.example.learning_android.domain.model

data class PlaceReadings(
  val placeId: String,
  val readings: List<DeviceReading>
)
