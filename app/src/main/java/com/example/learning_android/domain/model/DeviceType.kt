package com.example.learning_android.domain.model

import com.google.gson.annotations.SerializedName

enum class DeviceType(val value: String) {
  @SerializedName("pot")
  POT("pot"),

  @SerializedName("unknown")
  UNKNOWN("unknown");

  companion object {
    fun fromString(value: String?): DeviceType {
      return entries.find { it.value == value } ?: UNKNOWN
    }
  }
}