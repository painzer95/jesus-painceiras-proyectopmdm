package com.jesuspainceiras.gestionpeliculas.models

import kotlinx.serialization.Serializable

// Definimos las clases de datos que usaremos para comunicarnos con la API

// Usamos este modelo tanto para enviar los datos de registro como los de login
@Serializable
data class AuthRequest(
    val email: String,
    val password: String
)

// Capturamos el token que nos devuelve el servidor si el login es correcto
@Serializable
data class LoginResponse(
    val token: String
)

// Capturamos el ID y el email que nos devuelve el servidor al registrarnos
@Serializable
data class RegistroResponse(
    val id: String,
    val email: String
)