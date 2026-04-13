package com.filmstaden.app.ui.theme

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.runtime.Composable

private val FilmstadenColorScheme = darkColorScheme(
    primary = FsRed,
    onPrimary = TextPrimary,
    primaryContainer = FsRedDark,
    onPrimaryContainer = TextPrimary,
    secondary = FsRedLight,
    onSecondary = TextPrimary,
    tertiary = SeatSelected,
    background = BgDark,
    onBackground = TextPrimary,
    surface = BgCard,
    onSurface = TextPrimary,
    surfaceVariant = BgCardLight,
    onSurfaceVariant = TextMuted,
    outline = BorderSubtle,
    outlineVariant = BorderSubtle,
    error = FsRed,
    onError = TextPrimary
)

@Composable
fun FilmstadenTheme(
    content: @Composable () -> Unit
) {
    MaterialTheme(
        colorScheme = FilmstadenColorScheme,
        typography = Typography,
        content = content
    )
}
