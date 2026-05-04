package com.example.learning_android.ui.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Button
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.navigation.NavController
import com.example.learning_android.domain.model.AppPage
import com.example.learning_android.viewmodels.LoginViewModel

@Composable
fun Login(navController: NavController, viewModel: LoginViewModel){
    LaunchedEffect(Unit) {
        viewModel.navigationEvent.collect { page: AppPage ->
            navController.navigate(page.route) {
                popUpTo(AppPage.LOGIN.route) { inclusive = true }
            }
        }
    }

    Column(
        modifier = Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center,
    ) {

        OutlinedTextField(
            label = {Text("Username")},
            value = viewModel.username,
            onValueChange = { viewModel.username = it}
        )

        OutlinedTextField(
            label = {Text("Password")},
            value = viewModel.password,
            onValueChange = {viewModel.password = it}
        )

        Button(
            onClick = { viewModel.onLoginClick() },
            enabled = !viewModel.loading
        ){
            Text("Login")
        }
    }
}