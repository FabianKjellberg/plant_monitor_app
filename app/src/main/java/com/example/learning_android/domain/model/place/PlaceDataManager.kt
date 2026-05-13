package com.example.learning_android.domain.model.place

import android.util.Log
import com.example.learning_android.data.remote.client.ApiClient
import com.example.learning_android.data.remote.dto.PostDailyMetricsBodyDto
import com.example.learning_android.data.remote.helpers.averageOrNull
import com.example.learning_android.domain.model.DeviceReading
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.filter
import kotlinx.coroutines.flow.mapLatest
import kotlinx.coroutines.flow.stateIn
import java.time.ZoneId
import kotlin.collections.filter

@OptIn(ExperimentalCoroutinesApi::class)
class PlaceDataManager(
  initialBuckets: List<DailyMetric> = emptyList(),
  private val api: ApiClient,
  private val scope: CoroutineScope,
  private val placeId: String,
){
  private val _hasValues = MutableStateFlow<Boolean>(false)
  val hasValues = _hasValues.asStateFlow()
  private val _buckets = MutableStateFlow(initialBuckets)
  private val _selectedMonth = MutableStateFlow<Int?>(null)
  val selectedMonth = _selectedMonth.asStateFlow()

  private val _filteredBucket: StateFlow<List<DailyMetric>> =
    combine(_buckets, _selectedMonth) { buckets, month ->
      if (month == null) {
        buckets
      } else {
        buckets.filter { it.date.monthValue == month }
      }
    }.stateIn(
      scope = scope,
      started = SharingStarted.WhileSubscribed(5000),
      initialValue = initialBuckets
    )

  val averageTemp: StateFlow<Float?> =
    _filteredBucket.mapLatest { buckets ->
      buckets.map { bucket ->
        bucket.temp.avgTemp
      }.averageOrNull()
    }.stateIn(
      scope = scope,
      started = SharingStarted.WhileSubscribed(5000),
      initialValue = null,
    )

  val minTemp: StateFlow<Float?> =
    _filteredBucket.mapLatest { buckets ->
      buckets.minOfOrNull { bucket ->
        bucket.temp.minTemp
      }
    }.stateIn(
      scope = scope,
      started = SharingStarted.WhileSubscribed(5000),
      initialValue = null,
    )

  val maxTemp: StateFlow<Float?> =
    _filteredBucket.mapLatest { buckets ->
      buckets.maxOfOrNull { bucket ->
        bucket.temp.maxTemp
      }
    }.stateIn(
      scope = scope,
      started = SharingStarted.WhileSubscribed(5000),
      initialValue = null,
    )

  val averageHumidity: StateFlow<Float?> =
    _filteredBucket.mapLatest { buckets ->
      buckets.map { bucket ->
        bucket.humidity.avgHumidity
      }.averageOrNull()
    }.stateIn(
      scope = scope,
      started = SharingStarted.WhileSubscribed(5000),
      initialValue = null,
    )

  val averageDli: StateFlow<Float?> =
    _filteredBucket.mapLatest { buckets ->
      buckets.map { bucket ->
        bucket.light.dli
      }.averageOrNull()
    }.stateIn(
      scope = scope,
      started = SharingStarted.WhileSubscribed(5000),
      initialValue = null
    )

  val averagePeakPpfd: StateFlow<Float?> =
    _filteredBucket.mapLatest { buckets ->
      buckets.map { bucket ->
        bucket.light.peakPpfd
      }.averageOrNull()
    }.stateIn(
      scope = scope,
      started = SharingStarted.WhileSubscribed(5000),
      initialValue = null
    )

    val monthlyCoverage: StateFlow<Map<Int, Int>> = _buckets.mapLatest { bucket ->
      bucket.groupBy { readings ->
        readings.date.monthValue }.mapValues { (_, daysInMonth) ->
          daysInMonth.size
      }
    }.stateIn(
      scope = scope,
      started = SharingStarted.WhileSubscribed(5000),
      initialValue = emptyMap()
    )

  fun selectMonth(month: Int) {
    if(selectedMonth.value == month) {
      _selectedMonth.value = null
    }
    else {
      _selectedMonth.value = month
    }
  }

  suspend fun syncData(readings: List<DeviceReading>) {
    val zone = ZoneId.systemDefault()

    val newBuckets: List<DailyMetric> = readings.groupBy { reading ->
      reading.readAt.atZone(zone).toLocalDate()
    }.map { (date, dailyReadings) ->

      val hourlyAverageTemp = dailyReadings.groupBy {
        it.readAt.atZone(zone).hour }.map { (_, hourReadings) ->
          hourReadings.map { reading -> reading.temperature }.averageOrNull()
      }

      val avgTemp = hourlyAverageTemp.averageOrNull()
      val minTemp = hourlyAverageTemp.filterNotNull().minOrNull()
      val maxTemp = hourlyAverageTemp.filterNotNull().maxOrNull()

      val avgHumidity = dailyReadings.map { reading -> reading.humidity }.averageOrNull()

      val avgLux = dailyReadings.map { reading -> reading.lux }.averageOrNull()

      val luxToPpfdFactor = 0.0185f
      val sensorFactor = 1.5F
      val secondsBetweenReadings = 300f

      val totalMicromoles = dailyReadings.sumOf { reading ->
        val luxValue = reading.lux ?: 0F

        (luxValue * luxToPpfdFactor * secondsBetweenReadings * sensorFactor).toDouble()
      }

      val dli = (totalMicromoles / 1_000_000).toFloat()

      val dailyMaxLux = dailyReadings.mapNotNull { it.lux }.maxOrNull() ?: 0f
      val peakPpfd = dailyMaxLux * sensorFactor* luxToPpfdFactor

      DailyMetric(
        light = DailyMetricLight(
          avgLux ?: 0F,
          dli,
          peakPpfd
        ),
        temp = DailyMetricTemp(
          avgTemp ?: 0F,
          minTemp ?: 0F,
          maxTemp ?: 0F,
        ),
        humidity = DailyMetricHumidity(
          avgHumidity ?: 0F
        ),
        date = date,
        totalReadings = dailyReadings.size
      )

    }

    val filteredNew = newBuckets.filter { it.totalReadings > 200 }

    try {
      val body = PostDailyMetricsBodyDto(
        placeId,
        dailyMetrics = filteredNew
      )

      val res = api.readingsApiService.postDailyMetrics(body)

      if(!res.isSuccessful) {
        Log.e("API_TEST", "failed uploading daily metrics: ${res.message()}")
      }
    }
    catch(e:Exception) {
      Log.e("API_TEST", "failed uploading daily metrics ${e.message}")
    }

    if(newBuckets.isNotEmpty() || _buckets.value.isNotEmpty()) _hasValues.value = true
    _buckets.value = (_buckets.value + filteredNew).distinctBy { it.date }
  }
}

