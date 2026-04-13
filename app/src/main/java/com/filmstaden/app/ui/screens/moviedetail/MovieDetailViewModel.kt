package com.filmstaden.app.ui.screens.moviedetail

import androidx.lifecycle.ViewModel
import com.filmstaden.app.data.models.Movie
import com.filmstaden.app.data.repository.FilmstadenRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

data class MovieDetailState(
    val movie: Movie,
    val dates: List<Pair<String, Int>>,
    val times: List<String>,
    val selectedDateIndex: Int = 2,
    val selectedTimeIndex: Int = 2
) {
    val selectedDate: Pair<String, Int> get() = dates[selectedDateIndex]
    val selectedTime: String get() = times[selectedTimeIndex]
}

class MovieDetailViewModel(
    movieId: String,
    private val repository: FilmstadenRepository
) : ViewModel() {

    private val _state = MutableStateFlow(
        MovieDetailState(
            movie = repository.getMovie(movieId),
            dates = repository.getAvailableDates(),
            times = repository.getAvailableTimes()
        )
    )
    val state: StateFlow<MovieDetailState> = _state.asStateFlow()

    fun selectDate(index: Int) = _state.update { it.copy(selectedDateIndex = index) }
    fun selectTime(index: Int) = _state.update { it.copy(selectedTimeIndex = index) }
}
