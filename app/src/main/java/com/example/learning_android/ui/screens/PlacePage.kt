package com.example.learning_android.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
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
import com.example.learning_android.ui.components.placePage.HumidCard
import com.example.learning_android.ui.components.placePage.LightCard
import com.example.learning_android.ui.components.placePage.TempCard
import com.example.learning_android.viewmodels.PlacePageViewModel
import kotlin.math.min

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PlacePage(
  viewModel: PlacePageViewModel,
  navController: NavController,
){
  val place by viewModel.place.collectAsStateWithLifecycle()
  val manager by viewModel.dataManager.collectAsStateWithLifecycle()

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
    Scaffold(
      topBar = {
        TopAppBar(
          title = { Text(place?.name ?: "Unknown") },
          navigationIcon = {
            IconButton(onClick = { navController.popBackStack() }) {
              Icon(painterResource(R.drawable.ic_arrow_left), "back")
            }
          },
          actions = {
            DevicePageDropdown(
              place?.name ?: "unknown",
              onChangeName = { name ->
                {  }
              }
            )
          }
        )
      }
    ) { paddingValues ->
      Column(
        modifier = Modifier
          .fillMaxSize()
          .padding(paddingValues)
          .padding(12.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
      ) {
        Text("Pot plant data:")
        Spacer(Modifier.height(8.dp))
        LightCard()
        Row(
          horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
          TempCard(
            manager = manager,
            modifier = Modifier.weight(1F))
          HumidCard(
            manager = manager,
            modifier = Modifier.weight(1F)
          )
        }
      }
    }
  }
}