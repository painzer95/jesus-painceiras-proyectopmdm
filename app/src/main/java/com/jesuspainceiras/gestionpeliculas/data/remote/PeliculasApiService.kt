package com.jesuspainceiras.gestionpeliculas.data.remote

import com.jesuspainceiras.gestionpeliculas.models.Pelicula
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Path

// Definimos las peticiones que realizaremos al servidor de películas.
interface PeliculasApiService {

    // Enviamos los datos de un nuevo usuario para realizar el registro en el sistema.
    @POST("api/v1/users/signup")
    fun registrarUsuario(
        @Body loginRequest: LoginRequest
    ): RegistroResponse

    // Enviamos las credenciales para obtener el token de acceso seguro.
    @POST("api/v1/users/login")
    fun iniciarSesion(
        @Body loginRequest: LoginRequest
    ): LoginResponse

    // Obtenemos la lista completa de películas desde la API.
    // Pasamos el token de seguridad obligatoriamente en la cabecera (Header).
    @GET("api/v1/movies")
    fun obtenerPeliculas(
        @Header("Authorization") token: String
    ): List<Pelicula>

    // Obtendremos una película por su identificador único para mostrar su detalle completo.
    @GET("api/v1/movies/{id}")
    fun obtenerDetallePelicula(
        @Header("Authorization") token: String,
        @Path("id") id: String
    ): Pelicula

    // Crearemos una nueva película mediante una petición POST enviando el objeto correspondiente.
    @POST("api/v1/movies")
    fun crearPelicula(
        @Header("Authorization") token: String,
        @Body pelicula: Pelicula
    ): Pelicula

    // Actualizaremos todos los datos de una película existente basándonos en su identificador único.
    @PUT("api/v1/movies/{id}")
    fun actualizarPelicula(
        @Header("Authorization") token: String,
        @Path("id") id: String,
        @Body pelicula: Pelicula
    ): Pelicula

    // Eliminaremos una película del servidor mediante una petición DELETE pasando su ID en la URL.
    @DELETE("api/v1/movies/{id}")
    fun eliminarPelicula(
        @Header("Authorization") token: String,
        @Path("id") id: String
    ): Unit
}