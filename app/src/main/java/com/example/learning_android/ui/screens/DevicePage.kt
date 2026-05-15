package com.example.learning_android.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import com.example.learning_android.R
import com.example.learning_android.repositories.IconResource
import com.example.learning_android.ui.components.devicePage.MetricDropDown
import com.example.learning_android.ui.components.SensorLineChart
import com.example.learning_android.ui.components.TimeRangeDropdown
import com.example.learning_android.ui.components.devicePage.DevicePageDropdown
import com.example.learning_android.ui.components.modals.SelectPlaceModal
import com.example.learning_android.viewmodels.DevicePageViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DevicePage(
  viewModel: DevicePageViewModel,
  navController: NavController
) {

  val device by viewModel.device.collectAsStateWithLifecycle()
  val assignedPlace by viewModel.assignedPlace.collectAsStateWithLifecycle()
  var assignModalOpen by remember { mutableStateOf(false) }

  Scaffold(
    topBar = {
      TopAppBar(
        title = { Text(device?.name ?: "Unknown") },
        navigationIcon = {
          IconButton(onClick = { navController.popBackStack() }) {
            Icon(painterResource(R.drawable.ic_arrow_left), "back")
          }
        },
        actions = {
          DevicePageDropdown(
            device?.name ?: "unknown",
            onChangeName = { name ->
              viewModel.updateDeviceName(name)
            }
          )
        }
      )
    }
  ) { innerPadding ->
    Column(
      modifier = Modifier
        .fillMaxSize()
        .padding(innerPadding)
        .padding(12.dp)
        .background(MaterialTheme.colorScheme.background),
      horizontalAlignment = Alignment.CenterHorizontally,
      verticalArrangement = Arrangement.Top
    ) {
      MetricDropDown(
        readingType = viewModel.readingType,
        updateReadingType = { type -> viewModel.updateReadingType(type) }
      )

      Spacer(Modifier.height(20.dp))

      SensorLineChart(
        title = viewModel.readingType.displayName,
        dataPoints = viewModel.chartValue ?: emptyList(),
        from = viewModel.fromDate,
        to = viewModel.toDate,
        readingType = viewModel.readingType,
      )

      Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(8.dp)
      ) {
        TimeRangeDropdown(
          label = "From",
          selectedTime = viewModel.fromDate,
          onTimeChange = { time -> viewModel.updateFromDate(time) },
          modifier = Modifier.weight(1f)
        )

        TimeRangeDropdown(
          label = "To",
          selectedTime = viewModel.toDate,
          onTimeChange = { time -> viewModel.updateToDate(time) },
          modifier = Modifier.weight(1f)
        )
      }
      Spacer(Modifier.height(8.dp))
      Box {
        OutlinedTextField(
          value = assignedPlace?.name ?: "",
          onValueChange = {},
          label = { Text("Assign to place") },
          readOnly = true,
          modifier = Modifier.fillMaxWidth(),
          leadingIcon = assignedPlace?.icon?.let { icon ->
            {
              Icon(
                painterResource(IconResource.getIconById(icon).iconId),
                contentDescription = null
              )
            }
          },
          trailingIcon = {
            Icon(
              painterResource(R.drawable.ic_chevron_down),
              contentDescription = null
            )
          }
        )
        Box(modifier = Modifier
          .matchParentSize()
          .clickable(onClick = {assignModalOpen = true})
        )
      }
    }
    if(device != null) {
      SelectPlaceModal(
        isOpen = assignModalOpen,
        deviceId = device!!.id,
        onDismiss = { assignModalOpen = false },
      )
    }
  }
}