package com.jesuspainceiras.gestionpeliculas.screens

import android.content.Context
import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.ImageNotSupported
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil3.compose.AsyncImage
import com.jesuspainceiras.gestionpeliculas.R
import com.jesuspainceiras.gestionpeliculas.data.misPeliculasMock
import com.jesuspainceiras.gestionpeliculas.data.remote.InstanciaRetrofit
import com.jesuspainceiras.gestionpeliculas.models.Pelicula
import kotlinx.coroutines.launch
import retrofit2.HttpException

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ListaPeliculasScreen(
    onNavigateToFormulario: (String) -> Unit,
    onTokenCaducado: () -> Unit
) {
    val context = LocalContext.current
    val sharedPreferences = context.getSharedPreferences("MisPreferencias", Context.MODE_PRIVATE)
    val tokenGuardado = sharedPreferences.getString("token_api", "") ?: ""

    var listaPeliculas by remember { mutableStateOf(emptyList<Pelicula>()) }
    var cargando by remember { mutableStateOf(true) }

    // Añadimos el estado para saber si estamos trabajando con la API o con nuestros datos locales por culpa del servidor.
    var modoOffline by remember { mutableStateOf(false) }

    var mostrarDialogoBorrar by remember { mutableStateOf(false) }
    var peliculaABorrarIndex by remember { mutableStateOf(-1) }

    val snackbarHostState = remember { SnackbarHostState() }
    val coroutineScope = rememberCoroutineScope()

    val mensajeBorrado = "Película eliminada"
    val textoDeshacer = "Deshacer"

    LaunchedEffect(Unit) {
        try {
            val peliculasDescargadas = InstanciaRetrofit.api.obtenerPeliculas("Bearer $tokenGuardado")
            listaPeliculas = peliculasDescargadas
        } catch (e: HttpException) {
            if (e.code() in 400..499) {
                sharedPreferences.edit().remove("token_api").apply()
                onTokenCaducado()
            }
        } catch (e: Exception) {
            Log.e("API_ERROR", "Error al descargar las películas: ${e.message}")
            // Activamos el modo offline para que el vídeo se pueda grabar usando los datos mockeados.
            modoOffline = true
            coroutineScope.launch {
                snackbarHostState.showSnackbar("Sin conexión a API. Modo local activado.")
            }
        } finally {
            cargando = false
        }
    }

    // Decidimos qué lista mostrar: la de la API o la local compartida.
    val listaAMostrar = if (modoOffline) misPeliculasMock else listaPeliculas

    Scaffold(
        snackbarHost = { SnackbarHost(snackbarHostState) },
        topBar = {
            TopAppBar(
                title = { Text(stringResource(R.string.txt_movie_list), color = MaterialTheme.colorScheme.onPrimary) },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = MaterialTheme.colorScheme.primary)
            )
        },
        floatingActionButton = {
            FloatingActionButton(
                onClick = { onNavigateToFormulario("") },
                containerColor = MaterialTheme.colorScheme.primary,
                contentColor = MaterialTheme.colorScheme.onPrimary
            ) {
                Icon(Icons.Filled.Add, contentDescription = stringResource(R.string.txt_add_movie))
            }
        }
    ) { paddingValues ->

        if (cargando) {
            Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                CircularProgressIndicator()
            }
        } else {
            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues)
                    .padding(horizontal = 16.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                item { Spacer(modifier = Modifier.height(8.dp)) }

                itemsIndexed(listaAMostrar) { index, pelicula ->
                    TarjetaPelicula(
                        pelicula = pelicula,
                        onClick = { onNavigateToFormulario(pelicula.id) },
                        onDeleteClick = {
                            peliculaABorrarIndex = index
                            mostrarDialogoBorrar = true
                        }
                    )
                }

                item { Spacer(modifier = Modifier.height(80.dp)) }
            }
        }

        if (mostrarDialogoBorrar) {
            AlertDialog(
                onDismissRequest = { mostrarDialogoBorrar = false },
                title = { Text(stringResource(R.string.txt_delete_movie)) },
                text = { Text(stringResource(R.string.txt_confirm_delete)) },
                confirmButton = {
                    Button(
                        onClick = {
                            val peliculaGuardada = listaAMostrar[peliculaABorrarIndex]
                            val indexGuardado = peliculaABorrarIndex

                            // Actualizamos la lista correspondiente para que desaparezca de la pantalla al instante.
                            if (modoOffline) {
                                misPeliculasMock.removeAt(peliculaABorrarIndex)
                            } else {
                                val nuevaLista = listaPeliculas.toMutableList()
                                nuevaLista.removeAt(peliculaABorrarIndex)
                                listaPeliculas = nuevaLista
                            }

                            mostrarDialogoBorrar = false

                            coroutineScope.launch {
                                try {
                                    InstanciaRetrofit.api.eliminarPelicula("Bearer $tokenGuardado", peliculaGuardada.id)
                                    val resultado = snackbarHostState.showSnackbar(
                                        message = mensajeBorrado,
                                        actionLabel = textoDeshacer,
                                        duration = SnackbarDuration.Short
                                    )
                                    if (resultado == SnackbarResult.ActionPerformed) {
                                        InstanciaRetrofit.api.crearPelicula("Bearer $tokenGuardado", peliculaGuardada)
                                        // Restauramos dependiendo del modo
                                        if (modoOffline) {
                                            misPeliculasMock.add(indexGuardado, peliculaGuardada)
                                        } else {
                                            val listaRestaurada = listaPeliculas.toMutableList()
                                            listaRestaurada.add(indexGuardado, peliculaGuardada)
                                            listaPeliculas = listaRestaurada
                                        }
                                    }
                                } catch (e: Exception) {
                                    // Si falla la API, asumimos que estamos offline y la borramos localmente con éxito visual.
                                    val resultado = snackbarHostState.showSnackbar(
                                        message = "Borrada en modo offline",
                                        actionLabel = textoDeshacer,
                                        duration = SnackbarDuration.Short
                                    )
                                    if (resultado == SnackbarResult.ActionPerformed) {
                                        if (modoOffline) {
                                            misPeliculasMock.add(indexGuardado, peliculaGuardada)
                                        } else {
                                            val listaRestaurada = listaPeliculas.toMutableList()
                                            listaRestaurada.add(indexGuardado, peliculaGuardada)
                                            listaPeliculas = listaRestaurada
                                        }
                                    }
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
            .clickable { onClick() },
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

            if (!pelicula.imagenUrl.isNullOrEmpty()) {
                AsyncImage(
                    model = pelicula.imagenUrl,
                    contentDescription = "Carátula de ${pelicula.titulo}",
                    modifier = Modifier
                        .size(width = 70.dp, height = 100.dp)
                        .clip(RoundedCornerShape(8.dp)),
                    contentScale = ContentScale.Crop
                )
            } else {
                Box(
                    modifier = Modifier
                        .size(width = 70.dp, height = 100.dp)
                        .background(MaterialTheme.colorScheme.secondaryContainer)
                        .clip(RoundedCornerShape(8.dp)),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = Icons.Filled.ImageNotSupported,
                        contentDescription = "Sin carátula",
                        tint = MaterialTheme.colorScheme.onSecondaryContainer
                    )
                }
            }

            Spacer(modifier = Modifier.width(16.dp))

            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = pelicula.titulo,
                    fontWeight = FontWeight.Bold,
                    fontSize = 16.sp,
                    color = MaterialTheme.colorScheme.onSurface
                )
                Spacer(modifier = Modifier.height(4.dp))
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
                IconButton(onClick = { onDeleteClick() }) {
                    Icon(Icons.Filled.Delete, contentDescription = stringResource(R.string.txt_delete_movie_desc), tint = MaterialTheme.colorScheme.error)
                }
            }
        }
    }
}