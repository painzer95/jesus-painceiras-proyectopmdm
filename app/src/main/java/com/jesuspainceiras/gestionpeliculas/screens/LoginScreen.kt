package com.jesuspainceiras.gestionpeliculas.screens

import android.content.Context
import androidx.compose.foundation.layout.*
import androidx.compose.material3.Icon
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
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

@Composable
fun LoginScreen(
    onNavigateToRegistro: () -> Unit,
    onLoginSuccess: () -> Unit
) {
    val context = LocalContext.current
    val sharedPreferences = context.getSharedPreferences("MisPreferencias", Context.MODE_PRIVATE)

    // Leemos el email guardado y también la contraseña para la validación v1.1. Si no hay nada, devolvemos un texto vacío.
    val emailGuardado = sharedPreferences.getString("email_registrado", "") ?: ""
    val passwordGuardada = sharedPreferences.getString("password_registrada", "") ?: ""

    // Inicializamos el estado del email con el valor guardado.
    var email by remember { mutableStateOf(emailGuardado) }
    var password by remember { mutableStateOf("") }

    var errorEmail by remember { mutableStateOf(false) }
    var errorPassword by remember { mutableStateOf(false) }
    // Añadimos un estado para mostrar el error si las credenciales fallan.
    var errorCredenciales by remember { mutableStateOf(false) }

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

        // Internacionalizamos el título de la app para la mejora objetada por el profesor.
        Text(
            text = stringResource(R.string.app_name_display),
            fontSize = 32.sp,
            color = MaterialTheme.colorScheme.primary
        )

        Spacer(modifier = Modifier.height(32.dp))

        CineInput(
            value = email,
            // Limpiamos también el error de credenciales si el usuario empieza a escribir de nuevo.
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

        // Mostramos el mensaje si el login es incorrecto.
        if (errorCredenciales) {
            Spacer(modifier = Modifier.height(8.dp))
            Text("Credenciales incorrectas. Verifica tu email y contraseña.", color = MaterialTheme.colorScheme.error, fontSize = 12.sp)
        }

        Spacer(modifier = Modifier.height(32.dp))

        Button(
            onClick = {
                // Modificamos las validaciones usando isBlank y simplificamos la lógica para la mejora objetada por el profesor.
                errorEmail = email.isBlank()
                errorPassword = password.isBlank()

                if (!errorEmail && !errorPassword) {
                    // Validamos contra la cuenta guardada en memoria o la cuenta de administrador que hemos creado por defecto.
                    val esCuentaGuardada = email == emailGuardado && password == passwordGuardada && emailGuardado.isNotEmpty()
                    val esCuentaAdmin = email == "admin@cineapp.com" && password == "1234"

                    if (esCuentaGuardada || esCuentaAdmin) {
                        onLoginSuccess()
                    } else {
                        errorCredenciales = true
                    }
                }
            },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text(stringResource(R.string.txt_buttonLogin))
        }

        Spacer(modifier = Modifier.height(16.dp))

        TextButton(onClick = { onNavigateToRegistro() }) {
            Text(stringResource(R.string.txt_no_account))
        }
    }
}

// Añadimos la anotación PreviewScreenSizes para probar diferentes pantallas para la mejora objetada por el profesor.
@androidx.compose.ui.tooling.preview.PreviewScreenSizes
@Preview(showBackground = true)
@Composable
fun LoginScreenPreview() {
    LoginScreen(onNavigateToRegistro = {}, onLoginSuccess = {})
}