package com.example.learning_android.viewmodels

import android.R.attr.name
import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.learning_android.data.mapper.toDomain
import com.example.learning_android.data.remote.client.ApiClient
import com.example.learning_android.data.remote.dto.UpdateNameRequestDto
import com.example.learning_android.domain.model.ChartEntity
import com.example.learning_android.domain.model.DeviceReadings
import com.example.learning_android.domain.model.PotDevice
import com.example.learning_android.domain.model.ReadingType
import com.example.learning_android.repositories.DeviceRepository
import kotlinx.coroutines.launch
import java.time.Instant
import java.time.temporal.ChronoUnit

@RequiresApi(Build.VERSION_CODES.O)
class DevicePageViewModel(
    savedStateHandle: SavedStateHandle
) : ViewModel() {

   
    private val deviceId: String = checkNotNull(savedStateHandle["deviceId"])

    var device by mutableStateOf<PotDevice?>(null)
        private set

    init {
        device = DeviceRepository.getDeviceById(deviceId)
    }

    private var readings by mutableStateOf<DeviceReadings?>(null)
    var fromDate by mutableStateOf<Instant>(Instant.now().minus(1, ChronoUnit.DAYS))
        private set
    var toDate by mutableStateOf<Instant>(Instant.now())
        private set
    var readingType by mutableStateOf(ReadingType.LUX)
        private set

    var chartValue by mutableStateOf<List<ChartEntity>?>(null)

    fun updateReadingType(type: ReadingType) {
        this.readingType = type
        this.updateChartValues()
    }

    fun updateToDate(toDate: Instant) {
        this.toDate = toDate
        loadData();
    }

    fun updateFromDate(fromDate: Instant) {
        this.fromDate = fromDate
        loadData();
    }

    private fun updateChartValues() {
        val currentReadings = readings?.readings ?: return;

        chartValue = currentReadings.map { reading ->
            val value = when (readingType) {
                ReadingType.LUX -> reading.lux ?: 0F
                ReadingType.BATTERY_PERCENTAGE -> reading.batteryMv ?: 0F
                ReadingType.HUMIDITY -> reading.humidity ?: 0F
                ReadingType.PRESSURE -> reading.pressure ?: 0F
                ReadingType.TEMPERATURE -> reading.temperature ?: 0F
            }

            ChartEntity(
                value = value,
                time = reading.readAt
            )
        }
    }


    fun loadData() {
        viewModelScope.launch {
            try {
                val from = fromDate.toString()
                val to = toDate.toString()

                val response = ApiClient.readingsApiService
                    .getDeviceReadingRange(from, to, deviceId);
                Log.e("API_TEST", "success")
                readings = response.toDomain();

                updateChartValues();
            } catch (e: Exception) {
                Log.e("API_TEST", "Error: ${e.message}")
            }
        }
    }

    fun updateDeviceName(name: String) {
        viewModelScope.launch {
            val oldName = device?.deviceName ?: "unknown"
            DeviceRepository.updateDeviceName(deviceId, name)

            device = device?.copy(deviceName = name)

            val body = UpdateNameRequestDto(name = name, deviceId = deviceId)
            val response = ApiClient.deviceApiService.changeDeviceName(body)

            if (!response.isSuccessful) {
                DeviceRepository.updateDeviceName(deviceId, oldName)
                device = device?.copy(deviceName = oldName)
            }
        }
    }
}