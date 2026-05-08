package com.example.learning_android.ui.screens
import android.Manifest
import android.bluetooth.BluetoothAdapter
import android.content.Intent
import android.os.Build
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
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
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.learning_android.R
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
fun AddDevice(viewModel: AddDeviceViewModel, navController: NavController) {
    val context = LocalContext.current

    val permissions = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
        arrayOf(
            Manifest.permission.BLUETOOTH_SCAN,
            Manifest.permission.BLUETOOTH_CONNECT
        )
    } else {
        arrayOf(
            Manifest.permission.ACCESS_FINE_LOCATION
        )
    }

    val permissionLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestMultiplePermissions()
    ) { result ->
        if (result.values.all { it }) {
            viewModel.startScanning()
        } else {
            navController.popBackStack()
        }
    }

    val enableBluetoothLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.StartActivityForResult()
    ) { result ->
        if (viewModel.isBluetoothEnabled()) {
            viewModel.startScanning()
        } else {
            navController.popBackStack()
        }
    }

    LaunchedEffect(Unit) {
        when {
            !viewModel.hasBluetoothPermission(context) -> {
                permissionLauncher.launch(permissions)
            }

            !viewModel.isBluetoothEnabled() -> {
                val intent = Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE)
                enableBluetoothLauncher.launch(intent)
            }

            else -> {
                viewModel.startScanning()
            }
        }
    }

    Box(modifier = Modifier.fillMaxSize()) {
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

        IconButton(
            modifier = Modifier.padding(vertical = 24.dp, horizontal = 4.dp).align(Alignment.TopEnd),
            onClick = { navController.popBackStack()}
        ) {
            Icon(
                painter = painterResource(R.drawable.ic_x_smaller),
                contentDescription = "go-back"
            )
        }
    }
}