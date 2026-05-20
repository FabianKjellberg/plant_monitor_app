package com.example.learning_android.data.remote.api

import com.example.learning_android.data.remote.dto.LoginRequestDto
import com.example.learning_android.data.remote.dto.LoginResponseDto
import com.example.learning_android.data.remote.dto.RefreshResponseDto
import com.example.learning_android.data.remote.dto.RegisterRequestDto
import retrofit2.Call
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST

interface AuthApiService {
    @POST("auth/refresh")
    fun refresh(): Call<RefreshResponseDto>

    @GET("auth/test-auth")
    suspend fun testAuth(): Response<Unit>

    @POST("auth/login")
    suspend fun login(
        @Body body: LoginRequestDto
    ): Response<LoginResponseDto>

    @POST("auth/register")
    suspend fun register(
        @Body body: RegisterRequestDto
    ): Response<Unit>

    @POST("auth/logout")
    suspend fun logout() : Response<Unit>
}