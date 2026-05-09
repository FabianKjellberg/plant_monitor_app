package com.example.learning_android.viewmodels

import android.app.Application
import androidx.compose.material3.SnackbarDuration
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel

class AddPlaceViewModel(
  application: Application,
  private val savedStateHandle: SavedStateHandle
) : AndroidViewModel(application) {

  private val homeId: String = checkNotNull(savedStateHandle["homeId"])

  var userInputRoomName by mutableStateOf("")
  var userInputRoomIconId by mutableStateOf<String?>(null)

  var userInputPlaceRoomId by mutableStateOf<String?>(null)
  var userInputPlaceName by mutableStateOf("")
  var userInputPlaceIconId by mutableStateOf<String?>(null)
}