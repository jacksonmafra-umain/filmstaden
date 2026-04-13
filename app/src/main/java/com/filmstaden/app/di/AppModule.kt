package com.filmstaden.app.di

import com.filmstaden.app.data.repository.FilmstadenRepository
import com.filmstaden.app.navigation.AppComposeNavigator
import com.filmstaden.app.navigation.ComposeNavigator
import com.filmstaden.app.ui.screens.home.HomeViewModel
import com.filmstaden.app.ui.screens.moviedetail.MovieDetailViewModel
import com.filmstaden.app.ui.screens.seatselection.SeatSelectionViewModel
import com.filmstaden.app.ui.screens.more.MoreViewModel
import com.filmstaden.app.ui.screens.tickets.MyTicketsViewModel
import org.koin.core.module.dsl.viewModel
import org.koin.dsl.module

val navigationModule = module {
    single<AppComposeNavigator> { ComposeNavigator() }
}

val dataModule = module {
    single { FilmstadenRepository() }
}

val viewModelModule = module {
    viewModel { HomeViewModel(get()) }
    viewModel { params -> MovieDetailViewModel(params.get(), get()) }
    viewModel { params -> SeatSelectionViewModel(params.get(), params.get(), params.get(), get()) }
    viewModel { MyTicketsViewModel(get()) }
    viewModel { MoreViewModel(get()) }
}

val appModules = listOf(navigationModule, dataModule, viewModelModule)
