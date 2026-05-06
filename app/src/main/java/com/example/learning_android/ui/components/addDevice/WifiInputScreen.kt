package com.example.learning_android.ui.components.addDevice

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.learning_android.domain.model.EspWifiConnectionStatus
import com.example.learning_android.viewmodels.AddDeviceViewModel

@Composable
fun WifiInputScreen(viewModel: AddDeviceViewModel) {

  val status = viewModel.wifiConnectionStatus.value

  val buttonText = when (status) {
    EspWifiConnectionStatus.BLE_STATUS_WAITING -> "Connect"
    EspWifiConnectionStatus.BLE_STATUS_FAILED -> "Retry"
    EspWifiConnectionStatus.BLE_STATUS_CONNECTING -> "Connecting..."
    EspWifiConnectionStatus.BLE_STATUS_SUCCESS -> "Connected"
    null -> "Connect"
  }

  val isBusy =
    status == EspWifiConnectionStatus.BLE_STATUS_CONNECTING ||
    status == EspWifiConnectionStatus.BLE_STATUS_SUCCESS

  Column(
    modifier = Modifier.fillMaxSize(),
    horizontalAlignment = Alignment.CenterHorizontally,
    verticalArrangement = Arrangement.Center,
  ) {
    Text("Enter the Wi-Fi network name and password your device should connect to.")
    Spacer(modifier = Modifier.height(16.dp))
    OutlinedTextField(
      label = { Text("Wi-Fi SSID")},
      value = viewModel.userInputSsid.value,
      onValueChange = { viewModel.userInputSsid.value = it},
      enabled = !isBusy
    )

    OutlinedTextField(
      label = { Text("Password")},
      value = viewModel.userInputPassword.value,
      onValueChange = {viewModel.userInputPassword.value = it},
      enabled = !isBusy
    )

    Button(
      onClick = { viewModel.sendWifiCredentials()},
      enabled = !isBusy
    ) {
      Text(buttonText)
    }

    Spacer(modifier = Modifier.height(12.dp))
    Text(
      text = if (status == EspWifiConnectionStatus.BLE_STATUS_FAILED)
        "Connection failed. Please check your details and try again."
      else
        "",
      color = MaterialTheme.colorScheme.error
    )
  }
}