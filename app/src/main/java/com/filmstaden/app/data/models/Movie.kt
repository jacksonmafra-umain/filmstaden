package com.filmstaden.app.data.models

import androidx.annotation.DrawableRes

data class Movie(
    val id: String,
    val title: String,
    @DrawableRes val posterResId: Int,
    @DrawableRes val heroResId: Int? = null,
    val genre: String,
    val duration: String,
    val year: String,
    val ageRating: String,
    val rating: Float,
    val description: String,
    val category: MovieCategory
)

enum class MovieCategory { NOW_PLAYING, COMING_SOON, TOP_MOVIES }
