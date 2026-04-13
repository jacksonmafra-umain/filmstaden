package com.filmstaden.app.ui

import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.filmstaden.app.R
import com.filmstaden.app.ui.components.PathTraceFromSvg
import com.filmstaden.app.ui.theme.BgDark

@Composable
fun FilmstadenSplash(onFinished: () -> Unit) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(BgDark),
        contentAlignment = Alignment.Center
    ) {
        // The logo's viewBox is 146x32 — preserve aspect ratio so the Canvas
        // measures to a non-zero size.
        PathTraceFromSvg(
            drawableId = R.drawable.ic_filmstaden_logo,
            modifier = Modifier.size(width = 220.dp, height = 48.dp),
            speedMs = 1600,
            pauseMs = 400,
            easing = FastOutSlowInEasing,
            stopSignal = true,
            strokeColor = Color.White,
            onCycle = onFinished
        )
    }
}
