package com.example.learning_android.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import com.example.learning_android.R
import com.example.learning_android.repositories.IconResource
import com.example.learning_android.ui.components.devicePage.DevicePageDropdown
import com.example.learning_android.viewmodels.PlacePageViewModel

@Composable
fun PlacePage(
  viewModel: PlacePageViewModel,
  navController: NavController
){
  val place by viewModel.place.collectAsStateWithLifecycle()

  if(place == null ){
    Box(
      modifier = Modifier
        .fillMaxSize()
        .background(MaterialTheme.colorScheme.background),
      contentAlignment = Alignment.Center
    ) {
      CircularProgressIndicator()
    }
  }
  else {
    Column(
      modifier = Modifier
        .fillMaxSize()
        .padding(12.dp)
        .padding(top = 20.dp)
        .background(MaterialTheme.colorScheme.background),
      horizontalAlignment = Alignment.CenterHorizontally,
      verticalArrangement = Arrangement.Top
    ) {
      Row(
        modifier = Modifier.padding(bottom = 20.dp),
        verticalAlignment = Alignment.CenterVertically
      ) {
        IconButton(
          onClick = { navController.popBackStack()}
        ) {
          Icon(
            painter = painterResource(R.drawable.ic_arrow_left),
            contentDescription = "go-back"
          )
        }
        Spacer(Modifier.width(12.dp))
        if(place?.icon != null) {
          Icon(
            painter = painterResource(IconResource.getIconById(place?.icon).iconId),
            contentDescription = "icon"
          )
          Spacer(Modifier.width(4.dp))
        }
        Text(
          text = place?.name ?: "Unknown",
          fontSize = 24.sp,
          modifier = Modifier.weight(1F)
        )
        DevicePageDropdown(
          place?.name ?: "unknown",
          onChangeName = { name ->
            {  }
          }
        )
      }
    }
  }
}