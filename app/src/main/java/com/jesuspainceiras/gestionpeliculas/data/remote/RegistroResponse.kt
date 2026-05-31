package com.jesuspainceiras.gestionpeliculas.data.remote

import kotlinx.serialization.Serializable

// Recibimos la confirmación del servidor con nuestro nuevo ID y el correo tras un registro exitoso.
@Serializable
data class RegistroResponse(
    val email: String,
    val id: String
)