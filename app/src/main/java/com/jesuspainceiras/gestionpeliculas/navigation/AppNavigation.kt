package com.jesuspainceiras.gestionpeliculas.navigation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.remember
import androidx.navigation3.ui.NavDisplay
import androidx.navigation3.runtime.NavKey
import androidx.navigation3.runtime.entryProvider
import androidx.navigation3.runtime.rememberNavBackStack
import com.jesuspainceiras.gestionpeliculas.models.Pelicula
import com.jesuspainceiras.gestionpeliculas.screens.RegistroScreen
import com.jesuspainceiras.gestionpeliculas.screens.LoginScreen
import com.jesuspainceiras.gestionpeliculas.screens.ListaPeliculasScreen
import com.jesuspainceiras.gestionpeliculas.screens.FormularioPeliculaScreen
import kotlinx.serialization.Serializable

@Serializable
data object PantallaLogin : NavKey

@Serializable
data object PantallaRegistro : NavKey

@Serializable
data object PantallaLista : NavKey

// Le pasamos el índice de la lista. Al poner -1, significa que es una película nueva.
@Serializable
data class PantallaFormulario(val indicePelicula: Int = -1) : NavKey

@Composable
fun AppNavigation() {
    val backStack = rememberNavBackStack(PantallaLogin)

    // Guardamos la lista mutable de películas.
    val misPeliculas = remember {
        mutableStateListOf(
            Pelicula("Expediente Warren: The Conjuring", "Terror", "James Wan", 6.8),
            Pelicula("La llegada", "Ciencia ficción", "Denis Villeneuve", 7.3),
            Pelicula("Mad Max: Furia en la carretera", "Ciencia ficción", "George Miller", 7.1)
        )
    }

    NavDisplay(
        backStack = backStack,
        entryProvider = entryProvider {

            entry<PantallaLogin> {
                LoginScreen(
                    onNavigateToRegistro = { backStack.add(PantallaRegistro) },
                    onLoginSuccess = {
                        backStack.clear()
                        backStack.add(PantallaLista)
                    }
                )
            }

            entry<PantallaRegistro> {
                RegistroScreen(
                    onNavigateToLogin = { backStack.removeLastOrNull() }
                )
            }

            entry<PantallaLista> {
                ListaPeliculasScreen(
                    peliculas = misPeliculas,
                    onNavigateToFormulario = { indice ->
                        backStack.add(PantallaFormulario(indice))
                    }
                )
            }

            entry<PantallaFormulario> { navKey ->
                FormularioPeliculaScreen(
                    indice = navKey.indicePelicula,
                    listaPeliculas = misPeliculas,
                    onVolver = { backStack.removeLastOrNull() }
                )
            }
        }
    )
}