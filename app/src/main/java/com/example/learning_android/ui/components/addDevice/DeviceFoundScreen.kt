package com.example.learning_android.ui.components.addDevice

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.learning_android.domain.model.AddDeviceState
import com.example.learning_android.viewmodels.AddDeviceViewModel

@Composable
fun DeviceFoundScreen(viewModel: AddDeviceViewModel) {
  Column(
    modifier = Modifier.fillMaxSize().padding(24.dp),
    verticalArrangement = Arrangement.Center,
    horizontalAlignment = Alignment.CenterHorizontally
  ) {
    Text("Device found")

    Button(onClick = {viewModel.updateUiState(AddDeviceState.WIFI_INPUT)}) {
      Text("Configure Wifi Settings")
    }
  }
}