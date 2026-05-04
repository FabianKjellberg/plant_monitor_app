package com.example.learning_android.viewmodels

import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.learning_android.business.AuthService
import com.example.learning_android.data.remote.client.ApiClient
import com.example.learning_android.data.remote.client.TokenManager
import com.example.learning_android.data.remote.dto.LoginRequestDto
import com.example.learning_android.ui.components.AppPage
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch

class LoginViewModel(): ViewModel() {
    var username by mutableStateOf("")
    var password by mutableStateOf("")

    var loading by mutableStateOf(false)

    private val _navigationEvent = Channel<AppPage>(Channel.CONFLATED)
    val navigationEvent = _navigationEvent.receiveAsFlow()

    fun onLoginClick() {
        viewModelScope.launch {
            loading = true;
            Log.e("API_TEST", "Starting call")
            try {
                val res = ApiClient.authApiService.login(
                    LoginRequestDto(username = username, password = password)
                )
                Log.e("API_TEST", "res: ${res}")
                val accessToken = res.accessToken
                TokenManager.saveAccessToken(ApiClient.getContext(), accessToken)
                _navigationEvent.send(AppPage.DASHBOARD)
            }
            catch (e: Exception) {
                Log.e("API_TEST", "Error: ${e.message}")
            }
            finally {
                loading = false
            }
        }
    }
}