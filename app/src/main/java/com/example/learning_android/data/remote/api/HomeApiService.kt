package com.example.learning_android.data.remote.api

import com.example.learning_android.data.remote.dto.CreatePlaceRequestDto
import com.example.learning_android.data.remote.dto.CreatePlaceResponseDto
import com.example.learning_android.data.remote.dto.CreateRoomRequestDto
import com.example.learning_android.data.remote.dto.CreateRoomResponseDto
import com.example.learning_android.data.remote.dto.GetHomeResponseDto
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST

interface HomeApiService {
  @GET("home/all")
  suspend fun getAllHomes (): GetHomeResponseDto

  @POST("home/room/create")
  suspend fun createRoom (
    @Body body: CreateRoomRequestDto
  ): CreateRoomResponseDto

  @POST("home/place/create")
  suspend fun createPlace (
    @Body body: CreatePlaceRequestDto
  ): CreatePlaceResponseDto
}
