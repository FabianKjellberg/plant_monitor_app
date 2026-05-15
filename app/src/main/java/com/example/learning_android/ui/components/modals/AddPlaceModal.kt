package com.example.learning_android.ui.components.modals

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.GridItemSpan
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.SnackbarDuration
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.learning_android.domain.model.DetailedHomeRoom
import com.example.learning_android.repositories.IconResource
import com.example.learning_android.ui.components.IconPreviewButton
import com.example.learning_android.ui.components.RoomDropDownMenu
import com.example.learning_android.ui.screens.SelectedCard
import com.example.learning_android.viewmodels.modals.AddPlaceModalViewModel
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddPlaceModal(
  homeId: String,
  isOpen: Boolean,
  room: DetailedHomeRoom?,
  onDismiss: () -> Unit,
  viewModel: AddPlaceModalViewModel = viewModel()
) {
  LaunchedEffect(room) {
    viewModel.initialize(room, homeId)
  }

  val sheetState = rememberModalBottomSheetState()

  var showIconSheet by remember { mutableStateOf(false) }
  val rooms = viewModel.rooms.collectAsStateWithLifecycle()

  Column(modifier = Modifier.fillMaxWidth()) {
    if(viewModel.isRoomSelectionEnabled) {
      RoomDropDownMenu(
        rooms = rooms.value,
        selectedRoom = viewModel.selectedRoom,
        onSelect = { room -> viewModel.selectedRoom = room }
      )
    }
    Spacer(Modifier.height(4.dp))
    Row(
      modifier = Modifier.fillMaxWidth(),
    ) {
      IconPreviewButton(
        modifier = Modifier.padding(top = 8.dp),
        onClick = { showIconSheet = true },
        selectedIconId = viewModel.userInputPlaceIconId
      )
      Spacer(Modifier.width(8.dp))
      OutlinedTextField(
        label = {Text("Place name")},
        value = viewModel.userInputPlaceName,
        onValueChange = { viewModel.userInputPlaceName = it},
        singleLine = true,
        keyboardOptions = KeyboardOptions(
          imeAction = ImeAction.Done
        ),
        keyboardActions = KeyboardActions(
          onDone = {
            keyboardController?.hide()
          }
        )
      )
    }
    Spacer(Modifier.height(8.dp))
    Button(
      onClick = {
        viewModel.addPlace { placeName ->
          scope.launch {
            snackbarHostState.showSnackbar(
              message = "${placeName} added to your home",
              duration = SnackbarDuration.Short
            )
          }
        }
      },
      enabled = addPlaceButtonEnabled
    ) {
      Text("Add Place")
    }
  }
  if (showIconSheet) {
    ModalBottomSheet(
      onDismissRequest = { showIconSheet = false },
      sheetState = sheetState,
      containerColor = MaterialTheme.colorScheme.surfaceContainerLow
    ) {
      LazyVerticalGrid(
        columns = GridCells.Fixed(4),
        modifier = Modifier.padding(16.dp).height(300.dp)
      ) {
        IconResource.groupedIcons.forEach { category, icons ->
          item(span = { GridItemSpan(4) }) {
            Text(
              text = category,
              style = MaterialTheme.typography.titleMedium,
              color = MaterialTheme.colorScheme.primary,
            )
          }
          items(icons) { icon ->
            IconButton(
              onClick = {
                if (selectedCard == SelectedCard.ADD_ROOM)
                  viewModel.userInputRoomIconId = icon.id
                else
                  viewModel.userInputPlaceIconId = icon.id

                showIconSheet = false
              },
              modifier = Modifier.size(64.dp)
            ) {
              Icon(painterResource(icon.iconId), contentDescription = null)
            }
          }
        }
      }
    }
  }
}