package com.example.learning_android.data.remote.helpers

import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.unit.IntSize
import kotlin.math.atan2

fun List<Float?>.averageOrNull(): Float? {
  val filtered = this.filterNotNull()
  if (filtered.isEmpty()) return null
  return filtered.average().toFloat()
}

fun calculateAngle(hit: Offset, size: IntSize): Float {
  val centerX = size.width / 2f
  val centerY = size.height / 2f

  val angleRad = atan2(hit.y - centerY, hit.x - centerX)

  var angleDeg = Math.toDegrees(angleRad.toDouble()).toFloat()
  angleDeg += 90f

  if (angleDeg < 0) angleDeg += 360f

  return angleDeg
}

fun getMonthName(month: Int): String {
  return when (month) {
    1 -> "JANUARY"
    2 -> "FEBRUARY"
    3 -> "MARCH"
    4 -> "APRIL"
    5 -> "MAY"
    6 -> "JUNE"
    7 -> "JULY"
    8 -> "AUGUST"
    9 -> "SEPTEMBER"
    10 -> "OCTOBER"
    11 -> "NOVEMBER"
    12 -> "DECEMBER"
    else -> ""
  }
}