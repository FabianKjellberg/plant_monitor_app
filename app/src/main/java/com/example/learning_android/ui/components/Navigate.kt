package com.example.learning_android.ui.components

import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.rememberNavController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.example.learning_android.domain.model.AppPage
import com.example.learning_android.ui.screens.AddDevice
import com.example.learning_android.ui.screens.AddPlace
import com.example.learning_android.ui.screens.Dashboard
import com.example.learning_android.ui.screens.DevicePage
import com.example.learning_android.ui.screens.Entry
import com.example.learning_android.ui.screens.Login
import com.example.learning_android.ui.screens.PlacePage
import com.example.learning_android.ui.screens.RoomPage
import com.example.learning_android.viewmodels.AddDeviceViewModel
import com.example.learning_android.viewmodels.AddPlaceViewModel
import com.example.learning_android.viewmodels.DashboardViewModel
import com.example.learning_android.viewmodels.DevicePageViewModel
import com.example.learning_android.viewmodels.EntryViewModel
import com.example.learning_android.viewmodels.LoginViewModel
import com.example.learning_android.viewmodels.PlacePageViewModel
import com.example.learning_android.viewmodels.RoomPageViewModel

@Composable
fun AppNavigation() {
    val navController = rememberNavController()

    NavHost(
        navController = navController,
        startDestination = AppPage.ENTRY.route,
    ) {
        composable(route = AppPage.ENTRY.route) {
            val viewModel = remember {
                EntryViewModel()
            }
            Entry(
                viewModel = viewModel,
                navController = navController
            )
        }

        composable(route = AppPage.LOGIN.route) {
            val viewModel = remember { LoginViewModel() }

            Login(
                navController = navController,
                viewModel = viewModel
            )
        }

        composable( route = AppPage.DASHBOARD.route) {
            val viewModel = remember { DashboardViewModel() }

            Dashboard(
                viewModel = viewModel,
                navController = navController
            )
        }

        composable( route = "${AppPage.DEVICE_PAGE.route}/{deviceId}" ) {
            val viewModel: DevicePageViewModel = viewModel()

            DevicePage(
                viewModel = viewModel,
                navController = navController
            )
        }

        composable ( route = "${AppPage.ROOM_PAGE.route}/{roomId}") {
            val viewModel: RoomPageViewModel = viewModel()

            RoomPage(
                viewModel = viewModel,
                navController = navController
            )
        }

        composable ( route = "${AppPage.PLACE_PAGE.route}/{placeId}") {
            val viewModel: PlacePageViewModel = viewModel()

            PlacePage(
                viewModel = viewModel,
                navController = navController
            )
        }

        composable( route = "${AppPage.ADD_DEVICE.route}/{homeId}") {
            val viewModel: AddDeviceViewModel = viewModel()

            AddDevice(viewModel, navController)
        }

        composable( route = "${AppPage.ADD_PLACE.route}/{homeId}") {
            val viewModel: AddPlaceViewModel = viewModel()

            AddPlace(viewModel, navController)
        }
    }
}
