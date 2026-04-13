package com.filmstaden.app.data.models

data class Cinema(
    val id: String,
    val name: String,
    val location: String,
    val city: String,
    val hall: String = "Sal 4"
)
