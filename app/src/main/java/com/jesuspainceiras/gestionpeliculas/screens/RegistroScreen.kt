package com.jesuspainceiras.gestionpeliculas.screens

import android.content.Context
import androidx.compose.foundation.layout.*
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
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.jesuspainceiras.gestionpeliculas.R
import com.jesuspainceiras.gestionpeliculas.components.CineInput
import androidx.core.content.edit

@Composable
fun RegistroScreen(
    onNavigateToLogin: () -> Unit
) {
    // Obtenemos el contexto para poder usar SharedPreferences.
    val context = LocalContext.current
    val sharedPreferences = context.getSharedPreferences("MisPreferencias", Context.MODE_PRIVATE)

    var nombre by remember { mutableStateOf("") }
    var email by remember { mutableStateOf("") }
    var password1 by remember { mutableStateOf("") }
    var password2 by remember { mutableStateOf("") }

    var errorVacio by remember { mutableStateOf(false) }
    var errorContrasenas by remember { mutableStateOf(false) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text(
            text = stringResource(R.string.txt_createAccount),
            fontSize = 32.sp,
            color = MaterialTheme.colorScheme.primary // Usando el tema del sistema.
        )

        Spacer(modifier = Modifier.height(32.dp))

        // Internacionalizamos las etiquetas de los inputs para la mejora objetada por el profesor.
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

        if (errorVacio) {
            Text(stringResource(R.string.txt_errorEmptyTextField), color = MaterialTheme.colorScheme.error, fontSize = 12.sp)
        } else if (errorContrasenas) {
            Text(stringResource(R.string.txt_errorPasswordMatch), color = MaterialTheme.colorScheme.error, fontSize = 12.sp)
        }

        Spacer(modifier = Modifier.height(32.dp))

        Button(
            onClick = {
                // Limpiamos los textos de espacios en blanco al principio y al final.
                val nombreLimpio = nombre.trim()
                val emailLimpio = email.trim()
                val pass1Limpia = password1.trim()
                val pass2Limpia = password2.trim()

                // Modificamos las validaciones usando isBlank sobre las variables limpias para evitar registros vacíos o con espacios.
                errorVacio = nombreLimpio.isBlank() || emailLimpio.isBlank() || pass1Limpia.isBlank() || pass2Limpia.isBlank()
                errorContrasenas = pass1Limpia != pass2Limpia

                if (!errorVacio && !errorContrasenas) {
                    // GUARDAR EL EMAIL EN SHAREDPREFERENCES. Guardamos los datos limpios de espacios.
                    sharedPreferences.edit {
                        putString("email_registrado", emailLimpio)
                        // Añadimos también el guardado de la contraseña para la validación v1.1.
                        putString("password_registrada", pass1Limpia)
                    }

                    onNavigateToLogin()
                }
            },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text(stringResource(R.string.txt_buttonRegister))
        }

        Spacer(modifier = Modifier.height(16.dp))

        TextButton(onClick = { onNavigateToLogin() }) {
            Text(stringResource(R.string.txt_already_account))
        }
    }
}

// Añadimos la anotación PreviewScreenSizes para la mejora objetada por el profesor.
@androidx.compose.ui.tooling.preview.PreviewScreenSizes
@Preview(showBackground = true)
@Composable
fun RegistroScreenPreview() {
    RegistroScreen(onNavigateToLogin = {})
}