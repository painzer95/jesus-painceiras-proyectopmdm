package com.jesuspainceiras.gestionpeliculas.data

import androidx.compose.runtime.mutableStateListOf
import com.jesuspainceiras.gestionpeliculas.models.Pelicula

// Usamos mutableStateListOf para que la UI se actualice automáticamente.
// Al haber añadido el id automático debemos indicarle al constructor los datos que le pasamos exactamente.
val misPeliculasMock = mutableStateListOf(
    Pelicula(titulo = "Expediente Warren: The Conjuring", genero = "Terror", director = "James Wan", nota = 6.8),
    Pelicula(titulo = "La llegada", genero = "Ciencia ficción", director = "Denis Villeneuve", nota = 7.3),
    Pelicula(titulo = "Mad Max: Furia en la carretera", genero = "Ciencia ficción", director = "George Miller", nota = 7.1),
    Pelicula(titulo = "Al filo del mañana", genero = "Ciencia ficción", director = "Doug Liman", nota = 7.0),
    Pelicula(titulo = "Interstellar", genero = "Ciencia ficción", director = "Christopher Nolan", nota = 8.6)
)