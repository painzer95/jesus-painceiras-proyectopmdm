package com.jesuspainceiras.gestionpeliculas.screens

import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.jesuspainceiras.gestionpeliculas.R
import com.jesuspainceiras.gestionpeliculas.components.CineInput
import com.jesuspainceiras.gestionpeliculas.models.Pelicula

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FormularioPeliculaScreen(
    idPelicula: String,
    listaPeliculas: MutableList<Pelicula>,
    onVolver: () -> Unit
) {
    // Buscamos la película por ID en lugar de por índice.
    val peliculaEditar = listaPeliculas.find { it.id == idPelicula }
    val esNueva = peliculaEditar == null

    // Si estamos editando, cogemos los datos de la película. Si no, campos en blanco.
    var titulo by remember { mutableStateOf(peliculaEditar?.titulo ?: "") }
    var genero by remember { mutableStateOf(peliculaEditar?.genero ?: "") }
    var director by remember { mutableStateOf(peliculaEditar?.director ?: "") }
    var nota by remember { mutableStateOf(peliculaEditar?.nota?.toString() ?: "") }

    var errorCampos by remember { mutableStateOf(false) }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    // Internacionalizamos el título superior dependiendo de si es nueva o editada.
                    Text(
                        if (esNueva) stringResource(R.string.txt_add_movie_title) else stringResource(R.string.txt_edit_movie_title),
                        color = MaterialTheme.colorScheme.onPrimary
                    )
                },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = MaterialTheme.colorScheme.primary),
                // Añadimos el icono de navegación hacia atrás.
                navigationIcon = {
                    IconButton(onClick = { onVolver() }) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = stringResource(R.string.txt_go_back),
                            tint = MaterialTheme.colorScheme.onPrimary
                        )
                    }
                }
            )
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Top
        ) {
            CineInput(
                value = titulo,
                onValueChange = { titulo = it; errorCampos = false },
                label = stringResource(R.string.txt_filmTitle)
            )

            Spacer(modifier = Modifier.height(16.dp))

            CineInput(
                value = genero,
                onValueChange = { genero = it; errorCampos = false },
                label = stringResource(R.string.txt_genere)
            )

            Spacer(modifier = Modifier.height(16.dp))

            CineInput(
                value = director,
                onValueChange = { director = it; errorCampos = false },
                label = stringResource(R.string.txt_director)
            )

            Spacer(modifier = Modifier.height(16.dp))

            CineInput(
                value = nota,
                onValueChange = { nota = it; errorCampos = false },
                label = stringResource(R.string.txt_score)
            )

            if (errorCampos) {
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    stringResource(R.string.txt_errorEmptyTextField),
                    color = MaterialTheme.colorScheme.error,
                    fontSize = 12.sp
                )
            }

            Spacer(modifier = Modifier.height(32.dp))

            Button(
                onClick = {
                    val notaNumerica = nota.toDoubleOrNull()

                    // Añadimos validación para que la nota esté estrictamente entre 0 y 10 para la mejora objetada por el profesor.
                    val notaValida = notaNumerica != null && notaNumerica in 0.0..10.0

                    // Modificamos el control de errores usando isBlank para evitar cadenas de espacios.
                    if (titulo.isBlank() || genero.isBlank() || director.isBlank() || !notaValida) {
                        errorCampos = true
                    } else {
                        // Modificamos indicando que datos le pasamos al tener el ID automático.
                        if (esNueva) {
                            val nuevaPelicula = Pelicula(
                                titulo = titulo,
                                genero = genero,
                                director = director,
                                nota = notaNumerica!!
                            )
                            listaPeliculas.add(nuevaPelicula)
                        } else {
                            val peliculaModificada = Pelicula(
                                id = peliculaEditar!!.id, // Conservamos el ID original.
                                titulo = titulo,
                                genero = genero,
                                director = director,
                                nota = notaNumerica!!
                            )
                            // Buscamos la posición de la película en la lista original para actualizarla.
                            val indexActualizar = listaPeliculas.indexOfFirst { it.id == peliculaEditar.id }
                            if (indexActualizar != -1) {
                                listaPeliculas[indexActualizar] = peliculaModificada
                            }
                        }
                        onVolver()
                    }
                },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(stringResource(R.string.txt_buttonSaveFilm))
            }

            Spacer(modifier = Modifier.height(16.dp))

            TextButton(
                onClick = { onVolver() },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(stringResource(R.string.txt_buttonCancelEdit))
            }
        }
    }
}