package com.example.learning_android.viewmodels

import android.app.Application
import android.util.Log
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.viewModelScope
import com.example.learning_android.data.remote.client.ApiClient
import com.example.learning_android.data.remote.dto.CreatePlaceRequestDto
import com.example.learning_android.data.remote.dto.CreateRoomRequestDto
import com.example.learning_android.domain.model.DetailedHomeRoom
import com.example.learning_android.repositories.HomeRepository
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class AddPlaceViewModel(
  application: Application,
  private val savedStateHandle: SavedStateHandle
) : AndroidViewModel(application) {

  private val homeId: String = checkNotNull(savedStateHandle["homeId"])

  val rooms: StateFlow<List<DetailedHomeRoom>> = HomeRepository.homes
    .map { homes ->
      homes.find { home -> home.id == homeId }?.rooms ?: emptyList()
    }
    .stateIn(
      scope = viewModelScope,
      started = SharingStarted.WhileSubscribed(5000),
      initialValue = emptyList()
    )

  var userInputRoomName by mutableStateOf("")
  var userInputRoomIconId by mutableStateOf<String?>(null)

  var selectedRoom by mutableStateOf<DetailedHomeRoom?>(null)
  var userInputPlaceName by mutableStateOf("")
  var userInputPlaceIconId by mutableStateOf<String?>(null)

  var busy by mutableStateOf(false)

  fun addRoom (onSuccess: (roomName: String) -> Unit) {
    busy = true
    val name = userInputRoomName
    val iconId = userInputRoomIconId

    viewModelScope.launch {
      try{
        val body = CreateRoomRequestDto(homeId, name, iconId)

        val res = ApiClient.homeApiService.createRoom(body)

        if(res.isSuccessful) {
          Log.e("API_TEST", "created room")
          onSuccess(res.body()?.room?.name ?: "")
          userInputRoomIconId = null
          userInputRoomName = ""
          HomeRepository.refetchHome()
        }
        else {
         Log.e("API_TEST", "failed creating room: ${homeId}")
        }
      }
      catch (e: Exception) {
        Log.e("API_TEST", "threw error creating room: ${e.message}")
      }
      finally {
        busy = false
      }
    }
  }

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