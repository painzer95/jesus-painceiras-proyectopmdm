package com.jesuspainceiras.gestionpeliculas.models

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class Pelicula(
    // Enlazamos nuestra variable 'id' con el campo "id" de la API.
    @SerialName("id")
    val id: String = "",

    // Enlazamos 'titulo' con "title".
    @SerialName("title")
    val titulo: String,

    // Enlazamos 'genero' con "genre".
    @SerialName("genre")
    val genero: String,

    // Enlazamos 'director' con "directorFullname".
    @SerialName("directorFullname")
    val director: String,

    // Enlazamos 'nota' con "rating".
    @SerialName("rating")
    val nota: Double,

    // Añadimos la URL de la carátula para poder usar Coil más adelante y cargar la imagen.
    @SerialName("imageUrl")
    val imagenUrl: String? = null
)