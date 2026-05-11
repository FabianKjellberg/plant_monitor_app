package com.example.learning_android.data.remote.api

import com.example.learning_android.data.remote.dto.AddDeviceToHomeDto
import com.example.learning_android.data.remote.dto.AssignDeviceToPlaceDto
import com.example.learning_android.data.remote.dto.CreateUserDeviceDto
import com.example.learning_android.data.remote.dto.GetDevicesResponseDto
import com.example.learning_android.data.remote.dto.UpdateNameRequestDto
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.PUT

interface DeviceApiService {
    @GET("device/all")
    suspend fun getAllDevices (): Response<GetDevicesResponseDto>

    @PUT("device/name")
    suspend fun changeDeviceName(
        @Body body: UpdateNameRequestDto
    ): Response<Unit>

    @PUT("device/home")
    suspend fun addDeviceToHome(
        @Body body: AddDeviceToHomeDto
    ): Response<Unit>

    @PUT("device/place")
    suspend fun assignDeviceToPlace(
        @Body body: AssignDeviceToPlaceDto
    ): Response<Unit>
}