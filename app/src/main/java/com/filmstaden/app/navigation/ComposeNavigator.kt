package com.filmstaden.app.navigation

import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow

class ComposeNavigator : AppComposeNavigator {

    private val _commands = MutableSharedFlow<NavigationCommand>(
        extraBufferCapacity = 64
    )
    override val commands = _commands.asSharedFlow()

    override fun navigate(screen: AppScreen) {
        _commands.tryEmit(NavigationCommand.Navigate(screen))
    }

    override fun navigateUp() {
        _commands.tryEmit(NavigationCommand.NavigateUp)
    }

    override fun popUpTo(screen: AppScreen, inclusive: Boolean) {
        _commands.tryEmit(NavigationCommand.PopUpTo(screen, inclusive))
    }

    override fun navigateAndClearBackStack(screen: AppScreen) {
        _commands.tryEmit(NavigationCommand.NavigateAndClear(screen))
    }
}
