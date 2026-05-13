package com.example.learning_android.ui.components.dashboard

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.learning_android.domain.model.DetailedHome

@Composable
fun DashboardPlacesContent(
  home: DetailedHome?,
  onClickPlaceCard: (placeId: String) -> Unit
) {

  if(home == null) {
    Text("unable to fetch your home, Check back later or contact support")
  }
  else if(home.rooms.isEmpty()) {
    Column(
      modifier = Modifier.fillMaxSize(),
      verticalArrangement = Arrangement.Center,
      horizontalAlignment = Alignment.CenterHorizontally
    ) {
      Text("No Places", fontSize = 20.sp)
      Text("click the + to add a Place")}
  }
  else {
    Column(
      modifier = Modifier
        .fillMaxSize()
        .padding(horizontal = 12.dp)
        .verticalScroll(rememberScrollState()),
      verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
      home.rooms.forEach { room ->
        RoomCard(
          room,
          onClickPlaceCard = { placeId -> onClickPlaceCard(placeId)},
          onAddPlace = {},
          onDeleteRoom = {},
          onRenameRoom = {},
        )
      }
      Spacer(Modifier.height(72.dp))
    }
  }
}
