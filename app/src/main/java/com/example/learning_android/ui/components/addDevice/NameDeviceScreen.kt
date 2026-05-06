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
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.learning_android.domain.model.AddDeviceState
import com.example.learning_android.domain.model.EspWifiConnectionStatus
import com.example.learning_android.viewmodels.AddDeviceViewModel

@Composable
fun NameDeviceScreen(viewModel: AddDeviceViewModel) {
  val buttonEnabled = !viewModel.namingLoading.value && viewModel.userInputName.value != ""

  Column(
    modifier = Modifier.fillMaxSize(),
    horizontalAlignment = Alignment.CenterHorizontally,
    verticalArrangement = Arrangement.Center,
  ) {
    Text("What would you like to call this device?")
    Spacer(modifier = Modifier.height(32.dp))
    OutlinedTextField(
      label = { Text("Device name")},
      value = viewModel.userInputName.value,
      onValueChange = { viewModel.userInputName.value = it},
      enabled = !viewModel.namingLoading.value
    )

    Spacer(modifier = Modifier.height(16.dp))

    Button(
      onClick = { viewModel.createUserDevice(
        onSuccess = {
          viewModel.updateUiState(AddDeviceState.ROOM_SELECTION)
        }
      )},
      enabled = buttonEnabled
    ) {
      Text("continue")
    }

    Spacer(modifier = Modifier.height(12.dp))
    Text(
      text = viewModel.namingErrorText.value,
      color = MaterialTheme.colorScheme.error
    )
  }
}