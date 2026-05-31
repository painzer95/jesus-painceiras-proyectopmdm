package com.jesuspainceiras.gestionpeliculas.data.remote

import kotlinx.serialization.Serializable

// Obtenemos y guardamos el token de seguridad que nos autoriza a realizar el resto de operaciones.
@Serializable
data class LoginResponse(
    val token: String
)