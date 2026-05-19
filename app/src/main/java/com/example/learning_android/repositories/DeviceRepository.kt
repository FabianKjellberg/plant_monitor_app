package com.example.learning_android.repositories

import android.util.Log
import com.example.learning_android.data.mapper.toDomain
import com.example.learning_android.data.remote.client.ApiClient
import com.example.learning_android.domain.model.Device
import com.example.learning_android.domain.model.DeviceHome
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.flatMap
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.mapLatest
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
        Log.e("API_TEST", "deviceHome body ${res.body()}")

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

  fun getDeviceFromId(id: String): Flow<Device?> {
    return _deviceHomes.map { homeList ->
      homeList.flatMap { home -> home.devices }
        .find { device -> device.id == id}
    }.distinctUntilChanged()
  }

  fun getHomeIdFromDevice(deviceId: String): Flow<String?> {
    return _deviceHomes.map { homeList ->
      homeList.find { home ->
        home.devices.any { device -> device.id == deviceId }
      }?.id
    }.distinctUntilChanged()
  }

  suspend fun forgetDevice(deviceId: String): Boolean {
    try {
      val response = ApiClient.deviceApiService.forgetDevice(deviceId)

      if(response.isSuccessful){
        return true
      }
      else {
        Log.e("API_TEST", "failed to remove device ${response.message()}")
        return false
      }
    }
    catch (e: Exception) {
      Log.e("API_TEST","failed to remove device ${e.message}")
      return false
    }
  }
}
