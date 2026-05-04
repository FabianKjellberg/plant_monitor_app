package com.example.learning_android.ui.screens

import android.annotation.SuppressLint
import android.bluetooth.BluetoothDevice
import android.os.Build
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.learning_android.viewmodels.AddDeviceViewModel

@Composable
fun AddDevice(viewModel: AddDeviceViewModel) {
    val context = LocalContext.current

    val bluetoothPermissions = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
        arrayOf(
            android.Manifest.permission.BLUETOOTH_SCAN,
            android.Manifest.permission.BLUETOOTH_CONNECT
        )
    } else {
        arrayOf(
            android.Manifest.permission.ACCESS_FINE_LOCATION
        )
    }

    val permissionLauncher = rememberLauncherForActivityResult(
        ActivityResultContracts.RequestMultiplePermissions()
    ) { permissions ->
        val allGranted = permissions.values.all { it }
        if (allGranted) {
            viewModel.startDiscovery()
        } else {
            Toast.makeText(context, "Permissions required for scanning", Toast.LENGTH_SHORT).show()
        }
    }

    Column(
        modifier = Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Button(onClick = {
            permissionLauncher.launch(bluetoothPermissions)
        }) {
            Text("Scan for Devices")
        }

        viewModel.foundDevices.forEach { device ->
            DeviceItem(device)
        }
    }
}

@SuppressLint("MissingPermission")
@Composable
fun DeviceItem(device: BluetoothDevice) {
    Column(modifier = Modifier.padding(8.dp)) {
        Text(text = device.name ?: "Unknown Device", fontSize = 18.sp)
        Text(text = device.address, fontSize = 12.sp, color = Color.Gray)
    }
}