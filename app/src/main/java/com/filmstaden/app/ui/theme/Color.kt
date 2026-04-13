package com.filmstaden.app.ui.theme

import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color

// Brand
val FsRed = Color(0xFFC5003E)
val FsRedDark = Color(0xFF8B002C)
val FsRedLight = Color(0xFFE8174F)

// Surfaces
val BgDark = Color(0xFF0A0A0A)
val BgCard = Color(0xFF1A1A1A)
val BgCardLight = Color(0xFF2A2A2A)
val BgSurface = Color(0xFF141414)

// Text
val TextPrimary = Color(0xFFFFFFFF)
val TextMuted = Color(0xFF999999)
val TextSubtle = Color(0xFF666666)

// Borders
val BorderSubtle = Color(0xFF333333)

// Seats
val SeatGood = Color(0xFF2DD4BF)
val SeatBetter = Color(0xFF6366F1)
val SeatBest = FsRed
val SeatReserved = Color(0xFF4B5563)
val SeatSelected = Color(0xFF22D3EE)

// Status
val Success = Color(0xFF10B981)
val SwishGreen = Color(0xFF59B748)

// Gradients
val FsRedGradient: Brush
    get() = Brush.verticalGradient(
        0.0f to FsRedDark,
        0.6f to FsRed,
        1.0f to FsRedLight
    )

val FsRedGradientHorizontal: Brush
    get() = Brush.horizontalGradient(
        0.0f to FsRed,
        1.0f to FsRedLight
    )
