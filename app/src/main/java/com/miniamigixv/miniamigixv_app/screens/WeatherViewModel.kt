package com.miniamigixv.miniamigixv_app.screens

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.miniamigixv.miniamigixv_app.data.model.WeatherData
import com.miniamigixv.miniamigixv_app.data.remote.MockWeatherApiService
import com.miniamigixv.miniamigixv_app.data.repository.WeatherRepository
import kotlinx.coroutines.launch

sealed class WeatherUiState {
    data object Idle : WeatherUiState()
    data object Loading : WeatherUiState()
    data class Success(val data: WeatherData) : WeatherUiState()
    data class Error(val message: String) : WeatherUiState()
}

class WeatherViewModel : ViewModel() {

    private val repository = WeatherRepository(MockWeatherApiService())

    var uiState by mutableStateOf<WeatherUiState>(WeatherUiState.Idle)
        private set

    var searchQuery by mutableStateOf("")
        private set

    fun updateSearchQuery(query: String) {
        searchQuery = query
    }

    fun searchWeather() {
        val city = searchQuery.trim()
        if (city.isBlank()) return

        uiState = WeatherUiState.Loading

        viewModelScope.launch {
            val result = repository.getWeather(city)
            uiState = result.fold(
                onSuccess = { WeatherUiState.Success(it) },
                onFailure = { WeatherUiState.Error(it.message ?: "Error al obtener el clima") }
            )
        }
    }
}
