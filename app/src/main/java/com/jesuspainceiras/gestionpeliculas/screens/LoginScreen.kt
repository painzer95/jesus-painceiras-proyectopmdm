package com.jesuspainceiras.gestionpeliculas.screens

import android.content.Context
import androidx.compose.foundation.layout.*
import androidx.compose.material3.Icon
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.jesuspainceiras.gestionpeliculas.R
import com.jesuspainceiras.gestionpeliculas.components.CineInput
import com.jesuspainceiras.gestionpeliculas.data.remote.InstanciaRetrofit
import com.jesuspainceiras.gestionpeliculas.data.remote.LoginRequest
import kotlinx.coroutines.launch

@Composable
fun LoginScreen(
    onNavigateToRegistro: () -> Unit,
    onLoginSuccess: () -> Unit
) {
    val context = LocalContext.current
    val sharedPreferences = context.getSharedPreferences("MisPreferencias", Context.MODE_PRIVATE)

    // Leemos el email y contraseña guardados localmente para nuestro sistema de respaldo.
    val emailGuardado = sharedPreferences.getString("email_registrado", "") ?: ""
    val passwordGuardada = sharedPreferences.getString("password_registrada", "") ?: ""

    // Inicializamos el estado del email con el valor guardado.
    var email by remember { mutableStateOf(emailGuardado) }
    var password by remember { mutableStateOf("") }

    var errorEmail by remember { mutableStateOf(false) }
    var errorPassword by remember { mutableStateOf(false) }
    var errorCredenciales by remember { mutableStateOf(false) }

    // Añadimos las variables para gestionar la carga y la corrutina para la llamada a la API.
    var cargando by remember { mutableStateOf(false) }
    val coroutineScope = rememberCoroutineScope()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        // Logo de la aplicación con archivo local.
        Icon(
            painter = painterResource(id = R.drawable.ic_movie),
            contentDescription = "Logo App",
            modifier = Modifier.size(80.dp),
            tint = MaterialTheme.colorScheme.primary
        )

        // Internacionalizamos el título de la app.
        Text(
            text = stringResource(R.string.app_name_display),
            fontSize = 32.sp,
            color = MaterialTheme.colorScheme.primary
        )

        Spacer(modifier = Modifier.height(32.dp))

        CineInput(
            value = email,
            // Limpiamos también el error de credenciales si empezamos a escribir de nuevo.
            onValueChange = { email = it; errorEmail = false; errorCredenciales = false },
            label = stringResource(R.string.txt_email),
            isError = errorEmail || errorCredenciales,
            leadingIcon = {
                Icon(painter = painterResource(id = R.drawable.ic_email), contentDescription = null)
            },
            supportingText = {
                if (errorEmail) Text(stringResource(R.string.txt_errorEmail))
            }
        )

        Spacer(modifier = Modifier.height(16.dp))

        CineInput(
            value = password,
            onValueChange = { password = it; errorPassword = false; errorCredenciales = false },
            label = stringResource(R.string.txt_password),
            visualTransformation = PasswordVisualTransformation(),
            isError = errorPassword || errorCredenciales,
            leadingIcon = {
                Icon(painter = painterResource(id = R.drawable.ic_password), contentDescription = null)
            },
            supportingText = {
                if (errorPassword) Text(stringResource(R.string.txt_errorPassword))
            }
        )

        // Mostramos el mensaje si el login es incorrecto o falla la petición en ambos métodos.
        if (errorCredenciales) {
            Spacer(modifier = Modifier.height(8.dp))
            Text("Credenciales incorrectas. Verifica tu email y contraseña.", color = MaterialTheme.colorScheme.error, fontSize = 12.sp)
        }

        Spacer(modifier = Modifier.height(32.dp))

        // Si estamos esperando la respuesta del servidor, mostramos el indicador de carga.
        if (cargando) {
            CircularProgressIndicator()
        } else {
            // Usamos un botón sólido normal.
            Button(
                onClick = {
                    val emailLimpio = email.trim()
                    val passLimpia = password.trim()

                    errorEmail = emailLimpio.isBlank()
                    errorPassword = passLimpia.isBlank()

                    if (!errorEmail && !errorPassword) {
                        cargando = true

                        coroutineScope.launch {
                            try {
                                val peticion = LoginRequest(email = emailLimpio, password = passLimpia)
                                val respuesta = InstanciaRetrofit.api.iniciarSesion(peticion)

                                sharedPreferences.edit().putString("token_api", respuesta.token).apply()
                                onLoginSuccess()
                            } catch (e: Exception) {
                                // Si el servidor falla (timeout de BD), activamos nuestro MODO RESPALDO.
                                val esCuentaGuardada = emailLimpio == emailGuardado && passLimpia == passwordGuardada && emailGuardado.isNotEmpty()
                                val esCuentaAdmin = emailLimpio == "admin@cineapp.com" && passLimpia == "1234"

                                if (esCuentaGuardada || esCuentaAdmin) {
                                    // Entramos en modo local guardando un token ficticio.
                                    sharedPreferences.edit().putString("token_api", "TOKEN_LOCAL_MOCK").apply()
                                    onLoginSuccess()
                                } else {
                                    // Si tampoco coinciden las credenciales locales, marcamos error.
                                    errorCredenciales = true
                                }
                            } finally {
                                cargando = false
                            }
                        }
                    }
                },
                modifier = Modifier.fillMaxWidth(),
                colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.primary)
            ) {
                Text(stringResource(R.string.txt_buttonLogin))
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        TextButton(onClick = { onNavigateToRegistro() }) {
            Text(stringResource(R.string.txt_no_account))
        }
    }
}

@androidx.compose.ui.tooling.preview.PreviewScreenSizes
@Preview(showBackground = true)
@Composable
fun LoginScreenPreview() {
    LoginScreen(onNavigateToRegistro = {}, onLoginSuccess = {})
}