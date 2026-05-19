package com.example.learning_android.viewmodels

import android.util.Log
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.learning_android.data.mapper.toDomain
import com.example.learning_android.data.remote.client.ApiClient
import com.example.learning_android.domain.model.place.PlaceDataManager
import com.example.learning_android.repositories.HomeRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class PlacePageViewModel(
  savedStateHandle: SavedStateHandle
): ViewModel() {
  private val placeId: String = checkNotNull(savedStateHandle["placeId"])

  private val _busy = MutableStateFlow(false)
  val busy = _busy.asStateFlow()
  private val _dataManager = MutableStateFlow<PlaceDataManager?>(null)
  val dataManager = _dataManager.asStateFlow()

  init {
    fetchData()
  }

  val place = HomeRepository.homes.map { homeList ->
    homeList
      .flatMap { home -> home.rooms }
      .flatMap { room -> room.places }
      .find { it.id == placeId }
  }
    .distinctUntilChanged()
    .stateIn(
      scope = viewModelScope,
      started = SharingStarted.WhileSubscribed(5_000),
      initialValue = null
    )

  fun fetchData() {
    viewModelScope.launch {
      _busy.value = true;

      try{
        val res = ApiClient.readingsApiService.getReadingsForPlace(placeId)

        if(res.isSuccessful) {
          val readings = res.body()?.toDomain()?.readings ?: emptyList()

          val newManager = PlaceDataManager(
            initialBuckets = emptyList(),
            api = ApiClient,
            scope = viewModelScope,
            placeId
          )
          newManager.syncData(readings)

          _dataManager.value = newManager
        }
        else {
          Log.e("API_TEST", "Fetching place data failed: ${res.message()}")
        }
      }
      catch (e: Exception){
        Log.e("API_TEST", "Fetching place data failed: ${e.message}}")
      }
      finally {
        _busy.value = true
      }
    }
  }

  fun changeName(name: String, onSuccess: () -> Unit) {
    viewModelScope.launch {
      val renamed = HomeRepository.changePlaceName(placeId, name)
      if (renamed) onSuccess()
    }
  }

  fun deletePlace(onSuccess: () -> Unit) {
    viewModelScope.launch {
      val removed = HomeRepository.deletePlace(placeId)
      if (removed) onSuccess()
    }
  }
}