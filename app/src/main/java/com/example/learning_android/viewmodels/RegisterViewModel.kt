package com.example.learning_android.viewmodels

import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.ui.text.toLowerCase
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.learning_android.data.remote.client.ApiClient
import com.example.learning_android.data.remote.dto.RegisterRequestDto
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class RegisterViewModel : ViewModel(){

  private val _busy = MutableStateFlow(false)
  val busy = _busy.asStateFlow()

  var passMatch by mutableStateOf(false)
    private set
  var passLength by mutableStateOf(false)
    private set
  var passUpper by mutableStateOf(false)
    private set
  var passLower by mutableStateOf(false)
    private set
  var passSpecial by mutableStateOf(false)
    private set
  var passDigit by mutableStateOf(false)
    private set

  var securePassword by mutableStateOf(true)
    private set

  var emailValue by mutableStateOf("")
  var passValue by mutableStateOf("")
  var repeatPassValue by mutableStateOf("")
  var homeNameValue by mutableStateOf("")

  private val emailRegex = "^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}\$".toRegex()

  val isRegisterEnabled: Boolean
    get() {
      val isEmailValid = emailValue.isNotBlank() && emailRegex.matches(emailValue)
      val isHomeNameValid = homeNameValue.trim().isNotEmpty()

      return securePassword && isEmailValid && isHomeNameValid && !busy.value
    }

  fun onPasswordChange() {
    passMatch = passValue.isNotEmpty() && passValue == repeatPassValue
    passLength = passValue.length >= 8
    passUpper = passValue.any {char -> char.isUpperCase()}
    passLower = passValue.any {char -> char.isLowerCase()}
    passDigit = passValue.any {char -> char.isDigit()}
    passSpecial = passValue.any {char -> !char.isDigit() && !char.isLetter()}

    securePassword =
      passMatch &&
      passLength &&
      passUpper &&
      passLower &&
      passDigit &&
      passSpecial
  }

  fun onRegisterClick(onSuccess: () -> Unit) {
    _busy.value = true

    viewModelScope.launch {
      val username = emailValue.lowercase().trim()
      val password = passValue
      val homeName = homeNameValue.trim()

      try {
        val body = RegisterRequestDto(username, password, homeName, null)
        val res = ApiClient.authApiService.register(body)

        if(res.isSuccessful) {
          onSuccess()
        }
        else {
          Log.e("API_TEST", "failed creating account ${res.message()}")
        }
      }
      catch (e: Exception) {
        Log.e("API_TEST", "Failed creating account ${e.message}")
      }
      finally {
        _busy.value = false
      }
    }
  }
}