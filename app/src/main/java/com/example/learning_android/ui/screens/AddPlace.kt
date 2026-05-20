package com.example.learning_android.ui.screens

import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
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
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Snackbar
import androidx.compose.material3.SnackbarDuration
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.learning_android.R
import com.example.learning_android.domain.model.SelectionState
import com.example.learning_android.ui.components.SelectionCard
import com.example.learning_android.viewmodels.AddPlaceViewModel
import androidx.compose.ui.text.input.ImeAction
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.learning_android.repositories.HomeRepository
import com.example.learning_android.repositories.IconResource
import com.example.learning_android.ui.components.IconPreviewButton
import com.example.learning_android.ui.components.RoomDropDownMenu
import kotlinx.coroutines.launch

enum class SelectedCard {
  NONE,
  ADD_ROOM,
  ADD_PLACE,
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddPlace(
  viewModel: AddPlaceViewModel,
  navController: NavController,
) {
  var selectedCard by remember {
    mutableStateOf (SelectedCard.NONE)
  }

  val addRoomState: SelectionState = when (selectedCard) {
    SelectedCard.NONE -> SelectionState.UNSELECTED
    SelectedCard.ADD_ROOM -> SelectionState.SELECTED
    SelectedCard.ADD_PLACE -> SelectionState.SHRUNK
  }
  val addPlaceState: SelectionState = when (selectedCard) {
    SelectedCard.NONE -> SelectionState.UNSELECTED
    SelectedCard.ADD_ROOM -> SelectionState.SHRUNK
    SelectedCard.ADD_PLACE -> SelectionState.SELECTED
  }

  val snackbarHostState = remember { SnackbarHostState() }
  val scope = rememberCoroutineScope()
  val keyboardController = LocalSoftwareKeyboardController.current

  val sheetState = rememberModalBottomSheetState()
  var showIconSheet by remember { mutableStateOf(false) }

  val addRoomButtonEnabled =
    !viewModel.busy &&
    viewModel.userInputRoomName != ""
  val addPlaceButtonEnabled =
    !viewModel.busy &&
    viewModel.userInputPlaceName != "" &&
    viewModel.selectedRoom != null

  val rooms = viewModel.rooms.collectAsStateWithLifecycle()

  val onDismissEnabled =
    !viewModel.busy && selectedCard != SelectedCard.NONE


  Scaffold(
    snackbarHost = {
      Box(modifier = Modifier
        .fillMaxSize()
        ){
        SnackbarHost(
          hostState = snackbarHostState,
          modifier = Modifier.align(Alignment.TopCenter).padding(top = 84.dp)
        ) { data ->
          Snackbar(
            containerColor = MaterialTheme.colorScheme.primary,
            contentColor = MaterialTheme.colorScheme.background,
            snackbarData = data
          )
        }
      }
    },
    containerColor = MaterialTheme.colorScheme.background
  ) { paddingValues ->
    Column(modifier = Modifier
      .fillMaxSize()
      .padding(20.dp)
      .padding(top = 0.dp)
      .padding(paddingValues)
      .clickable(
        interactionSource = remember { MutableInteractionSource() },
        indication = null,
        enabled = onDismissEnabled,
        onClick = { selectedCard = SelectedCard.NONE }
      )
    ) {
      Row(
        modifier = Modifier.padding(bottom = 20.dp),
        verticalAlignment = Alignment.CenterVertically
      ) {
        IconButton(
          onClick = { navController.popBackStack() }
        ) {
          Icon(
            painter = painterResource(R.drawable.ic_arrow_left),
            contentDescription = "go-back"
          )
        }
        Text(
          text = "Add a new place",
          fontSize = 24.sp,
          modifier = Modifier.weight(1F)
        )
      }
      Spacer(Modifier.weight(0.5F))
      SelectionCard(
        onClick = { selectedCard = SelectedCard.ADD_ROOM },
        title = "Add a room",
        subtitle = "Create a space to organize your home, like a kitchen or living room.",
        state = addRoomState,
        enabled = !viewModel.busy,
      ){
        Column(modifier = Modifier.fillMaxWidth()) {
          Row(
            modifier = Modifier.fillMaxWidth(),
          ) {
            IconPreviewButton(
              modifier = Modifier.padding(top = 8.dp),
              onClick = { showIconSheet = true },
              selectedIconId = viewModel.userInputRoomIconId
            )
            Spacer(Modifier.width(8.dp))
            OutlinedTextField(
              label = {Text("Room name")},
              value = viewModel.userInputRoomName,
              onValueChange = { viewModel.userInputRoomName = it},
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
              viewModel.addRoom { roomName ->
                selectedCard = SelectedCard.NONE
                scope.launch {
                  snackbarHostState.showSnackbar(
                    message = "${roomName} added to your home",
                    duration = SnackbarDuration.Short
                  )
                }
              }
            },
            enabled = addRoomButtonEnabled
          ) {
            Text("Add room")
          }
        }
      }
      Spacer(modifier = Modifier.height(20.dp))
      SelectionCard(
        onClick = { selectedCard = SelectedCard.ADD_PLACE },
        title = "Add a place",
        subtitle = "Define specific spots within a room to monitor, like a window or shelf.",
        state = addPlaceState,
        enabled = !viewModel.busy && rooms.value.isNotEmpty(),
      ){
        Column(modifier = Modifier.fillMaxWidth()) {
          RoomDropDownMenu(
            rooms = rooms.value,
            selectedRoom = viewModel.selectedRoom,
            onSelect = { room -> viewModel.selectedRoom = room }
          )
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
                selectedCard = SelectedCard.NONE
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
      }
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