package com.filmstaden.app.ui.screens.seatselection

import androidx.lifecycle.ViewModel
import com.filmstaden.app.data.models.Seat
import com.filmstaden.app.data.models.SeatStatus
import com.filmstaden.app.data.models.SeatTier
import com.filmstaden.app.data.repository.FilmstadenRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

data class SeatSelectionState(
    val movieId: String,
    val date: String,
    val time: String,
    val rows: List<List<Seat>>,
    val isPaymentSheetOpen: Boolean = false
) {
    val selectedSeats: List<Seat> get() = rows.flatten().filter { it.status == SeatStatus.SELECTED }
    val ticketCount: Int get() = selectedSeats.size
    val totalPrice: Int get() = selectedSeats.sumOf { it.tier.price }
    val seatsLabel: String get() = if (selectedSeats.isEmpty()) "—" else
        selectedSeats.joinToString(", ") { it.label }

    private val uniformTier: SeatTier?
        get() = selectedSeats.map { it.tier }.distinct().singleOrNull()

    val tierLabel: String
        get() = when (uniformTier) {
            SeatTier.GOOD -> "Ordinary"
            SeatTier.BETTER -> "Better"
            SeatTier.BEST -> "Best"
            null -> if (selectedSeats.isEmpty()) "Ordinary" else "Mixed"
        }

    val priceEachLabel: String
        get() = uniformTier?.let { "${it.price} SEK each" }
            ?: if (selectedSeats.isEmpty()) "${SeatTier.GOOD.price} SEK each" else "Mixed prices"

    val canAddSeat: Boolean get() = rows.flatten().any { it.status == SeatStatus.AVAILABLE }
    val canRemoveSeat: Boolean get() = selectedSeats.isNotEmpty()
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

    fun addSeat() {
        _state.update { current ->
            val flat = current.rows.flatten()
            val selected = flat.filter { it.status == SeatStatus.SELECTED }
            val preferredTier = selected.map { it.tier }.distinct().singleOrNull()
            val target = preferredTier
                ?.let { tier -> flat.firstOrNull { it.status == SeatStatus.AVAILABLE && it.tier == tier } }
                ?: flat.firstOrNull { it.status == SeatStatus.AVAILABLE }
                ?: return@update current

            current.copy(rows = current.rows.map { row ->
                row.map {
                    if (it.row == target.row && it.number == target.number) it.copy(status = SeatStatus.SELECTED)
                    else it
                }
            })
        }
    }

    fun removeSeat() {
        _state.update { current ->
            val target = current.rows.flatten().lastOrNull { it.status == SeatStatus.SELECTED }
                ?: return@update current
            current.copy(rows = current.rows.map { row ->
                row.map {
                    if (it.row == target.row && it.number == target.number) it.copy(status = SeatStatus.AVAILABLE)
                    else it
                }
            })
        }
    }

    fun openPayment() = _state.update { it.copy(isPaymentSheetOpen = true) }
    fun closePayment() = _state.update { it.copy(isPaymentSheetOpen = false) }
}
