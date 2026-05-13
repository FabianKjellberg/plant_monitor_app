package com.example.learning_android.domain.model.place

import java.time.LocalDate

data class DailyMetric(
  val temp: DailyMetricTemp,
  val humidity: DailyMetricHumidity,
  val light: DailyMetricLight,
  val date: LocalDate
)

data class DailyMetricTemp(
  val avgTemp: Float,
  val minTemp: Float,
  val maxTemp: Float,
)

data class DailyMetricHumidity(
  val avgHumidity: Float,
)

data class DailyMetricLight(
  val avgLux: Float,
)
