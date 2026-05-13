package com.example.learning_android.domain.model.place

import java.time.LocalDate

data class DailyMetric(
  val avgTemp: Float,
  val minTemp: Float,
  val maxTemp: Float,
  val avgHumidity: Float,
  val avgLux: Float,
  val dli: Float,
  val peakPpfd: Float,
  val date: LocalDate,
  val totalReadings: Int
)
