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
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.jesuspainceiras.gestionpeliculas.R
import com.jesuspainceiras.gestionpeliculas.models.Pelicula
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ListaPeliculasScreen(
    peliculas: MutableList<Pelicula>,
    // Modificamos el parámetro para que reciba un String (ID) en lugar de un Int.
    onNavigateToFormulario: (String) -> Unit
) {
    // Variables para gestionar el SnackBar (Notificación inferior para deshacer).
    val snackbarHostState = remember { SnackbarHostState() }
    val coroutineScope = rememberCoroutineScope()

    // Extraemos los textos de la notificación aquí arriba para evitar el fallo dentro de la corrutina.
    val mensajeBorrado = stringResource(R.string.txt_movie_deleted)
    val textoDeshacer = stringResource(R.string.txt_undo)

    // Alerta de confirmación de borrado.
    var mostrarDialogoBorrar by remember { mutableStateOf(false) }
    var peliculaABorrarIndex by remember { mutableStateOf(-1) }

    Scaffold(
        snackbarHost = { SnackbarHost(snackbarHostState) },
        topBar = {
            TopAppBar(
                // Internacionalizamos los textos de la barra superior y los botones.
                title = { Text(stringResource(R.string.txt_movie_list), color = MaterialTheme.colorScheme.onPrimary) },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = MaterialTheme.colorScheme.primary)
            )
        },
        floatingActionButton = {
            FloatingActionButton(
                // Modificamos el paso del parámetro a una cadena vacía.
                onClick = { onNavigateToFormulario("") }, // Cadena vacía para crear una nueva.
                containerColor = MaterialTheme.colorScheme.primary,
                contentColor = MaterialTheme.colorScheme.onPrimary
            ) {
                Icon(Icons.Filled.Add, contentDescription = stringResource(R.string.txt_add_movie))
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
                    onClick = { onNavigateToFormulario(pelicula.id) }, // Editar película pasando su ID.
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
                title = { Text(stringResource(R.string.txt_delete_movie)) },
                text = { Text(stringResource(R.string.txt_confirm_delete)) },
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
                                // Utilizamos las variables preparadas previamente.
                                val resultado = snackbarHostState.showSnackbar(
                                    message = mensajeBorrado,
                                    actionLabel = textoDeshacer,
                                    duration = SnackbarDuration.Short
                                )
                                if (resultado == SnackbarResult.ActionPerformed) {
                                    // Si pulsamos deshacer, la volvemos a meter donde estaba.
                                    peliculas.add(indexGuardado, peliculaGuardada)
                                }
                            }
                        }
                    ) {
                        Text(stringResource(R.string.txt_yes_delete))
                    }
                },
                dismissButton = {
                    TextButton(onClick = { mostrarDialogoBorrar = false }) {
                        Text(stringResource(R.string.txt_cancel))
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
                    color = MaterialTheme.colorScheme.onSurface
                )
                Spacer(modifier = Modifier.height(4.dp))
                // Formateamos las cadenas usando stringResource con argumentos.
                Text(text = stringResource(R.string.txt_genre_label, pelicula.genero), color = MaterialTheme.colorScheme.onSurfaceVariant, fontSize = 14.sp)
                Text(text = stringResource(R.string.txt_director_label, pelicula.director), color = MaterialTheme.colorScheme.onSurfaceVariant, fontSize = 14.sp)
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
                    Icon(Icons.Filled.Delete, contentDescription = stringResource(R.string.txt_delete_movie_desc), tint = MaterialTheme.colorScheme.error)
                }
            }
        }
    }
}