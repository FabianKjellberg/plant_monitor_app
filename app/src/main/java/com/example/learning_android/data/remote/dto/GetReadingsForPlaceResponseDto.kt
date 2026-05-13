package com.example.learning_android.data.remote.dto

data class GetReadingsForPlaceResponseDto (
  val placeId: String,
  val readings: List<DeviceReadingDto>
)
