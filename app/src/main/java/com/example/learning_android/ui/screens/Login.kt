package com.example.learning_android.ui.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Button
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import com.example.learning_android.viewmodels.LoginViewModel

@Composable
fun Login(onLoginSuccess: () -> Unit, viewModel: LoginViewModel){
    if(viewModel.isLoggedIn) {
        onLoginSuccess();
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

        Button(onClick = { viewModel.onLoginClick() }){
            Text("Login")
        }
    }
}