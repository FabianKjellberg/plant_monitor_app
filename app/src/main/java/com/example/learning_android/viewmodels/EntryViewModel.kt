package com.example.learning_android.viewmodels

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.learning_android.data.remote.client.ApiClient
import com.example.learning_android.domain.model.AppPage
import com.example.learning_android.repositories.DeviceRepository
import com.example.learning_android.repositories.HomeRepository
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch

class EntryViewModel : ViewModel() {

    var loadingText by mutableStateOf("Authenticating")
        private set

    private val _navigationEvent = Channel<AppPage>(Channel.CONFLATED)
    val navigationEvent = _navigationEvent.receiveAsFlow()

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
            try {
                val res = ApiClient.authApiService.testAuth()
                if (res.isSuccessful) {
                    HomeRepository.startAutoRefetch()
                    DeviceRepository.startAutoRefetch()
                    _navigationEvent.send(AppPage.DASHBOARD)
                } else {
                    _navigationEvent.send(AppPage.LOGIN)
                }
            } catch (e: Exception) {
                _navigationEvent.send(AppPage.LOGIN)
            }
        }
    }
}