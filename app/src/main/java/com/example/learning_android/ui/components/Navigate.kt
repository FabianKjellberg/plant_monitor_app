package com.example.learning_android.ui.components

import androidx.compose.animation.AnimatedContentTransitionScope
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.rememberNavController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.example.learning_android.data.remote.client.AuthEvent
import com.example.learning_android.data.remote.client.SessionHandler
import com.example.learning_android.domain.model.AppPage
import com.example.learning_android.ui.screens.AddDevice
import com.example.learning_android.ui.screens.AddPlace
import com.example.learning_android.ui.screens.Dashboard
import com.example.learning_android.ui.screens.DevicePage
import com.example.learning_android.ui.screens.Entry
import com.example.learning_android.ui.screens.Login
import com.example.learning_android.ui.screens.PlacePage
import com.example.learning_android.ui.screens.Register
import com.example.learning_android.viewmodels.AddDeviceViewModel
import com.example.learning_android.viewmodels.AddPlaceViewModel
import com.example.learning_android.viewmodels.DashboardViewModel
import com.example.learning_android.viewmodels.DevicePageViewModel
import com.example.learning_android.viewmodels.EntryViewModel
import com.example.learning_android.viewmodels.LoginViewModel
import com.example.learning_android.viewmodels.PlacePageViewModel
import com.example.learning_android.viewmodels.RegisterViewModel

@Composable
fun AppNavigation() {
  val navController = rememberNavController()

  LaunchedEffect(Unit) {
    SessionHandler.events.collect { event ->
      when(event) {
        AuthEvent.SessionExpired -> {
          navController.navigate(AppPage.LOGIN.route) {
            popUpTo(0) { inclusive = true }
          }
        }
      }
    }
  }

  NavHost(
    navController = navController,
    startDestination = AppPage.ENTRY.route,

    enterTransition = {
      slideIntoContainer(AnimatedContentTransitionScope.SlideDirection.Left, tween(350))
    },
    exitTransition = {
      slideOutOfContainer(AnimatedContentTransitionScope.SlideDirection.Left, tween(350))
    },
    popEnterTransition = {
      slideIntoContainer(AnimatedContentTransitionScope.SlideDirection.Right, tween(350))
    },
    popExitTransition = {
      slideOutOfContainer(AnimatedContentTransitionScope.SlideDirection.Right, tween(350))
    }
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

    composable(
      route = AppPage.LOGIN.route,
      enterTransition = {
        val initialRoute = initialState.destination.route

        val direction = if(initialRoute == AppPage.DASHBOARD.route)
          AnimatedContentTransitionScope.SlideDirection.Right
        else
          AnimatedContentTransitionScope.SlideDirection.Left

        slideIntoContainer(
          direction,
          animationSpec = tween(350)
        )
      }
    ) {
      val viewModel = remember { LoginViewModel() }

      Login(
        navController = navController,
        viewModel = viewModel
      )
    }

    composable(route = AppPage.REGISTER.route) {
      val viewModel = remember { RegisterViewModel() }

      Register(
        navController = navController,
        viewModel = viewModel
      )
    }

    composable(
      route = AppPage.DASHBOARD.route,
      exitTransition = {
        val targetRoute = targetState.destination.route
        val direction = if (targetRoute == AppPage.LOGIN.route)
          AnimatedContentTransitionScope.SlideDirection.Right
        else
          AnimatedContentTransitionScope.SlideDirection.Left

        slideOutOfContainer(direction, tween(350))
      },
    ) {
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
