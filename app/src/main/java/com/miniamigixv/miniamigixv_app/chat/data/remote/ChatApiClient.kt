package com.miniamigixv.miniamigixv_app.chat.data.remote

import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

object ChatApiClient {

    private val loggingInterceptor = HttpLoggingInterceptor().apply {
        level = HttpLoggingInterceptor.Level.BODY
    }

    private val okHttpClient = OkHttpClient.Builder()
        .connectTimeout(ChatApiConfig.TIMEOUT_SECONDS, TimeUnit.SECONDS)
        .readTimeout(ChatApiConfig.TIMEOUT_SECONDS, TimeUnit.SECONDS)
        .writeTimeout(ChatApiConfig.TIMEOUT_SECONDS, TimeUnit.SECONDS)
        .addInterceptor(loggingInterceptor)
        .build()

    private val retrofit = Retrofit.Builder()
        .baseUrl(ChatApiConfig.BASE_URL)
        .client(okHttpClient)
        .addConverterFactory(GsonConverterFactory.create())
        .build()

    val apiService: ChatApiService = retrofit.create(ChatApiService::class.java)
}
