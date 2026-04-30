package com.example.learning_android.ui.components.dashboard

import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import com.example.learning_android.domain.model.PotDevice
import com.example.learning_android.ui.components.AppPage
import com.example.learning_android.ui.components.DeviceCard
import kotlin.collections.forEach

@Composable
fun DashboardDeviceContent (devices: List<PotDevice>?, onClickCard: (deviceId: String) -> Unit) {
    if (devices == null) {
        Text("unable to fetch devices, Check back later or context support")
    }
    else if (devices.isEmpty()) {
        Text("You have no devices, click the + to add a device")
    }
    else {
        devices.forEach { device ->
            DeviceCard(
                device = device,
                onClick = { onClickCard(device.deviceId) }
            )
        }
    }
}