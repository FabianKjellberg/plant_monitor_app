package com.example.learning_android.repositories

import com.example.learning_android.domain.model.PotDevice


object DeviceRepository {
    var devices: List<PotDevice> = emptyList()

    fun getDeviceById(id: String): PotDevice? {
        return devices.find { it.deviceId == id }
    }

    fun updateDeviceName(id: String, name: String) {
        devices.map { device ->
            if (device.deviceId == id) {
                device.copy(name = name)
            }
            else {
                device
            }
        }
    }
}
