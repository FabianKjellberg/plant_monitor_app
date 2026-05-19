package com.example.learning_android.ui.components.dashboard


import androidx.compose.animation.animateContentSize
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.spring
import com.example.learning_android.R
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
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
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.example.learning_android.domain.model.DetailedHomePlace
import com.example.learning_android.domain.model.DetailedHomeRoom
import com.example.learning_android.repositories.IconResource

@Composable
fun RoomCard(
  room: DetailedHomeRoom,
  onClickPlaceCard: (placeId: String) -> Unit,
  onDeleteRoom: () -> Unit,
  onAddPlace: () -> Unit,
  onRenameRoom: (name: String) -> Unit
) {
  val nrOfDevices = room.places.sumOf { place ->  place.devices.size }

  Box(
    modifier = Modifier
      .fillMaxWidth()
      .clip(RoundedCornerShape(12.dp))
      .background(
        brush = Brush.verticalGradient(
          colors = listOf(
            MaterialTheme.colorScheme.surfaceVariant,
            MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.6F)
          )
        )
      )
      .padding(10.dp)
      .padding(horizontal = 8.dp)
      .animateContentSize(
      animationSpec = spring(
        dampingRatio = Spring.DampingRatioLowBouncy,
        stiffness = Spring.StiffnessLow
      )
    )
  ) {
    Column(
      modifier = Modifier.fillMaxWidth()
    ) {
      Row(
        Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
      ){
        Row(
          horizontalArrangement = Arrangement.spacedBy(4.dp),
          verticalAlignment = Alignment.CenterVertically
        ) {
          if(room.icon != null){
            Icon(
              painter = painterResource(IconResource.getIconById(room.icon).iconId),
              contentDescription = "icon"
            )
          }
          Text(
            text = room.name,
            style = MaterialTheme.typography.titleLarge,
          )
          Text(
            text = "($nrOfDevices)",
            style = MaterialTheme.typography.labelLarge,
            color = MaterialTheme.colorScheme.secondary
          )
        }
      RoomCardDropDownMenu(
        onDeleteRoom = {
          onDeleteRoom()
                       },
        onRenameRoom = { name -> onRenameRoom(name) },
        onAddPlace = { onAddPlace() },
        roomName = room.name
        )
      }
      if(room.places.isEmpty()) {
        Text("Tap '+' or '...' to add a place to this room.")
      }
      else {
        Column(
          modifier = Modifier.fillMaxWidth(),
          verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
          Spacer(Modifier.height(4.dp))
          room.places.forEach { place ->
            PlaceCard(
              place = place,
              onClick = { placeId ->
                onClickPlaceCard(placeId)
              }
            )
          }
        }
      }
    }
  }
}