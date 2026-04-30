package com.example.learning_android.data.remote.dto

data class GetDeviceReadingsResponseDto(
    val deviceId: String,
    val readings: List<DeviceReadingDto>,
)