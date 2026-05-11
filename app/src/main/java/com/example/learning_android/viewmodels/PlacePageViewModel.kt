package com.example.learning_android.viewmodels

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.learning_android.repositories.HomeRepository
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn

class PlacePageViewModel(
  savedStateHandle: SavedStateHandle
): ViewModel() {
  private val homeId: String = checkNotNull(savedStateHandle["homeId"])
  private val placeId: String = checkNotNull(savedStateHandle["placeId"])

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
}