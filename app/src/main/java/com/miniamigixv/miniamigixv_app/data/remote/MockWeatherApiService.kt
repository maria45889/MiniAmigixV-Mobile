package com.miniamigixv.miniamigixv_app.data.remote

import com.miniamigixv.miniamigixv_app.data.model.WeatherData
import kotlinx.coroutines.delay

class MockWeatherApiService : WeatherApiService {

    override suspend fun getWeather(city: String): Result<WeatherData> {
        delay(1200)

        return when (city.lowercase()) {
            "londres" -> Result.success(
                WeatherData(
                    city = "Londres",
                    temperature = 14.5,
                    feelsLike = 12.8,
                    humidity = 72,
                    windSpeed = 4.1,
                    description = "Cielo parcialmente nublado",
                    iconCode = "02d"
                )
            )
            "madrid" -> Result.success(
                WeatherData(
                    city = "Madrid",
                    temperature = 28.0,
                    feelsLike = 29.2,
                    humidity = 35,
                    windSpeed = 2.3,
                    description = "Cielo despejado",
                    iconCode = "01d"
                )
            )
            "tokio" -> Result.success(
                WeatherData(
                    city = "Tokio",
                    temperature = 22.3,
                    feelsLike = 21.0,
                    humidity = 65,
                    windSpeed = 3.8,
                    description = "Lluvia ligera",
                    iconCode = "10d"
                )
            )
            "méxico", "mexico" -> Result.success(
                WeatherData(
                    city = "Ciudad de México",
                    temperature = 23.5,
                    feelsLike = 22.0,
                    humidity = 45,
                    windSpeed = 1.5,
                    description = "Soleado",
                    iconCode = "01d"
                )
            )
            "buenos aires" -> Result.success(
                WeatherData(
                    city = "Buenos Aires",
                    temperature = 18.2,
                    feelsLike = 16.9,
                    humidity = 78,
                    windSpeed = 5.2,
                    description = "Nublado",
                    iconCode = "04d"
                )
            )
            "new york" -> Result.success(
                WeatherData(
                    city = "New York",
                    temperature = 10.0,
                    feelsLike = 7.5,
                    humidity = 60,
                    windSpeed = 6.7,
                    description = "Ventoso",
                    iconCode = "03d"
                )
            )
            else -> Result.failure(Exception("Ciudad no encontrada. Prueba con: Londres, Madrid, Tokio, México, Buenos Aires o New York"))
        }
    }
}
