package com.example.learning_android.ui.components

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.rememberNavController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.example.learning_android.ui.screens.Dashboard
import com.example.learning_android.ui.screens.DevicePage
import com.example.learning_android.ui.screens.Entry
import com.example.learning_android.ui.screens.Home
import com.example.learning_android.ui.screens.Login
import com.example.learning_android.viewmodels.DashboardViewModel
import com.example.learning_android.viewmodels.DevicePageViewModel
import com.example.learning_android.viewmodels.EntryViewModel
import com.example.learning_android.viewmodels.LoginViewModel

enum class AppPage(val route: String) {
    ENTRY("entry"),
    ADD_DEVICE("add_device"),
    DASHBOARD("dashboard"),
    LOGIN("login"),
    REGISTER("register"),
    DEVICE_PAGE("device_page")

}

@Composable
fun AppNavigation() {
    val navController = rememberNavController()

    NavHost(
        navController = navController,
        startDestination = AppPage.ENTRY.route,
    ) {
        composable(route = AppPage.ENTRY.route) {
            val viewModel = remember {
                EntryViewModel(
                    onRedirect = { nav ->
                        navController.navigate(nav) {
                            popUpTo(AppPage.ENTRY.route) {
                                inclusive = true
                            }
                        }
                    }
                )
            }

            Entry(
                viewModel = viewModel,

            )
        }

        composable(route = AppPage.LOGIN.route) {
            val viewModel = remember { LoginViewModel() }

            Login(
                onLoginSuccess = {
                    navController.navigate(AppPage.DASHBOARD.route) {
                        popUpTo(AppPage.LOGIN.route) {
                            inclusive = true
                        }
                    }
                },
                viewModel = viewModel
            )
        }

        composable( route = AppPage.DASHBOARD.route) {
            val viewModel = remember { DashboardViewModel() }
            viewModel.loadDevices();

            Dashboard(
                viewModel = viewModel,
                navController = navController
            )
        }

        composable( route = "${AppPage.DEVICE_PAGE.route}/{deviceId}" ) {
            val viewModel: DevicePageViewModel = viewModel()
            viewModel.loadData();

            DevicePage(
                viewModel = viewModel,
                navController = navController
            )
        }
    }
}
