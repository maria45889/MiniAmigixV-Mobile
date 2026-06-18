package com.miniamigixv.miniamigixv_app.data.repository

import com.miniamigixv.miniamigixv_app.data.model.WeatherData
import com.miniamigixv.miniamigixv_app.data.remote.WeatherApiService

class WeatherRepository(private val apiService: WeatherApiService) {

    suspend fun getWeather(city: String): Result<WeatherData> {
        if (city.isBlank()) {
            return Result.failure(IllegalArgumentException("Ingresa una ciudad"))
        }
        return apiService.getWeather(city)
    }
}
