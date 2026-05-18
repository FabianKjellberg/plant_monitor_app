package com.example.learning_android.repositories

import android.util.Log
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.viewModelScope
import com.example.learning_android.data.mapper.toDomain
import com.example.learning_android.data.remote.client.ApiClient
import com.example.learning_android.data.remote.dto.CreatePlaceRequestDto
import com.example.learning_android.data.remote.dto.DeleteRoomRequestDto
import com.example.learning_android.data.remote.dto.UpdateRoomNameRequestDto
import com.example.learning_android.domain.model.DetailedHome
import com.example.learning_android.domain.model.DetailedHomePlace
import com.example.learning_android.domain.model.DetailedHomeRoom
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.isActive
import kotlinx.coroutines.launch

object HomeRepository {
  private val _homes = MutableStateFlow<List<DetailedHome>>(emptyList())
  val homes: StateFlow<List<DetailedHome>> = _homes.asStateFlow()
  private val _isRefetching = MutableStateFlow(false)
  val isRefetching: StateFlow<Boolean> = _isRefetching.asStateFlow()

  private val repositoryScope = CoroutineScope(SupervisorJob() + Dispatchers.IO)
  private var refreshJob: Job? = null

  fun startAutoRefetch() {
    if (refreshJob?.isActive == true) return

    refreshJob = repositoryScope.launch {
      while (isActive) {
        refetchHome()
        delay(60_000)
      }
    }
  }

  fun stopAutoRefetch() {
    refreshJob?.cancel()
    _homes.value = emptyList()
  }

  suspend fun refetchHome() {
    _isRefetching.value = true
    try {
      val res = ApiClient.homeApiService.getAllHomes()
      _homes.value = res.homes.map { it.toDomain() }
    }
    catch (e: Exception){
      Log.e("API_TEST", "unable to fetch homes ${e.message}")
    }
    finally {
      _isRefetching.value = false
    }
  }

  fun getHomeFromId(homeId: String): Flow<DetailedHome?> {
    return _homes.map { homeList ->
      homeList.find { home -> home.id == homeId }
    }
  }

  fun getRoomFromId(roomId: String): Flow<DetailedHomeRoom?> {
    return _homes.map { homeList ->
      homeList.flatMap { home ->
        home.rooms
      }.find {
        room -> room.id == roomId
      }
    }
  }

  fun getPlaceFromId(placeId: String): Flow<DetailedHomePlace?> {
    return _homes.map { homeList ->
      homeList.flatMap { home ->
        home.rooms
      }.flatMap { room ->
          room.places
        }.find{ place ->
          place.id == placeId
        }
    }
  }

  suspend fun deleteRoom(roomId: String): Boolean {
    try {
      val body = DeleteRoomRequestDto(roomId = roomId)
      val response = ApiClient.homeApiService.deleteRoom(body)

      if(!response.isSuccessful){
        Log.e("API_TEST", "Unalbe to delete room ${response.message()}")
        return false
      }
      else {
        refetchHome()
        return true
      }
    }
    catch (e: Exception) {
      Log.e("API_TEST", "Unalbe to delete room ${e.message}")
      return false
    }
  }

  suspend fun renameRoom(roomId: String, name: String): Boolean {
      try {
        val body = UpdateRoomNameRequestDto(name = name, roomId = roomId)
        val response = ApiClient.homeApiService.updateRoomName(body);

        if (response.isSuccessful) {
          refetchHome()
          return true;
        }
        Log.e("API_TEST", "Failed renaming room ${response.message()}")
        return false
      }
      catch (e: Exception) {
        Log.e("API_TEST", "failed updating name, reverting locally: ${e.message}")
        return false
      }
  }

  suspend fun addPlace(roomId: String?, name: String?, iconId: String?): Boolean {
    if(roomId == null || name == null) return false

    try{
      val body = CreatePlaceRequestDto(roomId, name, iconId)

      val res = ApiClient.homeApiService.createPlace(body)

      if(res.isSuccessful) {
        Log.e("API_TEST", "created room")
        refetchHome()
        return true
      }
      else {
        Log.e("API_TEST", "failed creating room")
        return false
      }
    }
    catch (e: Exception) {
      Log.e("API_TEST", "threw error creating room: ${e.message}")
      return false
    }
  }
}