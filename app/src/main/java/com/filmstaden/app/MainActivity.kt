package com.filmstaden.app

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import com.filmstaden.app.ui.AppRoot
import com.filmstaden.app.ui.theme.FilmstadenTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            FilmstadenTheme {
                AppRoot()
            }
        }
    }
}
