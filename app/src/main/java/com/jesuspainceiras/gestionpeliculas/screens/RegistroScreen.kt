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

        CineInput(
            value = nombre,
            onValueChange = { nombre = it; errorVacio = false },
            label = "Nombre completo",
            isError = errorVacio
        )

        Spacer(modifier = Modifier.height(16.dp))

        CineInput(
            value = email,
            onValueChange = { email = it; errorVacio = false },
            label = "Correo electrónico",
            isError = errorVacio
        )

        Spacer(modifier = Modifier.height(16.dp))

        CineInput(
            value = password1,
            onValueChange = { password1 = it; errorVacio = false; errorContrasenas = false },
            label = "Escriba la contraseña",
            visualTransformation = PasswordVisualTransformation(),
            isError = errorVacio || errorContrasenas
        )

        Spacer(modifier = Modifier.height(16.dp))

        CineInput(
            value = password2,
            onValueChange = { password2 = it; errorVacio = false; errorContrasenas = false },
            label = "Repita la contraseña",
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
                errorVacio = false
                errorContrasenas = false

                if (nombre.isEmpty() || email.isEmpty() || password1.isEmpty() || password2.isEmpty()) {
                    errorVacio = true
                } else if (password1 != password2) {
                    errorContrasenas = true
                } else {
                    // GUARDAR EL EMAIL EN SHAREDPREFERENCES.
                    sharedPreferences.edit().putString("email_registrado", email).apply()

                    onNavigateToLogin()
                }
            },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text(stringResource(R.string.txt_buttonRegister))
        }

        Spacer(modifier = Modifier.height(16.dp))

        TextButton(onClick = { onNavigateToLogin() }) {
            Text("¿Ya tienes cuenta? Inicia sesión")
        }
    }
}

@Preview(showBackground = true)
@Composable
fun RegistroScreenPreview() {
    RegistroScreen(onNavigateToLogin = {})
}