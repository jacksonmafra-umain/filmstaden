package com.filmstaden.app.ui.screens.home

import androidx.lifecycle.ViewModel
import com.filmstaden.app.data.models.Cinema
import com.filmstaden.app.data.models.Movie
import com.filmstaden.app.data.models.MovieCategory
import com.filmstaden.app.data.repository.FilmstadenRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

data class HomeState(
    val cinema: Cinema,
    val nowPlaying: List<Movie>,
    val comingSoon: List<Movie>,
    val topMovies: List<Movie>,
    val userName: String,
    val isCinemaSheetOpen: Boolean = false
)

class HomeViewModel(
    private val repository: FilmstadenRepository
) : ViewModel() {

    private val _state = MutableStateFlow(
        HomeState(
            cinema = repository.getSelectedCinema(),
            nowPlaying = repository.getMovies(MovieCategory.NOW_PLAYING),
            comingSoon = repository.getMovies(MovieCategory.COMING_SOON),
            topMovies = repository.getMovies(MovieCategory.TOP_MOVIES),
            userName = repository.getUserProfile().name.substringBefore(' ')
        )
    )
    val state: StateFlow<HomeState> = _state.asStateFlow()

    fun openCinemaSheet() = _state.update { it.copy(isCinemaSheetOpen = true) }
    fun closeCinemaSheet() = _state.update { it.copy(isCinemaSheetOpen = false) }

    fun selectCinema(cinema: Cinema) {
        _state.update { it.copy(cinema = cinema, isCinemaSheetOpen = false) }
    }

    fun getCinemas(): List<Cinema> = repository.getCinemas()
}
