package com.miniamigixv.miniamigixv_app.ui.theme

import android.content.Context
import android.content.SharedPreferences
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class ThemeViewModel(context: Context) : ViewModel() {
    private val prefs: SharedPreferences = context.getSharedPreferences("theme_prefs", Context.MODE_PRIVATE)
    
    private val _isDarkTheme = MutableStateFlow(prefs.getBoolean("is_dark_theme", true))
    val isDarkTheme: StateFlow<Boolean> = _isDarkTheme.asStateFlow()

    fun setTheme(isDark: Boolean) {
        _isDarkTheme.value = isDark
        prefs.edit().putBoolean("is_dark_theme", isDark).apply()
    }

    fun toggleTheme() {
        val newValue = !_isDarkTheme.value
        setTheme(newValue)
    }
}
