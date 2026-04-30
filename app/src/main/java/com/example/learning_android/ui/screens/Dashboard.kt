package com.example.learning_android.ui.screens

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
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.learning_android.domain.model.DashboardNav
import com.example.learning_android.ui.components.AppPage
import com.example.learning_android.ui.components.dashboard.BottomNavBar
import com.example.learning_android.ui.components.dashboard.DashboardHeader
import com.example.learning_android.ui.components.DeviceCard
import com.example.learning_android.ui.components.dashboard.DashboardDeviceContent
import com.example.learning_android.viewmodels.DashboardViewModel

@Composable
fun Dashboard (
    viewModel: DashboardViewModel,
    navController: NavController
) {
    val nrOfDevices = viewModel.devices?.size?.toString() ?: '-'

    val deviceText = "Your devices ($nrOfDevices)"

    val placesText = "Your places (0)"

    val headerText = when (viewModel.dashboardNav) {
        DashboardNav.DEVICES -> deviceText
        DashboardNav.PLACES -> placesText
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
            .padding(top = 32.dp)
    ) {
        DashboardHeader(
            headertext = headerText
        )

        Box(modifier = Modifier.weight(1f)) {
            if(viewModel.loadingDevices) {
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
            }
            else {
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .verticalScroll(rememberScrollState())
                        .padding(top = 20.dp, bottom = 80.dp)
                        .padding(horizontal = 12.dp),
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {

                    DashboardDeviceContent(
                        devices = viewModel.devices,
                        onClickCard = {
                            deviceId -> navController.navigate("${AppPage.DEVICE_PAGE.route}/${deviceId}")
                        }
                    )
                }

                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(20.dp)
                        .background(
                            brush = Brush.verticalGradient(
                                colors = listOf(
                                    MaterialTheme.colorScheme.background,
                                    Color.Transparent
                                )
                            )
                        )
                )
            }


            Row(
                modifier = Modifier.fillMaxSize(),
                verticalAlignment = Alignment.Bottom

            ) {
                BottomNavBar(
                    onNavClick = { nav -> viewModel.onChangeNav(nav) },
                    nav = viewModel.dashboardNav
                )
            }
        }
    }
}