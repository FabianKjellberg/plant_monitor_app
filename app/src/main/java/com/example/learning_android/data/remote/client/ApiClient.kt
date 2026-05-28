package com.example.learning_android.data.remote.client

import android.content.Context
import com.example.learning_android.data.remote.api.AuthApiService
import com.example.learning_android.data.remote.api.DeviceApiService
import com.example.learning_android.data.remote.api.HomeApiService
import com.example.learning_android.data.remote.api.ReadingsApiService
import com.example.learning_android.data.remote.dto.RefreshResponseDto
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import com.franmontiel.persistentcookiejar.PersistentCookieJar
import com.franmontiel.persistentcookiejar.cache.SetCookieCache
import com.franmontiel.persistentcookiejar.persistence.SharedPrefsCookiePersistor
import com.franmontiel.persistentcookiejar.ClearableCookieJar
import kotlinx.coroutines.runBlocking
import okhttp3.OkHttpClient
import kotlin.getValue

object ApiClient {

  private const val BASE_URL = "https://plant-monitor-worker-cf.tekital1000.workers.dev"
  private lateinit var cookieJar: ClearableCookieJar
  private var appContext: Context? = null;

  fun init(context: Context) {
    val ctx = context.applicationContext
    appContext = ctx
    cookieJar = PersistentCookieJar(SetCookieCache(), SharedPrefsCookiePersistor(ctx))
  }

  fun getContext(): Context {
    return appContext ?: throw IllegalStateException("ApiClient not initialized!")
  }

  fun logout() {
    val context = appContext ?: return

    TokenManager.clear(context)
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
        if (response.priorResponse() != null) return@authenticator null

        val bootstrapClient = OkHttpClient.Builder()
          .cookieJar(cookieJar)
          .build()

        val refreshRequest = okhttp3.Request.Builder()
          .url("$BASE_URL/auth/refresh")
          .post(okhttp3.RequestBody.create(null, ByteArray(0)))
          .build()
        val refreshResponse = bootstrapClient.newCall(refreshRequest).execute()

        if (refreshResponse.isSuccessful) {
          val bodyString = refreshResponse.body()?.string()
          val refreshDto = com.google.gson.Gson().fromJson(bodyString, RefreshResponseDto::class.java)
          val newAccessToken = refreshDto?.accessToken

          val context = appContext
          if (newAccessToken != null && context != null) {
            TokenManager.saveAccessToken(context, newAccessToken)

            return@authenticator response.request().newBuilder()
              .header("Authorization", "Bearer $newAccessToken")
              .build()
          }
        }
        appContext?.let { TokenManager.clear(it) }

        runBlocking {
          SessionHandler.sessionExpired()
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

  val homeApiService: HomeApiService by lazy {
    retrofit.create(HomeApiService::class.java)
  }
}