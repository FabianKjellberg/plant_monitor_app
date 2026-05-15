package com.example.learning_android.viewmodels.modals

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.learning_android.domain.model.DetailedHomeRoom
import com.example.learning_android.repositories.HomeRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class AddPlaceModalViewModel: ViewModel() {
  var busy by mutableStateOf(false)

  private val _homeId = MutableStateFlow<String?>(null)
  private val _passedRoom = MutableStateFlow<DetailedHomeRoom?>(null)

  var isRoomSelectionEnabled by mutableStateOf(true)

  fun initialize(room: DetailedHomeRoom?, homeId: String) {
    _homeId.value = homeId
    _passedRoom.value = room

    if (room != null) {
      selectedRoom = room
      isRoomSelectionEnabled = false
    } else {
      selectedRoom = null
      isRoomSelectionEnabled = true
    }
  }

  val rooms: StateFlow<List<DetailedHomeRoom>> = combine(
    HomeRepository.homes,
    _homeId,
    _passedRoom
  ) { homesList, currentHomeId, passedRoom ->
    if (passedRoom != null) {
      emptyList()
    } else {
      homesList.find { it.id == currentHomeId }?.rooms ?: emptyList()
    }
  }.stateIn(
    scope = viewModelScope,
    started = SharingStarted.WhileSubscribed(5000),
    initialValue = emptyList()
  )

  var selectedRoom by mutableStateOf<DetailedHomeRoom?>(null)
  var userInputPlaceName by mutableStateOf("")
  var userInputPlaceIconId by mutableStateOf<String?>(null)

  fun addPlace (onSuccess: (placeName: String) -> Unit) {
    val roomId = selectedRoom?.id
    val name = userInputPlaceName
    val iconId = userInputPlaceIconId

    if(roomId == null) return

    viewModelScope.launch {
      busy = true;

      val success = HomeRepository.addPlace(roomId, name, iconId)

      if(success) {
        onSuccess(name)
        selectedRoom = null
        userInputPlaceName = ""
        userInputPlaceIconId = null
      }

      busy = false
    }
  }
}