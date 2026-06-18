package com.miniamigixv.miniamigixv_app.data.remote

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.ui.graphics.vector.ImageVector

object WeatherIconMapper {

    fun getIcon(iconCode: String): ImageVector {
        return when (iconCode.take(2)) {
            "01" -> Icons.Filled.LightMode
            "02" -> Icons.Filled.Cloud
            "03" -> Icons.Filled.Cloud
            "04" -> Icons.Filled.Cloud
            "09" -> Icons.Filled.WaterDrop
            "10" -> Icons.Filled.Umbrella
            "11" -> Icons.Filled.Thunderstorm
            "13" -> Icons.Filled.AcUnit
            "50" -> Icons.Filled.Cloud
            else -> Icons.Filled.Cloud
        }
    }
}
