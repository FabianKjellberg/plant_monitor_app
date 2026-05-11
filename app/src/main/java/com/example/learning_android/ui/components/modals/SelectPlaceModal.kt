package com.example.learning_android.ui.components.modals

import androidx.compose.foundation.layout.Arrangement
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
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.learning_android.repositories.IconResource
import com.example.learning_android.ui.components.AssignDeviceButton
import com.example.learning_android.viewmodels.modals.SelectPlaceModalViewModel
import com.example.learning_android.viewmodels.modals.SelectPlaceUIState

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SelectPlaceModal(
  isOpen: Boolean,
  deviceId: String,
  onDismiss: () -> Unit,
  viewModel: SelectPlaceModalViewModel = viewModel()
) {
  LaunchedEffect(deviceId) {
    viewModel.initialize(deviceId)
  }

  val sheetState = rememberModalBottomSheetState()

  val uiState by viewModel.uiState.collectAsStateWithLifecycle()
  val busy by viewModel.isBusy.collectAsStateWithLifecycle()

  if(isOpen) {
    ModalBottomSheet(
      onDismissRequest = onDismiss,
      sheetState = sheetState,
      containerColor = MaterialTheme.colorScheme.surface
    ) {
      Column(
        modifier = Modifier
          .padding(start = 20.dp, end = 20.dp, bottom = 20.dp)

      ) {
        when(val s = uiState) {
          is SelectPlaceUIState.Loading -> {
            CircularProgressIndicator()
          }
          is SelectPlaceUIState.Success -> {
            Text(
              text = "Assign ${s.device.name} to",
              style = MaterialTheme.typography.titleLarge
            )
            Spacer(Modifier.height(24.dp))
            LazyColumn(
              verticalArrangement = Arrangement.spacedBy(8.dp)
            ){
              s.home.rooms.forEach { room ->
                if(room.places.isNotEmpty()) {
                  item(room.id) {
                    Row(
                      modifier = Modifier.fillMaxWidth(),
                      verticalAlignment = Alignment.CenterVertically
                    ) {
                      if(room.icon != null) {
                        Icon(
                          painterResource(IconResource.getIconById(room.icon).iconId),
                          contentDescription = "icon",
                          modifier = Modifier.size(22.dp)
                        )
                      }
                      Spacer(Modifier.width(4.dp))
                      Text(
                        text = room.name,
                        style = MaterialTheme.typography.titleMedium,
                        fontSize = 18.sp
                      )
                    }
                  }
                  items(
                    items = room.places,
                    key = { place -> place.id }
                  ) { place ->
                    AssignDeviceButton(
                      place = place,
                      deviceId = deviceId,
                      onClick = {viewModel.assignDeviceToPlace(place.id)},
                      deviceType = s.device.type,
                      busy = busy
                    )
                  }
                }
              }
            }
          }
          is SelectPlaceUIState.Failed -> {
            Text(s.message)
          }
        }
      }
    }
  }
}