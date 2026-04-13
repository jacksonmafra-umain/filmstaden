package com.filmstaden.app.ui.screens.more

import androidx.lifecycle.ViewModel
import com.filmstaden.app.data.models.UserProfile
import com.filmstaden.app.data.repository.FilmstadenRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

data class MoreState(
    val profile: UserProfile,
    val notificationsEnabled: Boolean
)

class MoreViewModel(
    repository: FilmstadenRepository
) : ViewModel() {
    private val _state = MutableStateFlow(
        MoreState(
            profile = repository.getUserProfile(),
            notificationsEnabled = repository.getUserProfile().notificationsEnabled
        )
    )
    val state: StateFlow<MoreState> = _state.asStateFlow()

    fun toggleNotifications() = _state.update { it.copy(notificationsEnabled = !it.notificationsEnabled) }
}
