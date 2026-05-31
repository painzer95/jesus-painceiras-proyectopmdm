package com.jesuspainceiras.gestionpeliculas.data

import androidx.compose.runtime.mutableStateListOf
import com.jesuspainceiras.gestionpeliculas.models.Pelicula

// Usamos mutableStateListOf para que la UI se actualice automáticamente.
// Al haber añadido el id automático debemos indicarle al constructor los datos que le pasamos exactamente.
// Hemos sustituido las URLs por las de TMDB (The Movie Database) para evitar los bloqueos de seguridad de IMDb.
val misPeliculasMock = mutableStateListOf(
    Pelicula(
        titulo = "Expediente Warren: The Conjuring",
        genero = "Terror",
        director = "James Wan",
        nota = 6.8,
        imagenUrl = "https://image.tmdb.org/t/p/w500/wVYREutTvI2tmxr6ujrHT704wGF.jpg"
    ),
    Pelicula(
        titulo = "La llegada",
        genero = "Ciencia ficción",
        director = "Denis Villeneuve",
        nota = 7.3,
        imagenUrl = "https://image.tmdb.org/t/p/w500/x2FJsf1ElAgr63Y3PNPtJrcmpoe.jpg"
    ),
    Pelicula(
        titulo = "Mad Max: Furia en la carretera",
        genero = "Ciencia ficción",
        director = "George Miller",
        nota = 7.1,
        imagenUrl = "https://image.tmdb.org/t/p/w500/8tZYtuWezp8JbcsvHYO0O46tFbo.jpg"
    ),
    Pelicula(
        titulo = "Al filo del mañana",
        genero = "Ciencia ficción",
        director = "Doug Liman",
        nota = 7.0,
        imagenUrl = "https://image.tmdb.org/t/p/w500/uUHvlkLavotfGsNtosDy8ShsIYF.jpg"
    ),
    Pelicula(
        titulo = "Interstellar",
        genero = "Ciencia ficción",
        director = "Christopher Nolan",
        nota = 8.6,
        imagenUrl = "https://image.tmdb.org/t/p/w500/gEU2QniE6E77NI6lCU6MxlNBvIx.jpg"
    )
)