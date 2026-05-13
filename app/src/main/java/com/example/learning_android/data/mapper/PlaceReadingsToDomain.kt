package com.example.learning_android.data.mapper

import com.example.learning_android.data.remote.dto.GetReadingsForPlaceResponseDto
import com.example.learning_android.domain.model.PlaceReadings

fun GetReadingsForPlaceResponseDto.toDomain(): PlaceReadings {
  return PlaceReadings(
    placeId = placeId,
    readings = readings.map { it.toDomain() }
  )
}