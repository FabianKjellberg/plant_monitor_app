package com.example.learning_android.viewmodels

import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.learning_android.data.remote.client.ApiClient
import com.example.learning_android.domain.model.DashboardNav
import com.example.learning_android.domain.model.DetailedHome
import com.example.learning_android.domain.model.DetailedHomeRoom
import com.example.learning_android.domain.model.DeviceHome
import com.example.learning_android.repositories.DeviceRepository
import com.example.learning_android.repositories.HomeRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.flow.mapLatest
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class DashboardViewModel : ViewModel() {
  private val _selectedHomeId = MutableStateFlow<String?>(null)
  val selectedHomeId = _selectedHomeId.asStateFlow()

  private val _addPlaceRoom = MutableStateFlow<DetailedHomeRoom?>(null)
  val addPlaceRoom = _addPlaceRoom.asStateFlow()

  fun setAddPlaceRoom(room: DetailedHomeRoom?) {
    _addPlaceRoom.value = room
  }

  var dashboardNav by mutableStateOf(DashboardNav.PLACES)
    private set

  fun onChangeNav(nav: DashboardNav) {
    dashboardNav = nav;
  }

  init {
    viewModelScope.launch {
      launch {
        DeviceRepository.deviceHomes.collect { homes ->
          if (_selectedHomeId.value == null && homes.isNotEmpty()) {
            _selectedHomeId.value = homes.first().id
          }
        }
      }
    }
  }

  val selectedDeviceHome: StateFlow<DeviceHome?> =
    DeviceRepository.deviceHomes.combine(_selectedHomeId) {
      deviceHomes, selHomeId ->
        deviceHomes.find { home ->
          home.id == selHomeId
      }
    }
    .stateIn(
      scope = viewModelScope,
      started = SharingStarted.WhileSubscribed(5000),
      initialValue = null
    )

  val selectedHome: StateFlow<DetailedHome?> =
    HomeRepository.homes.combine(_selectedHomeId) {
      homes, selHomeId ->
        homes.find { home ->
          home.id == selHomeId
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

  fun renameRoom(roomId: String, name: String, onSuccess: () -> Unit) {
    viewModelScope.launch {
      val renamed = HomeRepository.renameRoom(roomId, name)
      if(renamed) onSuccess()
    }
  }

  fun deleteRoom(roomId: String, onSuccess: () -> Unit){
    viewModelScope.launch {
      Log.e("API_TEST", "got here :)")

      val deleted = HomeRepository.deleteRoom(roomId)
      if(deleted) onSuccess()
    }
  }
}