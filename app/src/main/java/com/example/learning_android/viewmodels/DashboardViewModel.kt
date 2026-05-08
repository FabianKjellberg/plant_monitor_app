package com.example.learning_android.viewmodels

import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.learning_android.data.mapper.toDomain
import com.example.learning_android.data.remote.client.ApiClient
import com.example.learning_android.domain.model.DashboardNav
import com.example.learning_android.domain.model.DeviceHome
import com.example.learning_android.repositories.DeviceRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.WhileSubscribed
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class DashboardViewModel : ViewModel() {
  private val _selectedHomeId = MutableStateFlow<String?>(null)
  val selectedHomeId: StateFlow<String?> = _selectedHomeId.asStateFlow()

  val selectedDeviceHome: StateFlow<DeviceHome?> =
    DeviceRepository.deviceHomes.combine(_selectedHomeId) {
      deviceHomes, selHomeId ->
        if(selHomeId == null && deviceHomes.isNotEmpty()) {
          deviceHomes.first()
        }
        else {
          deviceHomes.find { home -> home.id == selHomeId }
        }
    }
    .stateIn(
      scope = viewModelScope,
      started = SharingStarted.WhileSubscribed(5000),
      initialValue = null
    )

  val isInitialLoading: StateFlow<Boolean> = combine(
    DeviceRepository.isRefetching,
    selectedDeviceHome
  ) { isRefetching, currentHome ->
    isRefetching && currentHome == null
  }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), true)

  var dashboardNav by mutableStateOf(DashboardNav.DEVICES)
    private set

  fun onChangeNav(nav: DashboardNav) {
    dashboardNav = nav;
  }
}