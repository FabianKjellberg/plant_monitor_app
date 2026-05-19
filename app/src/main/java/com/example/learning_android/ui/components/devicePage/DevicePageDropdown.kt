package com.example.learning_android.ui.components.devicePage

import androidx.compose.foundation.layout.Box
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.res.painterResource
import com.example.learning_android.R
import com.example.learning_android.ui.components.DeleteDialog

@Composable
fun DevicePageDropdown(
  deviceName: String,
  onChangeName: (name: String) -> Unit,
  onForgetDevice: () -> Unit
) {
  var showMenu by remember { mutableStateOf(false) }
  var showChangeNameMenu by remember { mutableStateOf(false) }
  var showForgetDevice by remember { mutableStateOf(false)}

  Box(

  ) {
    IconButton(
      onClick = { showMenu = !showMenu}
    ) {
      Icon(
        painter = painterResource(R.drawable.ic_dots_three),
        contentDescription = "device-menu"
      )
    }


    DropdownMenu(
      expanded = showMenu,
      onDismissRequest = { showMenu = false },
      containerColor = MaterialTheme.colorScheme.surfaceVariant
    ) {
      DropdownMenuItem(
        text = {
          Text("Change device name")
        },
        onClick = {
          showMenu = false
          showChangeNameMenu = true
        },
        leadingIcon = { Icon(painterResource(R.drawable.ic_gear),null) },
      )
      DropdownMenuItem(
        text = {
          Text("Forget device", color = MaterialTheme.colorScheme.error)
        },
        onClick = {
          showMenu = false
          showForgetDevice = true
        },
        leadingIcon = {
          Icon(
            painterResource(R.drawable.ic_trash),
            contentDescription = null,
            tint = MaterialTheme.colorScheme.error
          )
        }
      )
    }

  }
  if(showChangeNameMenu) {
    ChangeNameDialog(
      onDismiss = {
        showChangeNameMenu = false
      },
      currentName = deviceName,
      onConfirm = { name ->
        onChangeName(name)
        showChangeNameMenu = false
      },
      title = "Change device name"
    )
  }
  if(showForgetDevice) {
    DeleteDialog(
      onDismiss = {
        showForgetDevice = false
      },
      itemName = deviceName,
      onConfirm = {
        onForgetDevice()
        showForgetDevice = false
      },
      title = "Forget device"
    )
  }
}