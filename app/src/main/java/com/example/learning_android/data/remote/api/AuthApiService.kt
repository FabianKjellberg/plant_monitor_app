package com.example.learning_android.data.remote.api

import com.example.learning_android.data.remote.dto.RefreshResponseDto
import retrofit2.Call
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.POST

interface AuthApiService {
    @POST("auth/refresh")
    fun refresh(): Call<RefreshResponseDto>

    @GET("auth/test-auth")
    fun testAuth(): Response<Unit>
}