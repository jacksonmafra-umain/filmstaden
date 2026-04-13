package com.filmstaden.app.navigation

import androidx.navigation3.runtime.NavKey
import kotlinx.serialization.Serializable

sealed interface AppScreen : NavKey

@Serializable
data object Home : AppScreen

@Serializable
data class MovieDetail(val movieId: String) : AppScreen

@Serializable
data class SeatSelection(
    val movieId: String,
    val date: String,
    val time: String
) : AppScreen

@Serializable
data object MyTickets : AppScreen

@Serializable
data object More : AppScreen
