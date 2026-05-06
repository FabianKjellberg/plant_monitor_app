package com.example.learning_android.ui.components.addDevice

import android.annotation.SuppressLint
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.learning_android.domain.model.AddDeviceState
import com.example.learning_android.viewmodels.AddDeviceViewModel

@Composable
@SuppressLint("MissingPermission")
fun DeviceFoundScreen(viewModel: AddDeviceViewModel) {
  Column(
    modifier = Modifier.fillMaxSize().padding(24.dp),
    verticalArrangement = Arrangement.Center,
    horizontalAlignment = Alignment.CenterHorizontally
  ) {
    Column(
      modifier = Modifier.fillMaxSize().padding(24.dp),
      verticalArrangement = Arrangement.Center,
      horizontalAlignment = Alignment.CenterHorizontally
    ) {
      Text("Device found", fontSize = 24.sp)

      Spacer(modifier = Modifier.height(8.dp))

      Text(
        text = viewModel.foundDevice.value?.name ?: "Plant Monitor",
        fontSize = 18.sp
      )

      Spacer(modifier = Modifier.height(4.dp))

      Text(
        text = "ID: ${viewModel.macAddr.value ?: "Loading..."}",
        fontSize = 14.sp,
        color = MaterialTheme.colorScheme.onSurfaceVariant
      )

      Spacer(modifier = Modifier.height(24.dp))

      Button(
        onClick = { viewModel.updateUiState(AddDeviceState.WIFI_INPUT) },
        enabled = viewModel.macAddr.value != null
      ) {
        Text("Continue")
      }
    }
  }
}