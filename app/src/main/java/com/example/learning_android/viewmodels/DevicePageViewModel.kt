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
import com.example.learning_android.domain.model.ReadingType
import com.example.learning_android.repositories.DeviceRepository
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import java.time.Instant
import java.time.temporal.ChronoUnit

@RequiresApi(Build.VERSION_CODES.O)
class DevicePageViewModel(
    savedStateHandle: SavedStateHandle
) : ViewModel() {

    private val deviceId: String = checkNotNull(savedStateHandle["deviceId"])

    val device = DeviceRepository.deviceHomes.map { homes ->
        homes.flatMap { home -> home.devices } . find { device -> device.id == deviceId }
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = null
    )

    private var readings by mutableStateOf<DeviceReadings?>(null)
    var fromDate by mutableStateOf<Instant>(Instant.now().minus(1, ChronoUnit.DAYS))
        private set
    var toDate by mutableStateOf<Instant>(Instant.now())
        private set
    var readingType by mutableStateOf(ReadingType.LUX)
        private set

    var chartValue by mutableStateOf<List<ChartEntity>?>(null)

    init {
        loadData()
    }

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
                Log.e("API_TEST", "got sensor data")
                readings = response.toDomain();

                updateChartValues();
            } catch (e: Exception) {
                Log.e("API_TEST", "Error: ${e.message}")
            }
        }
    }

    fun updateDeviceName(name: String) {
        val oldDevice = device.value
        val oldName = oldDevice?.name ?: return

        viewModelScope.launch {
            DeviceRepository.updateDeviceName(deviceId, name)

            try {
                val body = UpdateNameRequestDto(name = name, deviceId = deviceId)
                val response = ApiClient.deviceApiService.changeDeviceName(body)

                if (!response.isSuccessful) {
                    Log.e("API_TEST", "failed updating name, reverting locally: ${response.message()}")
                    DeviceRepository.updateDeviceName(deviceId, oldName)
                }
            }
            catch (e: Exception) {
                Log.e("API_TEST", "failed updating name, reverting locally: ${e.message}")
                DeviceRepository.updateDeviceName(deviceId, oldName)
            }
        }
    }
}