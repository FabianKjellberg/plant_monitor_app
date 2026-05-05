package com.example.learning_android.domain.model

enum class EspWifiConnectionStatus(val code: String) {
  BLE_STATUS_WAITING("0"),
  BLE_STATUS_SUCCESS("1"),
  BLE_STATUS_CONNECTING("2"),
  BLE_STATUS_FAILED("3");

  companion object {
    fun fromCode(code: String): EspWifiConnectionStatus {
      return values().find { it.code == code } ?: BLE_STATUS_WAITING
    }
  }
}