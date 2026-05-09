package com.example.learning_android.ui.components.dashboard

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.learning_android.domain.model.Device
import com.example.learning_android.ui.components.DeviceCard
import kotlin.collections.forEach

@Composable
fun DashboardDeviceContent (devices: List<Device>?, onClickCard: (deviceId: String) -> Unit) {
    if (devices == null) {
        Text("unable to fetch devices, Check back later or contact support")
    }
    else if (devices.isEmpty()) {
        Column(
            modifier = Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text("No devices", fontSize = 20.sp)
            Text("click the + to add a device")}
        }
    else {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .padding(horizontal = 12.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            devices.forEach { device ->
                DeviceCard(
                    device = device,
                    onClick = { onClickCard(device.id) }
                )
            }
            Spacer(Modifier.height(64.dp))
        }

    }
}