package com.example.learning_android.repositories

import android.util.Log
import com.example.learning_android.data.mapper.toDomain
import com.example.learning_android.data.remote.client.ApiClient
import com.example.learning_android.domain.model.DeviceHome
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.isActive
import kotlinx.coroutines.launch


object DeviceRepository {
  private val _deviceHomes = MutableStateFlow<List<DeviceHome>>(emptyList())
  val deviceHomes: StateFlow<List<DeviceHome>> = _deviceHomes.asStateFlow()
  private val _isRefetching = MutableStateFlow(false)
  val isRefetching: StateFlow<Boolean> = _isRefetching.asStateFlow()

  private val repositoryScope = CoroutineScope(SupervisorJob() + Dispatchers.IO)
  private var refetchJob: Job? = null

  fun startAutoRefetch() {
    if(refetchJob?.isActive == true) return

    refetchJob = repositoryScope.launch {
      while (isActive) {
        refetchDevices()
        delay(60_000)
      }
    }
  }

  fun stopAutoRefetch() {
    refetchJob?.cancel()
    _deviceHomes.value = emptyList()
  }

  suspend fun refetchDevices() {
    _isRefetching.value = true
    try {
      val res = ApiClient.deviceApiService.getAllDevices()



      if(res.isSuccessful) {
        _deviceHomes.value = res.body()?.toDomain() ?: emptyList()
      }
      else {
        Log.e("API_TEST", "unable to fetch device homes ${res.message()}")
      }
    }
    catch (e: Exception) {
      Log.e("API_TEST", "failed fetching devices ${e.message}")
    }
  }

  fun updateDeviceName(id: String, name: String) {
    val currentList = _deviceHomes.value
    val updatedList = currentList.map { home ->
      home.copy(devices = home.devices.map { device ->
        if(device.id == id) {
          device.copy(name = name)
        }
        else {
          device
        }
      })
    }

    _deviceHomes.value = updatedList;
  }
}
