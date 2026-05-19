package com.example.learning_android.ui.components.placePage

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
import com.example.learning_android.ui.components.devicePage.ChangeNameDialog

@Composable
fun PlacePageDropdown(
  placeName: String,
  onChangeName: (name: String) -> Unit,
  onDeletePlace: () -> Unit,
) {
  var showMenu by remember { mutableStateOf(false) }
  var showChangeNameMenu by remember { mutableStateOf(false) }
  var showDeletePlace by remember { mutableStateOf(false)}

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
          Text("Change place name")
        },
        onClick = {
          showMenu = false
          showChangeNameMenu = true
        },
        leadingIcon = { Icon(painterResource(R.drawable.ic_gear),null) },
      )
      DropdownMenuItem(
        text = {
          Text("Delete place", color = MaterialTheme.colorScheme.error)
        },
        onClick = {
          showMenu = false
          showDeletePlace = true
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
      currentName = placeName,
      onConfirm = { name ->
        onChangeName(name)
        showChangeNameMenu = false
      },
      title = "Change place name"
    )
  }
  if(showDeletePlace) {
    DeleteDialog(
      onDismiss = {
        showDeletePlace = false
      },
      itemName = placeName,
      onConfirm = {
        onDeletePlace()
        showDeletePlace = false
      },
      title = "Delete place"
    )
  }
}