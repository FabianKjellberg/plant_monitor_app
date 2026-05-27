package com.example.learning_android.data.remote.api

import com.example.learning_android.data.remote.dto.ChangePlaceIconRequestDto
import com.example.learning_android.data.remote.dto.ChangeRoomIconRequestDto
import com.example.learning_android.data.remote.dto.CreatePlaceRequestDto
import com.example.learning_android.data.remote.dto.CreatePlaceResponseDto
import com.example.learning_android.data.remote.dto.CreateRoomRequestDto
import com.example.learning_android.data.remote.dto.CreateRoomResponseDto
import com.example.learning_android.data.remote.dto.DeleteRoomRequestDto
import com.example.learning_android.data.remote.dto.GetHomeResponseDto
import com.example.learning_android.data.remote.dto.UpdatePlaceNameRequestDto
import com.example.learning_android.data.remote.dto.UpdateRoomNameRequestDto
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Path

interface HomeApiService {
  @GET("home/all")
  suspend fun getAllHomes (): GetHomeResponseDto

  @POST("home/room/create")
  suspend fun createRoom (
    @Body body: CreateRoomRequestDto
  ): Response<CreateRoomResponseDto>

  @POST("home/place/create")
  suspend fun createPlace (
    @Body body: CreatePlaceRequestDto
  ): Response<CreatePlaceResponseDto>

  @PUT("home/room/rename")
  suspend fun updateRoomName (
    @Body body: UpdateRoomNameRequestDto
  ): Response<Unit>

  @DELETE("home/room/{roomId}")
  suspend fun deleteRoom (
    @Path("roomId") roomId: String
  ): Response<Unit>

  @PUT("home/place/rename")
  suspend fun updatePlaceName(
    @Body body: UpdatePlaceNameRequestDto
  ): Response<Unit>

  @DELETE("home/place/{placeId}")
  suspend fun deletePlace(
    @Path("placeId") placeId: String
  ): Response<Unit>

  @PUT("home/room/icon")
  suspend fun changeRoomIcon(
    @Body body: ChangeRoomIconRequestDto
  ): Response<Unit>

  @PUT("home/place/icon")
  suspend fun changePlaceIcon(
    @Body body: ChangePlaceIconRequestDto
  ): Response<Unit>
}
