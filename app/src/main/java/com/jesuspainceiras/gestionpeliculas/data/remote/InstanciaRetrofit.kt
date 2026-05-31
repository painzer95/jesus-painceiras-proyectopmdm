package com.jesuspainceiras.gestionpeliculas.data.remote

import kotlinx.serialization.json.Json
import okhttp3.MediaType.Companion.toMediaType
import retrofit2.Retrofit
import retrofit2.converter.kotlinx.serialization.asConverterFactory

object InstanciaRetrofit {
    // Establecemos la URL base del servidor de producción.
    private const val BASE_URL = "https://moviesrestapi-production.up.railway.app/"

    // Ajustamos la configuración de JSON para ignorar campos nuevos que no tengamos mapeados en nuestros modelos.
    private val json = Json {
        ignoreUnknownKeys = true
    }

    // Inicializamos el constructor de Retrofit enlazándolo con el serializador de Kotlin.
    val retrofit by lazy {
        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(
                json.asConverterFactory(
                    "application/json; charset=utf-8".toMediaType()
                )
            )
            .build()
    }

    // Generamos la implementación de nuestro servicio de películas.
    val api: PeliculasApiService by lazy {
        retrofit.create(PeliculasApiService::class.java)
    }
}