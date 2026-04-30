package com.example.learning_android.data.remote.api

import com.example.learning_android.data.remote.dto.GetDevicesResponseDTO
import com.example.learning_android.data.remote.dto.UpdateNameRequestDto
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.PUT

interface DeviceApiService {
    @GET("device/all")
    suspend fun getAllDevices (): GetDevicesResponseDTO

    @PUT("device/name")
    suspend fun changeDeviceName(
        @Body body: UpdateNameRequestDto
    ): Response<Unit>
}