package com.filmstaden.app.data.models

data class UserProfile(
    val name: String,
    val email: String,
    val memberNumber: String,
    val orders: Int,
    val points: Int,
    val spentKr: Int,
    val notificationsEnabled: Boolean
)
