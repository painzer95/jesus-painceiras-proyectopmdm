package com.jesuspainceiras.gestionpeliculas.models

import java.util.UUID

data class Usuario(
    val id: String = UUID.randomUUID().toString(), // Generamos un ID único por defecto.
    val email: String
)