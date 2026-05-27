package com.example.learning_android.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.scrollable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.calculateEndPadding
import androidx.compose.foundation.layout.calculateStartPadding
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Snackbar
import androidx.compose.material3.SnackbarDuration
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.LayoutDirection
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import com.android.identity.documenttype.Icon
import com.example.learning_android.R
import com.example.learning_android.repositories.IconResource
import com.example.learning_android.ui.components.devicePage.DevicePageDropdown
import com.example.learning_android.ui.components.placePage.HumidCard
import com.example.learning_android.ui.components.placePage.LightCard
import com.example.learning_android.ui.components.placePage.PlacePageDropdown
import com.example.learning_android.ui.components.placePage.SeasonalLightWheel
import com.example.learning_android.ui.components.placePage.TempCard
import com.example.learning_android.viewmodels.PlacePageViewModel
import kotlinx.coroutines.launch
import kotlin.math.min

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PlacePage(
  viewModel: PlacePageViewModel,
  navController: NavController,
){
  val place by viewModel.place.collectAsStateWithLifecycle()
  val manager by viewModel.dataManager.collectAsStateWithLifecycle()
  val snackbarHostState = remember { SnackbarHostState() }
  val scope = rememberCoroutineScope()

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
          title = {
            Row(
              verticalAlignment = Alignment.CenterVertically,
              horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
              if (place?.icon != null) {
                Icon(
                  painter = painterResource(
                    IconResource.getIconById(place?.icon).iconId
                  ),
                  contentDescription = "title icon"
                )
              }

              Text(
                text = place?.name ?: "Unknown",
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
              )
            }
          },
          navigationIcon = {
            IconButton(onClick = { navController.popBackStack() }) {
              Icon(painterResource(R.drawable.ic_arrow_left), "back")
            }
          },
          actions = {
            place?.let { currentPlace ->
              PlacePageDropdown(
                place = currentPlace,
                onChangeName = { name ->
                  viewModel.changeName(
                    name,
                    onSuccess = {
                      scope.launch {
                        snackbarHostState.showSnackbar(
                          message = "Changed name of place to $name",
                          duration = SnackbarDuration.Short
                        )
                      }
                    }
                  )
                },
                onDeletePlace = {
                  viewModel.deletePlace(
                    onSuccess = {
                      navController.popBackStack()
                    }
                  )
                },
                onChangeIcon = { iconId ->
                  viewModel.changeIcon(
                    iconId,
                    onSuccess = {
                      val iconName = IconResource.getIconById(iconId).name

                      scope.launch {
                        snackbarHostState.showSnackbar(
                          message = "Changed icon to $iconName",
                          duration = SnackbarDuration.Short
                        )
                      }
                    }
                  )
                }
              )
            }
          }
        )
      },
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
      }
    ) { paddingValues ->
      Column(
        modifier = Modifier
          .fillMaxSize()
          .padding(
            top = paddingValues.calculateTopPadding(),
            start = paddingValues.calculateStartPadding(LayoutDirection.Ltr),
            end = paddingValues.calculateEndPadding(LayoutDirection.Ltr))
          .padding(horizontal = 12.dp)
          .verticalScroll(rememberScrollState()),
        verticalArrangement = Arrangement.spacedBy(12.dp)
      ) {

        Spacer(Modifier.height(12.dp))
        Text(
          text = "ENVIRONMENTAL DATA",
          fontSize = 18.sp,
          fontWeight = FontWeight.Bold,
          color = MaterialTheme.colorScheme.onSurfaceVariant,
        )
        LightCard(
          manager = manager
        )
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
        SeasonalLightWheel(
          manager = manager
        )
        Spacer(Modifier.height(8.dp))
      }
    }
  }
}