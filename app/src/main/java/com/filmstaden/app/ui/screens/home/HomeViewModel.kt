package com.filmstaden.app.ui.screens.home

import androidx.lifecycle.ViewModel
import com.filmstaden.app.data.models.Movie
import com.filmstaden.app.data.models.MovieCategory
import com.filmstaden.app.data.repository.FilmstadenRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

data class HomeState(
    val nowPlaying: List<Movie>,
    val comingSoon: List<Movie>,
    val topMovies: List<Movie>,
    val userName: String
)

class HomeViewModel(
    repository: FilmstadenRepository
) : ViewModel() {

    private val _state = MutableStateFlow(
        HomeState(
            nowPlaying = repository.getMovies(MovieCategory.NOW_PLAYING),
            comingSoon = repository.getMovies(MovieCategory.COMING_SOON),
            topMovies = repository.getMovies(MovieCategory.TOP_MOVIES),
            userName = repository.getUserProfile().name.substringBefore(' ')
        )
    )
    val state: StateFlow<HomeState> = _state.asStateFlow()
}
