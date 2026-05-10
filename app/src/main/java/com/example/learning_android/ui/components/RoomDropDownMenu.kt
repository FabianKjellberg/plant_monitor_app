package com.example.learning_android.ui.components

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.MenuDefaults
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import com.example.learning_android.domain.model.DetailedHomeRoom

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RoomDropDownMenu(
  rooms: List<DetailedHomeRoom>,
  selectedRoom: DetailedHomeRoom?,
  onSelect: (room: DetailedHomeRoom) -> Unit
) {
  var expanded by remember { mutableStateOf(false) }

  ExposedDropdownMenuBox(
    expanded = expanded,
    onExpandedChange = {expanded = !expanded},
    modifier = Modifier.fillMaxWidth()
  ) {
    OutlinedTextField(
      value = selectedRoom?.name ?: "Select Room",
      onValueChange = {},
      readOnly = true,
      label = {Text ("Room")},
      trailingIcon = {
        ExposedDropdownMenuDefaults.TrailingIcon(expanded = expanded)
      },
      colors = ExposedDropdownMenuDefaults.outlinedTextFieldColors(),
      modifier = Modifier
        .menuAnchor()
        .fillMaxWidth()
    )

    ExposedDropdownMenu(
      expanded = expanded,
      onDismissRequest = { expanded = false },
      containerColor = MaterialTheme.colorScheme.surfaceVariant
    ) {
      rooms.forEach { room ->
        DropdownMenuItem(
          text = { Text(room.name) },
          onClick = {
            expanded = false
            onSelect(room)
          },
        )
      }
    }
  }
}