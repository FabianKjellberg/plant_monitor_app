package com.example.learning_android.ui.components.placePage

import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import com.example.learning_android.domain.model.DetailedHomeDevice

@Composable
fun PlacePageDevices(
  devices: List<DetailedHomeDevice>,
  onClickDevice: (deviceId: String) -> Unit
) {
  Text(
    text = "ASSIGNED DEVICES",
    fontSize = 18.sp,
    fontWeight = FontWeight.Bold,
    color = MaterialTheme.colorScheme.onSurfaceVariant,
  )

  if(devices.isEmpty()) {
    Text("There is currently no devices")
  }

  devices.forEach { device ->
    PlacePageDeviceCard(
      device,
      onClick = {
        onClickDevice(device.id)
      }
    )
  }

  Button(
    onClick = {}
  ) {
    Text("Add device to this place")
  }
}