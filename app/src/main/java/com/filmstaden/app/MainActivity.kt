package com.filmstaden.app

import android.os.Bundle
import androidx.activity.ComponentActivity
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
        enableEdgeToEdge()

        // Keep splash visible briefly for a smooth transition into the app.
        splash.setKeepOnScreenCondition { false }

        setContent {
            FilmstadenTheme {
                AppRoot()
            }
        }
    }
}
