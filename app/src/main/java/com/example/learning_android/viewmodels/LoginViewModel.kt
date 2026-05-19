package com.example.learning_android.viewmodels

import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.learning_android.data.remote.client.ApiClient
import com.example.learning_android.data.remote.client.TokenManager
import com.example.learning_android.data.remote.dto.LoginRequestDto
import com.example.learning_android.domain.model.AppPage
import com.example.learning_android.repositories.DeviceRepository
import com.example.learning_android.repositories.HomeRepository
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import org.json.JSONObject

class LoginViewModel(): ViewModel() {
  var email by mutableStateOf("")
  var password by mutableStateOf("")

  private val _errorMessage = MutableStateFlow<String?>(null)
  val errorMessage = _errorMessage.asStateFlow()

  fun setErrorMessage(message: String) {
    _errorMessage.value = message
    viewModelScope.launch {
      delay(5000)
      _errorMessage.value = null
    }
  }

  var loading by mutableStateOf(false)

  private val _navigationEvent = Channel<AppPage>(Channel.CONFLATED)
  val navigationEvent = _navigationEvent.receiveAsFlow()



  fun onLoginClick() {
    viewModelScope.launch {
      loading = true;
      Log.e("API_TEST", "Starting call")
      try {
        val body = LoginRequestDto(username = email, password = password)
        val res = ApiClient.authApiService.login(body)

        if(!res.isSuccessful) {
          val errorJsonString = res.errorBody()?.string()

          if (!errorJsonString.isNullOrEmpty()) {
            try {
              val jsonObject = JSONObject(errorJsonString)
              setErrorMessage(jsonObject.getString("error"))
            } catch (e: Exception) {
              setErrorMessage("An unexpected error occurred")
            }
          } else {
            setErrorMessage(res.message())
          }
        }
        else {
          Log.e("API_TEST", "login success")
          val accessToken = res.body()?.accessToken ?: ""
          TokenManager.saveAccessToken(ApiClient.getContext(), accessToken)

          DeviceRepository.startAutoRefetch()
          HomeRepository.startAutoRefetch()

          _navigationEvent.send(AppPage.DASHBOARD)
        }
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