package com.filmstaden.app.navigation

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.ConfirmationNumber
import androidx.compose.material.icons.outlined.Home
import androidx.compose.material.icons.outlined.Menu
import androidx.compose.ui.graphics.vector.ImageVector

enum class BottomTab(
    val label: String,
    val icon: ImageVector,
    val root: AppScreen
) {
    HOME("Home", Icons.Outlined.Home, com.filmstaden.app.navigation.Home),
    TICKETS("Tickets", Icons.Outlined.ConfirmationNumber, MyTickets),
    MORE("More", Icons.Outlined.Menu, More)
}

fun AppScreen.toBottomTabOrNull(): BottomTab? = when (this) {
    com.filmstaden.app.navigation.Home -> BottomTab.HOME
    MyTickets -> BottomTab.TICKETS
    More -> BottomTab.MORE
    else -> null
}
