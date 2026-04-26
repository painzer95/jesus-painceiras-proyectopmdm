package com.jesuspainceiras.gestionpeliculas.screens

import android.content.Context
import androidx.compose.foundation.layout.*
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
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

@Composable
fun LoginScreen(
    onNavigateToRegistro: () -> Unit,
    onLoginSuccess: () -> Unit
) {
    val context = LocalContext.current
    val sharedPreferences = context.getSharedPreferences("MisPreferencias", Context.MODE_PRIVATE)

    // Leemos el email guardado. Si no hay nada, devolvemos un texto vacío.
    val emailGuardado = sharedPreferences.getString("email_registrado", "") ?: ""

    // Inicializamos el estado del email con el valor guardado.
    var email by remember { mutableStateOf(emailGuardado) }
    var password by remember { mutableStateOf("") }

    var errorEmail by remember { mutableStateOf(false) }
    var errorPassword by remember { mutableStateOf(false) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(32.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text(
            text = stringResource(R.string.txt_Welcome),
            fontSize = 32.sp,
            color = MaterialTheme.colorScheme.primary
        )

        Spacer(modifier = Modifier.height(32.dp))

        OutlinedTextField(
            value = email,
            onValueChange = {
                email = it
                errorEmail = false
            },
            placeholder = { Text(stringResource(R.string.txt_email)) },
            modifier = Modifier.fillMaxWidth(),
            singleLine = true,
            isError = errorEmail,
            supportingText = {
                if (errorEmail) {
                    Text(stringResource(R.string.txt_errorEmail))
                }
            }
        )

        Spacer(modifier = Modifier.height(16.dp))

        OutlinedTextField(
            value = password,
            onValueChange = {
                password = it
                errorPassword = false
            },
            label = { Text(stringResource(R.string.txt_password)) },
            visualTransformation = PasswordVisualTransformation(),
            modifier = Modifier.fillMaxWidth(),
            singleLine = true,
            isError = errorPassword,
            supportingText = {
                if (errorPassword) {
                    Text(stringResource(R.string.txt_errorPassword))
                }
            }
        )

        Spacer(modifier = Modifier.height(32.dp))

        Button(
            onClick = {
                var hayError = false
                if (email.isEmpty()){
                    errorEmail = true
                    hayError = true
                }
                if (password.isEmpty()){
                    errorPassword = true
                    hayError = true
                }

                if (!hayError) {
                    onLoginSuccess()
                }
            },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text(stringResource(R.string.txt_buttonLogin))
        }

        Spacer(modifier = Modifier.height(16.dp))

        TextButton(onClick = { onNavigateToRegistro() }) {
            Text("¿No tienes cuenta? Regístrate aquí")
        }
    }
}

@Preview(showBackground = true)
@Composable
fun LoginScreenPreview() {
    LoginScreen(onNavigateToRegistro = {}, onLoginSuccess = {})
}