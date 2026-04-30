package com.example.learning_android.data.remote.client

import android.content.Context
import com.example.learning_android.data.remote.api.AuthApiService
import com.example.learning_android.data.remote.api.DeviceApiService
import com.example.learning_android.data.remote.api.ReadingsApiService
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import com.franmontiel.persistentcookiejar.PersistentCookieJar
import com.franmontiel.persistentcookiejar.cache.SetCookieCache
import com.franmontiel.persistentcookiejar.persistence.SharedPrefsCookiePersistor
import com.franmontiel.persistentcookiejar.ClearableCookieJar
import okhttp3.OkHttpClient

object ApiClient {

    private const val BASE_URL = "https://plant-monitor-worker-cf.tekital1000.workers.dev"
    private lateinit var cookieJar: ClearableCookieJar
    private var appContext: Context? = null;

    fun init(context: Context) {
        val ctx = context.applicationContext
        appContext = ctx
        cookieJar = PersistentCookieJar(SetCookieCache(), SharedPrefsCookiePersistor(ctx))
    }

    private val okHttpClient: OkHttpClient by lazy {
        OkHttpClient.Builder()
            .cookieJar(cookieJar)
            .addInterceptor { chain ->
                val requestBuilder = chain.request().newBuilder()

                appContext?.let { ctx ->
                    TokenManager.getAccessToken(ctx)?.let { token ->
                        requestBuilder.addHeader("Authorization", "Bearer $token")
                    }
                }

                chain.proceed(requestBuilder.build())
            }

            .authenticator { _, response ->
                println("Detected 401 - Attempting Token Refresh")

                val refreshTokenResponse = authApiService.refresh().execute();

                if (refreshTokenResponse.isSuccessful) {
                    val newAccessToken = refreshTokenResponse.body()?.accessToken

                    if (newAccessToken != null && appContext != null) {
                        TokenManager.saveAccessToken(appContext!!, newAccessToken)

                        return@authenticator response.request().newBuilder()
                            .header("Authorization", "Bearer $newAccessToken")
                            .build()
                    }
                }
                null
            }
            .build()
    }

    private val retrofit: Retrofit by lazy {
        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .client(okHttpClient)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

    val readingsApiService: ReadingsApiService by lazy {
        retrofit.create(ReadingsApiService::class.java)
    }

    val deviceApiService: DeviceApiService by lazy {
        retrofit.create(DeviceApiService::class.java)
    }

    val authApiService: AuthApiService by lazy {
        retrofit.create(AuthApiService::class.java)
    }
}