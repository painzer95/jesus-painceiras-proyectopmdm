package com.jesuspainceiras.gestionpeliculas.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.jesuspainceiras.gestionpeliculas.models.Pelicula
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ListaPeliculasScreen(
    peliculas: MutableList<Pelicula>,
    onNavigateToFormulario: (Int) -> Unit
) {
    // Variables para gestionar el SnackBar (Notificación inferior para deshacer).
    val snackbarHostState = remember { SnackbarHostState() }
    val coroutineScope = rememberCoroutineScope()

    // Alerta de confirmación de borrado.
    var mostrarDialogoBorrar by remember { mutableStateOf(false) }
    var peliculaABorrarIndex by remember { mutableStateOf(-1) }

    Scaffold(
        snackbarHost = { SnackbarHost(snackbarHostState) },
        topBar = {
            TopAppBar(
                title = { Text("Lista de películas", color = MaterialTheme.colorScheme.onPrimary) },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = MaterialTheme.colorScheme.primary)
            )
        },
        floatingActionButton = {
            FloatingActionButton(
                onClick = { onNavigateToFormulario(-1) }, // -1 para crear una nueva.
                containerColor = MaterialTheme.colorScheme.primary,
                contentColor = MaterialTheme.colorScheme.onPrimary
            ) {
                Icon(Icons.Filled.Add, contentDescription = "Añadir película")
            }
        }
    ) { paddingValues ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(horizontal = 16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            item { Spacer(modifier = Modifier.height(8.dp)) }

            // Usamos itemsIndexed para saber la posición de la película en la lista.
            itemsIndexed(peliculas) { index, pelicula ->
                TarjetaPelicula(
                    pelicula = pelicula,
                    onClick = { onNavigateToFormulario(index) }, // Editar película.
                    onDeleteClick = {
                        peliculaABorrarIndex = index
                        mostrarDialogoBorrar = true
                    }
                )
            }

            item { Spacer(modifier = Modifier.height(80.dp)) }
        }

        // Ventana emergente para confirmar borrado.
        if (mostrarDialogoBorrar) {
            AlertDialog(
                onDismissRequest = { mostrarDialogoBorrar = false },
                title = { Text("Eliminar película") },
                text = { Text("Estás seguro de que quieres borrar esta película?") },
                confirmButton = {
                    Button(
                        onClick = {
                            val peliculaGuardada = peliculas[peliculaABorrarIndex]
                            val indexGuardado = peliculaABorrarIndex

                            // Borramos la película.
                            peliculas.removeAt(peliculaABorrarIndex)
                            mostrarDialogoBorrar = false

                            // Lanzamos el SnackBar para poder deshacer.
                            coroutineScope.launch {
                                val resultado = snackbarHostState.showSnackbar(
                                    message = "Película eliminada",
                                    actionLabel = "Deshacer",
                                    duration = SnackbarDuration.Short
                                )
                                if (resultado == SnackbarResult.ActionPerformed) {
                                    // Si pulsamos deshacer, la volvemos a meter donde estaba.
                                    peliculas.add(indexGuardado, peliculaGuardada)
                                }
                            }
                        }
                    ) {
                        Text("Sí, borrar")
                    }
                },
                dismissButton = {
                    TextButton(onClick = { mostrarDialogoBorrar = false }) {
                        Text("Cancelar")
                    }
                }
            )
        }
    }
}

@Composable
fun TarjetaPelicula(
    pelicula: Pelicula,
    onClick: () -> Unit,
    onDeleteClick: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onClick() }, // Al hacer clic en la tarjeta, la editamos.
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
        shape = RoundedCornerShape(8.dp),
        // Cambiamos el color de la tarjeta a surface para la mejora de coherencia visual objetada por el profesor.
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier
                    .size(width = 70.dp, height = 100.dp)
                    .background(MaterialTheme.colorScheme.secondaryContainer)
            )

            Spacer(modifier = Modifier.width(16.dp))

            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = pelicula.titulo,
                    fontWeight = FontWeight.Bold,
                    fontSize = 16.sp,
                    // Usamos onSurface para la mejora de contraste respecto al nuevo fondo de la tarjeta.
                    color = MaterialTheme.colorScheme.onSurface
                )
                Spacer(modifier = Modifier.height(4.dp))
                Text(text = "Género: ${pelicula.genero}", color = MaterialTheme.colorScheme.onSurfaceVariant, fontSize = 14.sp)
                Text(text = "Director: ${pelicula.director}", color = MaterialTheme.colorScheme.onSurfaceVariant, fontSize = 14.sp)
            }

            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Text(
                    text = pelicula.nota.toString(),
                    fontWeight = FontWeight.Bold,
                    fontSize = 24.sp,
                    color = MaterialTheme.colorScheme.primary
                )
                Spacer(modifier = Modifier.height(8.dp))
                // Botón de papelera.
                IconButton(onClick = { onDeleteClick() }) {
                    Icon(Icons.Filled.Delete, contentDescription = "Borrar película", tint = MaterialTheme.colorScheme.error)
                }
            }
        }
    }
}