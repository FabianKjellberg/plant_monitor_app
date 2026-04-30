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

@Composable
fun DevicePageDropdown(
    deviceName: String,
    onChangeName: (name: String) -> Unit
) {
    var showMenu by remember { mutableStateOf(false) }
    var showChangeNameMenu by remember { mutableStateOf(false) }

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
                    }
                )
                DropdownMenuItem(
                    text = {
                        Text("Forget device")
                    },
                    onClick = {showMenu = false}
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
            }
        )
    }

}