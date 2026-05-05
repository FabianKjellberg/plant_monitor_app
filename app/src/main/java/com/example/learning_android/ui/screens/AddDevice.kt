package com.example.learning_android.ui.screens
import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.learning_android.domain.model.AddDeviceState
import com.example.learning_android.ui.components.addDevice.DeviceFoundScreen
import com.example.learning_android.ui.components.addDevice.NameDeviceScreen
import com.example.learning_android.ui.components.addDevice.ProvisioningScreen
import com.example.learning_android.ui.components.addDevice.ScanningScreen
import com.example.learning_android.ui.components.addDevice.SelectRoomScreen
import com.example.learning_android.ui.components.addDevice.SuccessScreen
import com.example.learning_android.ui.components.addDevice.WifiInputScreen
import com.example.learning_android.viewmodels.AddDeviceViewModel

@Composable
fun AddDevice(viewModel: AddDeviceViewModel) {
    viewModel.startScanning()

    Column(modifier = Modifier.fillMaxSize()) {
        Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.End) {
            Button(
                onClick = {}
            ) {
                Text("X")
            }
        }
    }
    AnimatedContent(
        targetState = viewModel.uiState,
        transitionSpec = {
            slideInVertically { it } togetherWith slideOutVertically { -it }
        }
    ) { step ->
        when(step) {
            AddDeviceState.SCANNING -> ScanningScreen(viewModel)
            AddDeviceState.DEVICE_FOUND -> DeviceFoundScreen(viewModel)
            AddDeviceState.WIFI_INPUT -> WifiInputScreen(viewModel)
            AddDeviceState.NAMING -> NameDeviceScreen(viewModel)
            AddDeviceState.ROOM_SELECTION -> SelectRoomScreen(viewModel)
            AddDeviceState.SUCCESS -> SuccessScreen(viewModel)
            AddDeviceState.PROVISIONING -> ProvisioningScreen(viewModel)
        }
    }
}