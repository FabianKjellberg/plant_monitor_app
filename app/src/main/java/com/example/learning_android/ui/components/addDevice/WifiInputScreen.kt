package com.example.learning_android.ui.components.addDevice

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Button
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import com.example.learning_android.domain.model.EspWifiConnectionStatus
import com.example.learning_android.viewmodels.AddDeviceViewModel

@Composable
fun WifiInputScreen(viewModel: AddDeviceViewModel) {
  Column(
    modifier = Modifier.fillMaxSize(),
    horizontalAlignment = Alignment.CenterHorizontally,
    verticalArrangement = Arrangement.Center,
  ) {

    OutlinedTextField(
      label = { Text("Wifi-SSID")},
      value = viewModel.userInputSsid.value,
      onValueChange = { viewModel.userInputSsid.value = it}
    )

    OutlinedTextField(
      label = { Text("Password")},
      value = viewModel.userInputPassword.value,
      onValueChange = {viewModel.userInputPassword.value = it}
    )

    Button(
      onClick = { viewModel.sendWifiCredentials()},
      enabled = viewModel.wifiConnectionStatus.value !=
              EspWifiConnectionStatus.BLE_STATUS_CONNECTING) {
      Text("try connection")
    }
  }
}