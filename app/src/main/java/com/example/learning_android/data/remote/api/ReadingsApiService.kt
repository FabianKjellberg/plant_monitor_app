package com.example.learning_android.data.remote.api

import retrofit2.http.GET
import retrofit2.http.Path
import com.example.learning_android.data.remote.dto.GetDeviceReadingsResponseDto
import java.time.LocalDate

interface ReadingsApiService {
    @GET("readings/all/{deviceId}")
    suspend fun getDeviceReadings(
        @Path("deviceId") deviceId: String
    ): GetDeviceReadingsResponseDto

    @GET("readings/{from}/{to}/{deviceId}")
    suspend fun getDeviceReadingRange(
        @Path("from") from: String,
        @Path("to") to: String,
        @Path("deviceId") deviceId: String
    ): GetDeviceReadingsResponseDto
}