package com.miniamigixv.miniamigixv_app.ui.theme

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class ThemeViewModel : ViewModel() {
    private val _isDarkTheme = MutableStateFlow(true)
    val isDarkTheme: StateFlow<Boolean> = _isDarkTheme.asStateFlow()

    fun setTheme(isDark: Boolean) {
        _isDarkTheme.value = isDark
    }

    fun toggleTheme() {
        _isDarkTheme.value = !_isDarkTheme.value
    }
}
