package com.filmstaden.app.navigation

import kotlinx.coroutines.flow.SharedFlow

/**
 * Navigator abstraction for Navigation 3. Emits commands that are consumed
 * by the back stack binding in AppRoot.
 */
interface AppComposeNavigator {
    val commands: SharedFlow<NavigationCommand>

    fun navigate(screen: AppScreen)
    fun navigateUp()
    fun popUpTo(screen: AppScreen, inclusive: Boolean = false)
    fun navigateAndClearBackStack(screen: AppScreen)
}

sealed interface NavigationCommand {
    data class Navigate(val screen: AppScreen) : NavigationCommand
    data object NavigateUp : NavigationCommand
    data class PopUpTo(val screen: AppScreen, val inclusive: Boolean) : NavigationCommand
    data class NavigateAndClear(val screen: AppScreen) : NavigationCommand
}
