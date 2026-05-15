package com.example.learning_android.ui.components.dashboard

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
import com.example.learning_android.ui.components.devicePage.ChangeNameDialog

@Composable
fun RoomCardDropDownMenu(
  roomName: String,
  onDeleteRoom: () -> Unit,
  onAddPlace: () -> Unit,
  onRenameRoom: (name: String) -> Unit
) {
  var menuExpanded by remember { mutableStateOf(false) }
  var showChangeNameMenu by remember { mutableStateOf(false) }

  Box {
    IconButton(onClick = { menuExpanded = true }) {
      Icon(
        painterResource(R.drawable.ic_dots_three),
        contentDescription = "Room options"
      )
    }

    DropdownMenu(
      expanded = menuExpanded,
      onDismissRequest = { menuExpanded = false },
      containerColor = MaterialTheme.colorScheme.surface
    ) {
      DropdownMenuItem(
        text = { Text("Add Place") },
        onClick = {
          menuExpanded = false
          onAddPlace()
        },
        leadingIcon = { Icon(painterResource(R.drawable.ic_plus), null) }
      )
      DropdownMenuItem(
        text = { Text("Rename Room") },
        onClick = {
          menuExpanded = false
          showChangeNameMenu = true
        },
        leadingIcon = { Icon(painterResource(R.drawable.ic_gear), null) }
      )
      DropdownMenuItem(
        text = {
          Text("Delete", color = MaterialTheme.colorScheme.error)
        },
        onClick = {
          menuExpanded = false
          onDeleteRoom()
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
      currentName = roomName,
      onConfirm = { name ->
        onRenameRoom(name)
        showChangeNameMenu = false
      },
      title = "Change room name"
    )
  }
}
