package com.example.learning_android.data.remote.client

import com.example.learning_android.data.remote.api.DeviceApiService
import com.example.learning_android.data.remote.api.ReadingsApiService
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object ApiClient {

    private const val BASE_URL = "https://plant-monitor-worker-cf.tekital1000.workers.dev"

    val readingsApiService: ReadingsApiService by lazy {
        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(ReadingsApiService::class.java)
    }
    val deviceApiService : DeviceApiService by lazy {
        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(DeviceApiService::class.java)
    }
}