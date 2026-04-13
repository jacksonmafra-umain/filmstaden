package com.filmstaden.app.ui

import androidx.compose.animation.ExperimentalSharedTransitionApi
import androidx.compose.animation.SharedTransitionLayout
import androidx.compose.animation.SharedTransitionScope
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.animation.slideOutVertically
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation3.runtime.NavKey
import androidx.navigation3.runtime.entryProvider
import androidx.navigation3.runtime.rememberNavBackStack
import androidx.navigation3.ui.NavDisplay
import com.filmstaden.app.navigation.AppComposeNavigator
import com.filmstaden.app.navigation.AppScreen
import com.filmstaden.app.navigation.BottomTab
import com.filmstaden.app.navigation.Home
import com.filmstaden.app.navigation.MovieDetail
import com.filmstaden.app.navigation.More
import com.filmstaden.app.navigation.MyTickets
import com.filmstaden.app.navigation.NavigationCommand
import com.filmstaden.app.navigation.SeatSelection
import com.filmstaden.app.navigation.toBottomTabOrNull
import com.filmstaden.app.ui.components.AnimatedTabBar
import com.filmstaden.app.ui.sheets.CinemaSelectionSheet
import com.filmstaden.app.ui.sheets.CinemaSheetViewModel
import org.koin.compose.koinInject

@OptIn(ExperimentalSharedTransitionApi::class)
@Composable
fun AppRoot(
    navigator: AppComposeNavigator = koinInject(),
    cinemaSheetVm: CinemaSheetViewModel = koinInject()
) {
    val backStack = rememberNavBackStack(Home)
    val sheetOpen by cinemaSheetVm.isOpen.collectAsStateWithLifecycle()
    var splashDone by remember { mutableStateOf(false) }

    LaunchedEffect(navigator) {
        navigator.commands.collect { command ->
            when (command) {
                is NavigationCommand.Navigate -> {
                    if (backStack.lastOrNull() != command.screen) {
                        backStack.add(command.screen)
                    }
                }
                NavigationCommand.NavigateUp -> {
                    if (backStack.size > 1) backStack.removeAt(backStack.lastIndex)
                }
                is NavigationCommand.PopUpTo -> {
                    val index = backStack.indexOfLast { it == command.screen }
                    if (index >= 0) {
                        val removeFrom = if (command.inclusive) index else index + 1
                        while (backStack.size > removeFrom) backStack.removeAt(backStack.lastIndex)
                    }
                }
                is NavigationCommand.NavigateAndClear -> {
                    backStack.clear()
                    backStack.add(command.screen)
                }
            }
        }
    }

    val top = backStack.lastOrNull() as? AppScreen
    val selectedTab = top?.toBottomTabOrNull() ?: BottomTab.HOME
    val showTabBar = top is com.filmstaden.app.navigation.Home ||
            top is MyTickets ||
            top is More

    SharedTransitionLayout {
      CompositionLocalProvider(LocalSharedTransitionScope provides this) {
      Box(modifier = Modifier.fillMaxSize()) {
        Column(modifier = Modifier.fillMaxSize()) {
            Box(modifier = Modifier.weight(1f)) {
                NavDisplay(
                    backStack = backStack,
                    onBack = {
                        if (backStack.size > 1) backStack.removeAt(backStack.lastIndex)
                    },
                    transitionSpec = {
                        slideInHorizontally(tween(320)) { it } + fadeIn(tween(280)) togetherWith
                            slideOutHorizontally(tween(320)) { -it / 3 } + fadeOut(tween(280))
                    },
                    popTransitionSpec = {
                        slideInHorizontally(tween(320)) { -it / 3 } + fadeIn(tween(280)) togetherWith
                            slideOutHorizontally(tween(320)) { it } + fadeOut(tween(280))
                    },
                    entryProvider = entryProvider {
                        entry<Home> {
                            com.filmstaden.app.ui.screens.home.HomeScreen()
                        }
                        entry<MovieDetail> { key ->
                            com.filmstaden.app.ui.screens.moviedetail.MovieDetailScreen(movieId = key.movieId)
                        }
                        entry<SeatSelection> { key ->
                            com.filmstaden.app.ui.screens.seatselection.SeatSelectionScreen(
                                movieId = key.movieId,
                                date = key.date,
                                time = key.time
                            )
                        }
                        entry<MyTickets> {
                            com.filmstaden.app.ui.screens.tickets.MyTicketsScreen()
                        }
                        entry<More> {
                            com.filmstaden.app.ui.screens.more.MoreScreen()
                        }
                    }
                )
            }
            if (showTabBar) {
                AnimatedTabBar(
                    selected = selectedTab,
                    onSelect = { tab ->
                        val root = tab.root
                        val hasRoot = backStack.any { it == root }
                        when {
                            selectedTab == tab -> navigator.popUpTo(root, inclusive = false)
                            hasRoot -> navigator.popUpTo(root, inclusive = false)
                            else -> navigator.navigate(root)
                        }
                    }
                )
            }
        }

        CinemaSelectionSheet(
            visible = sheetOpen,
            cinemas = cinemaSheetVm.cinemas(),
            onSelect = cinemaSheetVm::select,
            onDismiss = cinemaSheetVm::close
        )

        if (!splashDone) {
            FilmstadenSplash(onFinished = { splashDone = true })
        }
      }
      }
    }
}

@Composable
private fun PlaceholderScreen(label: String) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = label,
            color = MaterialTheme.colorScheme.primary,
            style = MaterialTheme.typography.headlineMedium
        )
    }
}
