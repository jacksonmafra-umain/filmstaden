package com.filmstaden.app

import android.graphics.Color
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.SystemBarStyle
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import com.filmstaden.app.ui.AppRoot
import com.filmstaden.app.ui.theme.FilmstadenTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        // Install AndroidX splash screen API. Must be called BEFORE super.onCreate.
        val splash = installSplashScreen()
        super.onCreate(savedInstanceState)
        // Transparent system bars with light icons — the app is always dark themed,
        // so content draws behind the status and navigation bars and we rely on
        // per-screen `navigationBarsPadding()` / hero imagery for visual treatment.
        enableEdgeToEdge(
            statusBarStyle = SystemBarStyle.dark(Color.TRANSPARENT),
            navigationBarStyle = SystemBarStyle.dark(Color.TRANSPARENT)
        )

        // Keep splash visible briefly for a smooth transition into the app.
        splash.setKeepOnScreenCondition { false }

        setContent {
            FilmstadenTheme {
                AppRoot()
            }
        }
    }
}
