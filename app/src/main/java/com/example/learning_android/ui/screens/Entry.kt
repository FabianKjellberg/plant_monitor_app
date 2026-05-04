package com.example.learning_android.ui.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.learning_android.ui.components.AppPage
import com.example.learning_android.viewmodels.EntryViewModel

@Composable
fun Entry(viewModel: EntryViewModel, navController: NavController) {

    LaunchedEffect(Unit) {
        viewModel.navigationEvent.collect { page ->
            navController.navigate(page.route) {
                popUpTo(AppPage.ENTRY.route) { inclusive = true }
            }
        }
    }

    Column(
        modifier = Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(16.dp, alignment = Alignment.CenterVertically)
    ) {
        Text(text = viewModel.loadingText)

        CircularProgressIndicator(
            strokeWidth = 8.dp,
            modifier = Modifier.size(80.dp)
        )
    }
}