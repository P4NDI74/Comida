package com.example.comida.presentacion.pantallas

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.example.comida.persistencia.modelos.EstadoUi
import com.example.comida.presentacion.viewmodels.RegistroViewModel
import com.example.comida.ui.theme.*

@Composable
fun PantallaRegistro(
    navController: NavController,
    viewModel: RegistroViewModel = viewModel()
) {
    var nombre by remember { mutableStateOf("") }
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var confirmarPassword by remember { mutableStateOf("") }
    var verPassword by remember { mutableStateOf(false) }
    var verConfirmarPassword by remember { mutableStateOf(false) }
    var mensajeExito by remember { mutableStateOf(false) }
    var errorLocal by remember { mutableStateOf<String?>(null) }
    val estado = viewModel.estado

    LaunchedEffect(estado) {
        if (estado is EstadoUi.Exito) mensajeExito = true
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(GrisFondo)
            .verticalScroll(rememberScrollState())
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(160.dp)
                .background(AzulPrimario),
            contentAlignment = Alignment.Center
        ) {
            Text(
                "Crear cuenta",
                color = Blanco,
                fontSize = 30.sp,
                fontWeight = FontWeight.Bold
            )
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

                OutlinedTextField(
                    value = nombre,
                    onValueChange = { if (it.length <= 40) nombre = it },
                    label = { Text("Nombre completo") },
                    singleLine = true,
                    modifier = Modifier.fillMaxWidth(),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = AzulPrimario,
                        focusedLabelColor = AzulPrimario
                    )
                )
                Spacer(Modifier.height(12.dp))

                OutlinedTextField(
                    value = email,
                    onValueChange = { if (it.length <= 60) email = it },
                    label = { Text("Correo electronico") },
                    singleLine = true,
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email),
                    modifier = Modifier.fillMaxWidth(),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = AzulPrimario,
                        focusedLabelColor = AzulPrimario
                    )
                )
                Spacer(Modifier.height(12.dp))

                OutlinedTextField(
                    value = password,
                    onValueChange = { if (it.length <= 20) password = it },
                    label = { Text("Contrasena (min 6, max 20)") },
                    singleLine = true,
                    visualTransformation = if (verPassword) VisualTransformation.None else PasswordVisualTransformation(),
                    trailingIcon = {
                        IconButton(onClick = { verPassword = !verPassword }) {
                            Icon(
                                if (verPassword) Icons.Default.VisibilityOff else Icons.Default.Visibility,
                                contentDescription = null,
                                tint = AzulMedio
                            )
                        }
                    },
                    modifier = Modifier.fillMaxWidth(),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = AzulPrimario,
                        focusedLabelColor = AzulPrimario
                    )
                )
                Spacer(Modifier.height(12.dp))

                OutlinedTextField(
                    value = confirmarPassword,
                    onValueChange = { if (it.length <= 20) confirmarPassword = it },
                    label = { Text("Confirmar contrasena") },
                    singleLine = true,
                    visualTransformation = if (verConfirmarPassword) VisualTransformation.None else PasswordVisualTransformation(),
                    trailingIcon = {
                        IconButton(onClick = { verConfirmarPassword = !verConfirmarPassword }) {
                            Icon(
                                if (verConfirmarPassword) Icons.Default.VisibilityOff else Icons.Default.Visibility,
                                contentDescription = null,
                                tint = AzulMedio
                            )
                        }
                    },
                    modifier = Modifier.fillMaxWidth(),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = AzulPrimario,
                        focusedLabelColor = AzulPrimario
                    )
                )
                Spacer(Modifier.height(8.dp))

                errorLocal?.let {
                    Text(it, color = Rojo, fontSize = 13.sp)
                    Spacer(Modifier.height(4.dp))
                }
                if (estado is EstadoUi.Error) {
                    Text(estado.mensaje, color = Rojo, fontSize = 13.sp)
                    Spacer(Modifier.height(4.dp))
                }

                Spacer(Modifier.height(8.dp))

                if (mensajeExito) {
                    Card(
                        colors = CardDefaults.cardColors(containerColor = VerdeClaro),
                        shape = RoundedCornerShape(8.dp)
                    ) {
                        Text(
                            "Cuenta creada. Revisa tu correo para verificar tu cuenta.",
                            color = Verde,
                            modifier = Modifier.padding(12.dp),
                            fontSize = 14.sp
                        )
                    }
                    Spacer(Modifier.height(12.dp))
                    Button(
                        onClick = { navController.popBackStack() },
                        modifier = Modifier.fillMaxWidth().height(50.dp),
                        shape = RoundedCornerShape(12.dp),
                        colors = ButtonDefaults.buttonColors(containerColor = AzulPrimario)
                    ) { Text("Ir al Login", fontWeight = FontWeight.Bold) }
                } else {
                    Button(
                        onClick = {
                            errorLocal = null
                            when {
                                password.length < 6 ->
                                    errorLocal = "La contrasena debe tener al menos 6 caracteres"
                                password != confirmarPassword ->
                                    errorLocal = "Las contrasenas no coinciden"
                                else -> viewModel.registrar(nombre, email, password)
                            }
                        },
                        modifier = Modifier.fillMaxWidth().height(50.dp),
                        shape = RoundedCornerShape(12.dp),
                        colors = ButtonDefaults.buttonColors(containerColor = AzulPrimario),
                        enabled = estado !is EstadoUi.Cargando
                    ) {
                        if (estado is EstadoUi.Cargando)
                            CircularProgressIndicator(Modifier.size(24.dp), color = Blanco)
                        else
                            Text("Registrarse", fontWeight = FontWeight.Bold, fontSize = 16.sp)
                    }
                    Spacer(Modifier.height(8.dp))
                    TextButton(
                        onClick = { navController.popBackStack() },
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Text("Ya tienes cuenta? Inicia sesion", color = AzulMedio)
                    }
                }
            }
        }
    }
}