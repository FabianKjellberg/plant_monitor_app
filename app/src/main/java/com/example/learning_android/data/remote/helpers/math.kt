package com.example.learning_android.data.remote.helpers

fun List<Float?>.averageOrNull(): Float? {
  val filtered = this.filterNotNull()
  if (filtered.isEmpty()) return null
  return filtered.average().toFloat()
}