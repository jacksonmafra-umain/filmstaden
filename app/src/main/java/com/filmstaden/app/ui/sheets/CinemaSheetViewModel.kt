package com.filmstaden.app.ui.sheets

import androidx.lifecycle.ViewModel
import com.filmstaden.app.data.models.Cinema
import com.filmstaden.app.data.repository.FilmstadenRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

class CinemaSheetViewModel(
    private val repository: FilmstadenRepository
) : ViewModel() {

    val selectedCinema: StateFlow<Cinema> = repository.selectedCinema

    private val _isOpen = MutableStateFlow(false)
    val isOpen: StateFlow<Boolean> = _isOpen.asStateFlow()

    fun open() {
        _isOpen.value = true
    }

    fun close() {
        _isOpen.value = false
    }

    fun select(cinema: Cinema) {
        repository.setSelectedCinema(cinema)
        _isOpen.value = false
    }

    fun cinemas(): List<Cinema> = repository.getCinemas()
}
