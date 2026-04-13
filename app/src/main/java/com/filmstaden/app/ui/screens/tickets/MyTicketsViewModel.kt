package com.filmstaden.app.ui.screens.tickets

import androidx.lifecycle.ViewModel
import com.filmstaden.app.data.models.Ticket
import com.filmstaden.app.data.repository.FilmstadenRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

data class MyTicketsState(
    val tickets: List<Ticket>
)

class MyTicketsViewModel(
    repository: FilmstadenRepository
) : ViewModel() {
    private val _state = MutableStateFlow(MyTicketsState(tickets = repository.getTickets()))
    val state: StateFlow<MyTicketsState> = _state.asStateFlow()
}
