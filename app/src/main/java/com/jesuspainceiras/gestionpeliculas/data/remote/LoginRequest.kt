package com.jesuspainceiras.gestionpeliculas.data.remote

import kotlinx.serialization.Serializable

// Definimos la estructura de datos que enviamos al servidor para autenticarnos o registrarnos.
@Serializable
data class LoginRequest(
    val email: String,
    val password: String
)