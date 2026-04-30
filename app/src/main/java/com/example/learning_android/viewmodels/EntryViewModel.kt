package com.example.learning_android.viewmodels

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.learning_android.data.remote.client.ApiClient
import com.example.learning_android.ui.components.AppPage
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class EntryViewModel(val onRedirect: (nav: AppPage) -> Unit) : ViewModel() {

    var loadingText by mutableStateOf("Authenticating")
        private set

    init {
        startDotsAnimation()
        checkAuthentication()
    }

    private fun startDotsAnimation() {
        viewModelScope.launch {
            val baseText = "Authenticating"
            var dotCount = 0

            while (true) {
                loadingText = baseText + ".".repeat(dotCount)
                dotCount = (dotCount + 1) % 4
                delay(300)
            }
        }
    }

    private fun checkAuthentication() {
        viewModelScope.launch {
            val res = ApiClient.authApiService.testAuth()

            if (res.isSuccessful) {
                onRedirect(AppPage.DASHBOARD)
            } else {
                onRedirect(AppPage.LOGIN)
            }
        }
    }
}