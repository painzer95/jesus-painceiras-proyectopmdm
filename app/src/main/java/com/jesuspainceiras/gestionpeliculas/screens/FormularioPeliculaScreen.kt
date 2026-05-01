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
    indice: Int,
    listaPeliculas: MutableList<Pelicula>,
    onVolver: () -> Unit
) {
    val esNueva = indice == -1

    // Si estamos editando, cogemos los datos de la película. Si no, campos en blanco.
    var titulo by remember { mutableStateOf(if (esNueva) "" else listaPeliculas[indice].titulo) }
    var genero by remember { mutableStateOf(if (esNueva) "" else listaPeliculas[indice].genero) }
    var director by remember { mutableStateOf(if (esNueva) "" else listaPeliculas[indice].director) }
    var nota by remember { mutableStateOf(if (esNueva) "" else listaPeliculas[indice].nota.toString()) }

    var errorCampos by remember { mutableStateOf(false) }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        if (esNueva) "Añadir Película" else "Editar Película",
                        color = MaterialTheme.colorScheme.onPrimary
                    )
                },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = MaterialTheme.colorScheme.primary),
                // Añadimos el icono de navegación hacia atrás para la mejora objetada por el profesor.
                navigationIcon = {
                    IconButton(onClick = { onVolver() }) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "Volver atrás",
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
                    if (titulo.isEmpty() || genero.isEmpty() || director.isEmpty() || notaNumerica == null) {
                        errorCampos = true
                    } else {
                        // Aquí debemos modificar de nuevo indicando que datos le pasamos al tener ahora el ID automático.
                        val nuevaPelicula = Pelicula(
                            titulo = titulo,
                            genero = genero,
                            director = director,
                            nota = notaNumerica
                        )
                        if (esNueva) {
                            listaPeliculas.add(nuevaPelicula)
                        } else {
                            listaPeliculas[indice] = nuevaPelicula
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