package com.jesuspainceiras.gestionpeliculas.screens

import androidx.compose.foundation.layout.*
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
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
fun RegistroScreen(
    onNavigateToLogin: () -> Unit
) {
    // Definimos nuestras variables de estado para capturar los textos.
    var nombre by remember { mutableStateOf("") }
    var email by remember { mutableStateOf("") }
    var password1 by remember { mutableStateOf("") }
    var password2 by remember { mutableStateOf("") }

    var errorVacio by remember { mutableStateOf(false) }
    var errorContrasenas by remember { mutableStateOf(false) }

    // Añadimos el estado de carga y la corrutina para comunicarnos con la API.
    var cargando by remember { mutableStateOf(false) }
    val snackbarHostState = remember { SnackbarHostState() }
    val coroutineScope = rememberCoroutineScope()

    Scaffold(
        snackbarHost = { SnackbarHost(snackbarHostState) }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Text(
                text = stringResource(R.string.txt_createAccount),
                fontSize = 32.sp,
                color = MaterialTheme.colorScheme.primary
            )

            Spacer(modifier = Modifier.height(32.dp))

            // Internacionalizamos las etiquetas de los inputs.
            CineInput(
                value = nombre,
                onValueChange = { nombre = it; errorVacio = false },
                label = stringResource(R.string.txt_name),
                isError = errorVacio
            )

            Spacer(modifier = Modifier.height(16.dp))

            CineInput(
                value = email,
                onValueChange = { email = it; errorVacio = false },
                label = stringResource(R.string.txt_email),
                isError = errorVacio
            )

            Spacer(modifier = Modifier.height(16.dp))

            CineInput(
                value = password1,
                onValueChange = { password1 = it; errorVacio = false; errorContrasenas = false },
                label = stringResource(R.string.txt_write_password),
                visualTransformation = PasswordVisualTransformation(),
                isError = errorVacio || errorContrasenas
            )

            Spacer(modifier = Modifier.height(16.dp))

            CineInput(
                value = password2,
                onValueChange = { password2 = it; errorVacio = false; errorContrasenas = false },
                label = stringResource(R.string.txt_repeatPassword),
                visualTransformation = PasswordVisualTransformation(),
                isError = errorVacio || errorContrasenas
            )

            Spacer(modifier = Modifier.height(16.dp))

            // Mostramos los errores de validación local si los hay.
            if (errorVacio) {
                Text(stringResource(R.string.txt_errorEmptyTextField), color = MaterialTheme.colorScheme.error, fontSize = 12.sp)
            } else if (errorContrasenas) {
                Text(stringResource(R.string.txt_errorPasswordMatch), color = MaterialTheme.colorScheme.error, fontSize = 12.sp)
            }

            Spacer(modifier = Modifier.height(32.dp))

            // Controlamos si debemos mostrar el indicador de carga o el botón.
            if (cargando) {
                CircularProgressIndicator()
            } else {
                Button(
                    onClick = {
                        // Limpiamos los textos de espacios en blanco.
                        val nombreLimpio = nombre.trim()
                        val emailLimpio = email.trim()
                        val pass1Limpia = password1.trim()
                        val pass2Limpia = password2.trim()

                        // Validamos que todo esté correcto antes de enviar.
                        errorVacio = nombreLimpio.isBlank() || emailLimpio.isBlank() || pass1Limpia.isBlank() || pass2Limpia.isBlank()
                        errorContrasenas = pass1Limpia != pass2Limpia

                        if (!errorVacio && !errorContrasenas) {
                            // Iniciamos la carga
                            cargando = true

                            // Lanzamos la petición a la red de forma asíncrona.
                            coroutineScope.launch {
                                try {
                                    // Preparamos los datos según la estructura que espera la API.
                                    val peticion = LoginRequest(email = emailLimpio, password = pass1Limpia)

                                    // Ejecutamos el registro contra el servidor.
                                    InstanciaRetrofit.api.registrarUsuario(peticion)

                                    // Si llegamos aquí, el registro fue un éxito. Volvemos al Login.
                                    onNavigateToLogin()

                                } catch (e: Exception) {
                                    // Si falla (ej: usuario ya existe o servidor caído), avisamos al usuario.
                                    snackbarHostState.showSnackbar("Error al registrar: Puede que el usuario ya exista o la red falle.")
                                } finally {
                                    // Finalizamos la animación de carga.
                                    cargando = false
                                }
                            }
                        }
                    },
                    modifier = Modifier.fillMaxWidth(),
                    colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.primary)
                ) {
                    Text(stringResource(R.string.txt_buttonRegister))
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            TextButton(onClick = { onNavigateToLogin() }) {
                Text(stringResource(R.string.txt_already_account))
            }
        }
    }
}

// Mantenemos la vista previa con las anotaciones correspondientes.
@androidx.compose.ui.tooling.preview.PreviewScreenSizes
@Preview(showBackground = true)
@Composable
fun RegistroScreenPreview() {
    RegistroScreen(onNavigateToLogin = {})
}