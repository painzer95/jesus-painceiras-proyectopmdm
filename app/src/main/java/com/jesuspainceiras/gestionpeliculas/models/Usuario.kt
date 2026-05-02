package com.jesuspainceiras.gestionpeliculas.models

import java.util.UUID

data class Usuario(
    val id: String = UUID.randomUUID().toString(), // Generamos un ID único por defecto.
    val email: String
) {
    init {
        // Validamos que no esté vacío y que tenga un formato básico de correo electrónico.
        // Esto se lo he preguntado a la IA para mejorar un poco el tema de usuario y validaciones.
        require(email.isNotBlank()) { "El correo electrónico no puede estar vacío." }
        require(email.contains("@") && email.contains(".")) { "El formato del correo electrónico no es válido." }
    }
}