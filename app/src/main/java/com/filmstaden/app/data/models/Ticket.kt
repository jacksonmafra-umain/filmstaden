package com.filmstaden.app.data.models

data class Ticket(
    val id: String,
    val movie: Movie,
    val cinema: Cinema,
    val date: String,
    val time: String,
    val row: String,
    val seatNumber: String,
    val qrData: String,
    val ticketNumber: Int,
    val totalTickets: Int
)
