package com.miniamigixv.miniamigixv_app.data.model

data class WeatherData(
    val city: String,
    val temperature: Double,
    val feelsLike: Double,
    val humidity: Int,
    val windSpeed: Double,
    val description: String,
    val iconCode: String
)
