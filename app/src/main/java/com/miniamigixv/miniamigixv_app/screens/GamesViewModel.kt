package com.miniamigixv.miniamigixv_app.screens

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import com.miniamigixv.miniamigixv_app.screens.data.model.Game

class GamesViewModel : ViewModel() {
    var games by mutableStateOf(listOf<Game>())
        private set

    init {
        loadGames()
    }

    private fun loadGames() {
        games = listOf(
            Game(
                id = "1",
                title = "Adivinanza",
                description = "Resuelve los acertijos más difíciles.",
                tag = "LÓGICA",
                icon = Icons.Filled.Extension
            ),
            Game(
                id = "2",
                title = "Quiz Master",
                description = "¿Qué tanto sabes del mundo?",
                tag = "CULTURA",
                icon = Icons.Filled.Help
            ),
            Game(
                id = "3",
                title = "Memoria Neón",
                description = "Encuentra los pares de cartas idénticas.",
                tag = "AGILIDAD",
                icon = Icons.Filled.GpsFixed
            ),
            Game(
                id = "4",
                title = "Mente Rápida",
                description = "Cálculos mentales veloces.",
                tag = "NÚMEROS",
                icon = Icons.Filled.Pin
            ),
            Game(
                id = "5",
                title = "Reflejos",
                description = "Haz clic lo más rápido posible.",
                tag = "VELOCIDAD",
                icon = Icons.Filled.FlashOn
            ),
            Game(
                id = "6",
                title = "Fidget",
                description = "Relájate girando el spinner.",
                tag = "RELAX",
                icon = Icons.Filled.Refresh
            ),
            Game(
                id = "7",
                title = "Snake Neo",
                description = "El clásico juego de la serpiente.",
                tag = "RITMO",
                icon = Icons.Filled.SwipeRight
            ),
            Game(
                id = "8",
                title = "Respiración Consciente",
                description = "Sigue el ritmo del círculo para relajarte.",
                tag = "MINDFULNESS",
                icon = Icons.Filled.Spa
            ),
            Game(
                id = "9",
                title = "3 en Raya",
                description = "El clásico juego de estrategia.",
                tag = "ESTRATEGIA",
                icon = Icons.Filled.GridOn
            ),
            Game(
                id = "10",
                title = "Ajedrez",
                description = "El juego de estrategia por excelencia.",
                tag = "ESTRATEGIA",
                icon = Icons.Filled.Casino
            )
        )
    }
}
