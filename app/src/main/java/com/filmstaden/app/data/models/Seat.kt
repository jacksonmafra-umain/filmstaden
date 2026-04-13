package com.filmstaden.app.data.models

data class Seat(
    val row: Char,
    val number: Int,
    val tier: SeatTier,
    val status: SeatStatus
) {
    val label: String get() = "$row$number"
}

enum class SeatTier(val price: Int) {
    GOOD(99),
    BETTER(129),
    BEST(159)
}

enum class SeatStatus { AVAILABLE, RESERVED, SELECTED }
