package com.filmstaden.app.ui.screens.seatselection

import androidx.lifecycle.ViewModel
import com.filmstaden.app.data.models.Cinema
import com.filmstaden.app.data.models.Seat
import com.filmstaden.app.data.models.SeatStatus
import com.filmstaden.app.data.repository.FilmstadenRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

data class SeatSelectionState(
    val movieId: String,
    val date: String,
    val time: String,
    val cinema: Cinema,
    val rows: List<List<Seat>>,
    val ticketCount: Int = 2,
    val isPaymentSheetOpen: Boolean = false
) {
    val selectedSeats: List<Seat> get() = rows.flatten().filter { it.status == SeatStatus.SELECTED }
    val totalPrice: Int get() = selectedSeats.sumOf { it.tier.price }
    val seatsLabel: String get() = if (selectedSeats.isEmpty()) "—" else
        selectedSeats.joinToString(", ") { it.label }
}

class SeatSelectionViewModel(
    movieId: String,
    date: String,
    time: String,
    private val repository: FilmstadenRepository
) : ViewModel() {

    private val _state = MutableStateFlow(
        SeatSelectionState(
            movieId = movieId,
            date = date,
            time = time,
            cinema = repository.getSelectedCinema(),
            rows = repository.getSeatLayout()
        )
    )
    val state: StateFlow<SeatSelectionState> = _state.asStateFlow()

    fun toggleSeat(seat: Seat) {
        _state.update { current ->
            val newRows = current.rows.map { row ->
                row.map {
                    if (it.row == seat.row && it.number == seat.number && it.status != SeatStatus.RESERVED) {
                        it.copy(
                            status = if (it.status == SeatStatus.SELECTED) SeatStatus.AVAILABLE
                            else SeatStatus.SELECTED
                        )
                    } else it
                }
            }
            current.copy(rows = newRows)
        }
    }

    fun changeTicketCount(delta: Int) {
        _state.update { it.copy(ticketCount = (it.ticketCount + delta).coerceIn(1, 8)) }
    }

    fun openPayment() = _state.update { it.copy(isPaymentSheetOpen = true) }
    fun closePayment() = _state.update { it.copy(isPaymentSheetOpen = false) }
}
