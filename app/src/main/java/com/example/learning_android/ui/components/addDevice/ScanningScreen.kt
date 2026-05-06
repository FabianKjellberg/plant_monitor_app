package com.example.learning_android.ui.components.addDevice

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.blur
import androidx.compose.ui.unit.dp
import com.example.learning_android.viewmodels.AddDeviceViewModel

@Composable
fun ScanningScreen(viewModel: AddDeviceViewModel) {
  Column(
    modifier = Modifier.fillMaxSize().padding(24.dp),
    verticalArrangement = Arrangement.Center,
    horizontalAlignment = Alignment.CenterHorizontally
  ) {
    Text("Hold the device button for 5 seconds to start pairing mode.")
    Spacer(modifier = Modifier.height(32.dp))
    Text(text = "Scanning for devices")
    Spacer(modifier = Modifier.height(32.dp))
    CircularProgressIndicator(
      strokeWidth = 8.dp,
      modifier = Modifier.size(80.dp)
    )
  }

}