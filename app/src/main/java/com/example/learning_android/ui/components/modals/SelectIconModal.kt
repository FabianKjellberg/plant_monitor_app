package com.example.learning_android.ui.components.modals

import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.GridItemSpan
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.example.learning_android.repositories.IconResource

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SelectIconModal (
  onDismiss: () -> Unit,
  onClick: (iconId: String) -> Unit,
  selectedIcon: String?
) {
  val sheetState = rememberModalBottomSheetState()

  ModalBottomSheet(
    onDismissRequest = { onDismiss() },
    sheetState = sheetState,
    containerColor = MaterialTheme.colorScheme.surfaceContainerLow
  ) {
    LazyVerticalGrid(
      columns = GridCells.Fixed(4),
      modifier = Modifier.padding(16.dp).height(300.dp)
    ) {
      IconResource.groupedIcons.forEach { category, icons ->
        item(span = { GridItemSpan(4)}) {
          Text(
            text = category,
            style = MaterialTheme.typography.titleMedium,
            color = MaterialTheme.colorScheme.primary
          )
        }
        items(icons) { icon ->
          IconButton(
            onClick = {
              onClick(icon.id)
              onDismiss()
            },
            modifier = Modifier.size(64.dp),
            enabled = selectedIcon != icon.id
          ) {
            Icon(painterResource(icon.iconId), contentDescription = null)
          }
        }
      }
    }
  }
}