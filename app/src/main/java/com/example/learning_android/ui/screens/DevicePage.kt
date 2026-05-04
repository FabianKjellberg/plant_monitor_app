package com.example.learning_android.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
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
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.learning_android.R
import com.example.learning_android.ui.components.MetricDropDown
import com.example.learning_android.ui.components.SensorLineChart
import com.example.learning_android.ui.components.TimeRangeDropdown
import com.example.learning_android.ui.components.devicePage.DevicePageDropdown
import com.example.learning_android.viewmodels.DevicePageViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DevicePage(
    viewModel: DevicePageViewModel,
    navController: NavController
){

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
            Text(
                text = viewModel.device?.deviceName ?: "Unknown",
                fontSize = 24.sp,
                modifier = Modifier.weight(1F)
            )
            DevicePageDropdown(viewModel.device?.deviceName ?: "unknown", onChangeName = {})
        }

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
    }
}