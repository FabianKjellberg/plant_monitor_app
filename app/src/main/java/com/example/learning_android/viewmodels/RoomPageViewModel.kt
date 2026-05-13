package com.example.learning_android.viewmodels

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.learning_android.repositories.HomeRepository
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn

class RoomPageViewModel(
  savedStateHandle: SavedStateHandle
): ViewModel() {
  private val roomId: String = checkNotNull(savedStateHandle["roomId"])

  val room = HomeRepository.homes.map { homeList ->
    homeList.flatMap { home -> home.rooms }.find { room -> room.id == roomId}
  }
  .distinctUntilChanged()
  .stateIn(
    scope = viewModelScope,
    started = SharingStarted.WhileSubscribed(5000),
    initialValue = null
  )
}