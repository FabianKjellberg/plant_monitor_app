package com.example.learning_android.viewmodels.modals

import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.graphics.Path.Companion.combine
import androidx.compose.ui.text.style.TextDecoration.Companion.combine
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.learning_android.data.remote.client.ApiClient
import com.example.learning_android.data.remote.dto.AssignDeviceToPlaceDto
import com.example.learning_android.domain.model.DetailedHome
import com.example.learning_android.domain.model.DetailedHomePlace
import com.example.learning_android.domain.model.Device
import com.example.learning_android.repositories.DeviceRepository
import com.example.learning_android.repositories.HomeRepository
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

sealed interface SelectPlaceUIState {
  object Loading : SelectPlaceUIState

  data class Success (
    val home: DetailedHome,
    val device: Device
  ) : SelectPlaceUIState

  data class Failed (
    val message: String
  ) : SelectPlaceUIState
}

class SelectPlaceModalViewModel : ViewModel() {
  private val _deviceId = MutableStateFlow<String?>(null)

  private val _isBusy = MutableStateFlow(false)
  val isBusy = _isBusy.asStateFlow();

  @OptIn(ExperimentalCoroutinesApi::class)
  val uiState: StateFlow<SelectPlaceUIState> = _deviceId.flatMapLatest { id ->
    if (id == null) {
      flowOf(SelectPlaceUIState.Loading)
    } else {
      DeviceRepository.getDeviceFromId(id).flatMapLatest { device ->
        if (device == null) {
          flowOf(SelectPlaceUIState.Failed("Device not found"))
        } else {
          DeviceRepository.getHomeIdFromDevice(id).flatMapLatest { homeId ->
            if (homeId == null) {
              flowOf(SelectPlaceUIState.Failed("Home not found"))
            } else {
              HomeRepository.getHomeFromId(homeId).map { home ->
                if(home == null) {
                  SelectPlaceUIState.Failed("Home not found from repo")
                }
                else {
                  val sortedRooms = home.rooms.map { room ->
                    room.copy(
                      places = room.places.sortedWith(
                        compareByDescending<DetailedHomePlace> { place ->
                          place.devices.none { roomDevice -> roomDevice.type == device.type }
                        }.thenBy { it.name }
                      )
                    )
                  }.sortedBy { it.name }

                  SelectPlaceUIState.Success(
                    home = home.copy(rooms = sortedRooms),
                    device = device
                  )
                }
              }
            }
          }
        }
      }
    }
  }.stateIn(
    scope = viewModelScope,
    started = SharingStarted.WhileSubscribed(5000),
    initialValue = SelectPlaceUIState.Loading
  )

  fun initialize(id: String) {
    _deviceId.value = id
  }

  fun assignDeviceToPlace (placeId: String) {
    _isBusy.value = true

    if(_deviceId.value == null) return
    val deviceId = _deviceId.value ?: ""

    viewModelScope.launch {
      try {

        val body = AssignDeviceToPlaceDto(placeId = placeId, deviceId = deviceId)

        val res = ApiClient.deviceApiService.assignDeviceToPlace(body)

        if(res.isSuccessful) {
          Log.e("API_TEST", "assigned device to place worked")
          HomeRepository.refetchHome()
          DeviceRepository.refetchDevices()
        }
        else {
          Log.e("API_TEST", "assigned device to place failed: ${res.message()}")
        }
      }
      catch (e: Exception){
        Log.e("API_TEST", "Exception trying to assign to place failed: ${e.message}")
      }
      finally {
        _isBusy.value = false
      }
    }
  }
}