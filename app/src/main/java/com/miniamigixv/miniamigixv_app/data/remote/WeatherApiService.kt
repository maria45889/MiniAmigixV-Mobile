package com.miniamigixv.miniamigixv_app.data.remote

import com.miniamigixv.miniamigixv_app.data.model.WeatherData

/**
 * API service interface for weather data.
 *
 * TODO: Replace mock implementation with a real API call.
 * Example with Retrofit + OpenWeather:
 *
 * ```
 * interface WeatherApiService {
 *     @GET("data/2.5/weather")
 *     suspend fun getWeather(
 *         @Query("q") city: String,
 *         @Query("appid") apiKey: String,
 *         @Query("units") units: String = "metric",
 *         @Query("lang") lang: String = "es"
 *     ): WeatherResponse
 * }
 * ```
 */
interface WeatherApiService {
    suspend fun getWeather(city: String): Result<WeatherData>
}
