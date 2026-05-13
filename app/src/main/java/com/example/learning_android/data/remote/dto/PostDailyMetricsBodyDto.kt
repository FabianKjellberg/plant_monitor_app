package com.example.learning_android.data.remote.dto

import com.example.learning_android.domain.model.place.DailyMetric

data class PostDailyMetricsBodyDto (
  val placeId: String,
  val dailyMetrics: List<DailyMetric>
)
