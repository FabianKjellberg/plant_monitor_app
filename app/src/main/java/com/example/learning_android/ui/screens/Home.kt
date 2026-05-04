package com.example.learning_android.ui.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.learning_android.domain.model.AppPage

@Composable
fun Home(navigate: (AppPage) -> Unit) {
    Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.spacedBy(48.dp, alignment = Alignment.CenterVertically)
    ) {
        Column(
            modifier = Modifier.fillMaxWidth().weight(2f),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally,
        ){
            Text(text="Welcome to my plant project", fontSize = 24.sp)
        }

        Column(
            modifier = Modifier.fillMaxWidth().weight(1f),
            verticalArrangement = Arrangement.spacedBy(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
        ){
            Button(onClick = {navigate(AppPage.LOGIN)}) {
                Text(text = "Login", fontSize = 18.sp)
            }

            Button(onClick = {println("clicked button")}) {
                Text(text = "Register", fontSize = 18.sp)
            }
        }
    }
}