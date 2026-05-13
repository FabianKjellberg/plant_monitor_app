package com.example.learning_android.ui.screens

import android.util.Log
import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import com.example.learning_android.domain.model.AppPage
import com.example.learning_android.domain.model.DashboardNav
import com.example.learning_android.ui.components.dashboard.BottomNavBar
import com.example.learning_android.ui.components.dashboard.DashboardHeader
import com.example.learning_android.ui.components.dashboard.DashboardDeviceContent
import com.example.learning_android.ui.components.dashboard.DashboardPlacesContent
import com.example.learning_android.viewmodels.DashboardViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun Dashboard (
  viewModel: DashboardViewModel,
  navController: NavController
) {
  val deviceHome by viewModel.selectedDeviceHome.collectAsStateWithLifecycle()
  val home by viewModel.selectedHome.collectAsStateWithLifecycle()
  val isInitiallyLoading by viewModel.isInitialLoading.collectAsStateWithLifecycle()
  val homeId by viewModel.selectedHomeId.collectAsStateWithLifecycle()

  val nrOfDevices = deviceHome?.devices?.size?.toString() ?: '-'

  val deviceText = "Your devices ($nrOfDevices)"

  val placesText = home?.name ?: "Unknown"

  val headerText = when (viewModel.dashboardNav) {
    DashboardNav.DEVICES -> deviceText
    DashboardNav.PLACES -> placesText
  }

  Scaffold(
    topBar = {
      TopAppBar(
        title = { Text(headerText)},

      )
    }
  ) { innerPadding ->
    Column(
      modifier = Modifier
        .fillMaxSize()
        .background(MaterialTheme.colorScheme.background)
        .padding(top = innerPadding.calculateTopPadding())
    ) {
      Box(modifier = Modifier.weight(1f)) {
        if (isInitiallyLoading) {
          Column(
            modifier = Modifier.fillMaxSize().padding(bottom = 48.dp),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally,
          ) {
            CircularProgressIndicator(
              modifier = Modifier.size(80.dp),
              color = MaterialTheme.colorScheme.primary,
              strokeWidth = 8.dp
            )
          }
        } else {
          Column(
            modifier = Modifier
              .fillMaxSize()
              .padding(top = 12.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
          ) {
            AnimatedContent(
              targetState = viewModel.dashboardNav,
              transitionSpec = {

                if (targetState == DashboardNav.PLACES) {
                  slideInHorizontally { -it } togetherWith
                          slideOutHorizontally { it }
                } else {

                  slideInHorizontally { it } togetherWith
                          slideOutHorizontally { -it }
                }
              },
              label = "DashboardTransition"
            ) { targetNav ->
              when (targetNav) {
                DashboardNav.DEVICES ->
                  DashboardDeviceContent(
                    devices = deviceHome?.devices,
                    onClickCard = { deviceId ->
                      navController.navigate("${AppPage.DEVICE_PAGE.route}/${deviceId}")
                    }
                  )

                DashboardNav.PLACES ->
                  DashboardPlacesContent(
                    home = home,
                    onClickRoomCard = { roomId ->
                      navController.navigate("${AppPage.ROOM_PAGE.route}/${roomId}")
                    },
                    onClickPlaceCard = { placeId ->
                      navController.navigate("${AppPage.PLACE_PAGE.route}/${placeId}")
                    }
                  )
              }
            }
          }
        }


        Box(
          modifier = Modifier
            .fillMaxWidth()
            .align(Alignment.BottomCenter)
        ) {
          BottomNavBar(
            onNavClick = { nav -> viewModel.onChangeNav(nav) },
            nav = viewModel.dashboardNav,
            onAddClick = { nav ->
              Log.e("API_TEST", "home id in dashboard: $homeId")
              navController.navigate("${nav.route}/${homeId}")
            }
          )
        }
      }
    }
  }
}