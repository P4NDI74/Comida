package com.example.comida.presentacion.pantallas

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.example.comida.navegacion.Rutas
import com.example.comida.persistencia.modelos.EstadoUi
import com.example.comida.presentacion.viewmodels.LoginViewModel
import com.example.comida.ui.theme.*

@Composable
fun PantallaLogin(
    navController: NavController,
    viewModel: LoginViewModel = viewModel()
) {
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    val estado = viewModel.estado

    LaunchedEffect(estado) {
        if (estado is EstadoUi.Exito) {
            navController.navigate(Rutas.INICIO) {
                popUpTo(Rutas.LOGIN) { inclusive = true }
            }
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(GrisFondo)
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(260.dp)
                .background(AzulPrimario),
            contentAlignment = Alignment.Center
        ) {
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Text(
                    "Comida",
                    color = Blanco,
                    fontSize = 42.sp,
                    fontWeight = FontWeight.Bold
                )
                Spacer(Modifier.height(8.dp))
                Text(
                    "Gestiona tu presupuesto de comida",
                    color = Blanco.copy(alpha = 0.8f),
                    fontSize = 14.sp
                )
            }
        }

        Card(
            modifier = Modifier
                .fillMaxWidth()
                .padding(24.dp),
            shape = RoundedCornerShape(16.dp),
            colors = CardDefaults.cardColors(containerColor = Blanco),
            elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
        ) {
            Column(modifier = Modifier.padding(24.dp)) {
                Text(
                    "Iniciar sesion",
                    fontWeight = FontWeight.Bold,
                    fontSize = 20.sp,
                    color = AzulPrimario
                )
                Spacer(Modifier.height(20.dp))

                OutlinedTextField(
                    value = email,
                    onValueChange = { if (it.length <= 60) email = it },
                    label = { Text("Correo electronico") },
                    singleLine = true,
                    modifier = Modifier.fillMaxWidth(),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = AzulPrimario,
                        focusedLabelColor = AzulPrimario
                    ),
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email)
                )
                Spacer(Modifier.height(16.dp))

                OutlinedTextField(
                    value = password,
                    onValueChange = { if (it.length <= 20) password = it },
                    label = { Text("Contrasena") },
                    singleLine = true,
                    visualTransformation = PasswordVisualTransformation(),
                    modifier = Modifier.fillMaxWidth(),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = AzulPrimario,
                        focusedLabelColor = AzulPrimario
                    )
                )
                Spacer(Modifier.height(8.dp))

                if (estado is EstadoUi.Error) {
                    Text(estado.mensaje, color = Rojo, fontSize = 13.sp)
                    Spacer(Modifier.height(8.dp))
                }

                Spacer(Modifier.height(8.dp))
                Button(
                    onClick = { viewModel.login(email, password) },
                    modifier = Modifier.fillMaxWidth().height(50.dp),
                    shape = RoundedCornerShape(12.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = AzulPrimario),
                    enabled = estado !is EstadoUi.Cargando
                ) {
                    if (estado is EstadoUi.Cargando)
                        CircularProgressIndicator(Modifier.size(24.dp), color = Blanco)
                    else
                        Text("Iniciar Sesion", fontWeight = FontWeight.Bold, fontSize = 16.sp)
                }
                Spacer(Modifier.height(12.dp))
                TextButton(
                    onClick = { navController.navigate(Rutas.REGISTRO) },
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text("No tienes cuenta? Registrate", color = AzulMedio)
                }
            }
        }
    }
}