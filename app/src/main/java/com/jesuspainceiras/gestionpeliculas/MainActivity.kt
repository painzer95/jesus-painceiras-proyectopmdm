package com.jesuspainceiras.gestionpeliculas

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import com.jesuspainceiras.gestionpeliculas.navigation.AppNavigation
import com.jesuspainceiras.gestionpeliculas.ui.theme.GestionPeliculasTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            GestionPeliculasTheme {
                AppNavigation()
            }
        }
    }
}