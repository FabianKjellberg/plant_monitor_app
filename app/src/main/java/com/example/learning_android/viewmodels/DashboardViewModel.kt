package com.example.learning_android.viewmodels

import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.learning_android.data.mapper.toDomain
import com.example.learning_android.data.remote.client.ApiClient
import com.example.learning_android.domain.model.DashboardNav
import com.example.learning_android.domain.model.PotDevice
import com.example.learning_android.repositories.DeviceRepository
import kotlinx.coroutines.launch

class DashboardViewModel : ViewModel() {
    var devices by mutableStateOf<List<PotDevice>?>(null)
        private set
    var loadingDevices by mutableStateOf(false)
        private set

    var dashboardNav by mutableStateOf(DashboardNav.DEVICES)
        private set

    fun onChangeNav(nav: DashboardNav) {
        dashboardNav = nav;
    }

    fun loadDevices(){
        viewModelScope.launch {
            if(devices == null)
                loadingDevices = true

            try{
                val response = ApiClient.deviceApiService.getAllDevices()
                Log.e("API_TEST",response.toString())
                val resDevices = response.toDomain();

                devices = resDevices
                DeviceRepository.devices = resDevices;
            }
            catch (e: Exception) {
                Log.e("API_TEST", "Error: ${e.message}")
            }
            finally {
                loadingDevices = false
            }
        }
    }
}