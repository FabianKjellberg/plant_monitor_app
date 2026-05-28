package com.example.learning_android.data.remote.client

import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow

object SessionHandler {
  private val _events = MutableSharedFlow<AuthEvent>()
  val events = _events.asSharedFlow()

  suspend fun sessionExpired() {
    _events.emit(AuthEvent.SessionExpired)
  }
}

sealed class AuthEvent {
  data object SessionExpired: AuthEvent()
}