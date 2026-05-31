package com.jesuspainceiras.gestionpeliculas.navigation

import androidx.compose.runtime.Composable
import androidx.navigation3.ui.NavDisplay
import androidx.navigation3.runtime.NavKey
import androidx.navigation3.runtime.entryProvider
import androidx.navigation3.runtime.rememberNavBackStack
import com.jesuspainceiras.gestionpeliculas.data.misPeliculasMock
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

// Modificamos el parámetro para pasar el ID de la lista en lugar del índice para la mejora objetada por el profesor. Al pasar una cadena vacía, significa que es una película nueva.
@Serializable
data class PantallaFormulario(val idPelicula: String = "") : NavKey

@Composable
fun AppNavigation() {
    val backStack = rememberNavBackStack(PantallaLogin)

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
                    onNavigateToFormulario = { id ->
                        backStack.add(PantallaFormulario(id))
                    },
                    // Le indicamos qué hacer cuando la pantalla nos avise de que el token ha caducado
                    onTokenCaducado = {
                        backStack.clear()
                        backStack.add(PantallaLogin)
                    }
                )
            }

            entry<PantallaFormulario> { navKey ->
                FormularioPeliculaScreen(
                    idPelicula = navKey.idPelicula,
                    listaPeliculas = misPeliculasMock,
                    onVolver = { backStack.removeLastOrNull() }
                )
            }
        }
    )
}