package com.example.learning_android.viewmodels

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import com.example.learning_android.business.AuthService

class LoginViewModel(
    private val authService: AuthService = AuthService()
) {
    var username by mutableStateOf("")
    var password by mutableStateOf("")
    var errorMessage by mutableStateOf<String?>(null)
    var isLoggedIn by mutableStateOf(false)

    fun onLoginClick() {
        val success = authService.login(username, password)

        if(!success) {
            errorMessage = "failed to login"
            isLoggedIn = false
        }
        else {
            errorMessage = null
            isLoggedIn = true
        }
    }
}