package com.example.learning_android.data.remote.helpers


fun batteryPercentFromMv(mv: Int?): Int? {
  if(mv == null) return null

  val points = listOf(
    4200 to 100,
    4100 to 90,
    4000 to 80,
    3900 to 65,
    3800 to 50,
    3700 to 35,
    3600 to 20,
    3500 to 10,
    3300 to 0
  )

  if (mv >= points.first().first) return 100
  if (mv <= points.last().first) return 0

  for (i in 0 until points.size - 1) {
    val (mvHigh, pctHigh) = points[i]
    val (mvLow, pctLow) = points[i + 1]

    if (mv in mvLow..mvHigh) {
      val ratio = (mv - mvLow).toFloat() / (mvHigh - mvLow)
      return (pctLow + ratio * (pctHigh - pctLow)).toInt()
    }
  }

  return null
}