package com.example.learning_android.data.remote.helpers

private const val OFFSET_MV = 180

fun batteryPercentFromMv(mv: Int?): Float? {
  return mv?.let { mv ->
    val corrected = mv + OFFSET_MV
    ((corrected - 3300f) / (4200f - 3300f))
      .coerceIn(0f, 1f) * 100f
  }
}