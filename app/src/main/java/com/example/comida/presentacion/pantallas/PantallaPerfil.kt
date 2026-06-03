package com.example.comida.presentacion.pantallas

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.comida.navegacion.Rutas
import com.example.comida.ui.theme.*
import com.google.firebase.auth.FirebaseAuth

@Composable
fun PantallaPerfil(navController: NavController) {
    val auth = FirebaseAuth.getInstance()
    val usuario = auth.currentUser

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(GrisFondo)
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(200.dp)
                .background(AzulPrimario),
            contentAlignment = Alignment.Center
        ) {
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Box(
                    modifier = Modifier
                        .size(80.dp)
                        .clip(CircleShape)
                        .background(Blanco),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        Icons.Default.Person,
                        contentDescription = null,
                        tint = AzulPrimario,
                        modifier = Modifier.size(50.dp)
                    )
                }
                Spacer(Modifier.height(12.dp))
                Text("Mi perfil", color = Blanco, fontSize = 20.sp, fontWeight = FontWeight.Bold)
            }
        }

        Spacer(Modifier.height(24.dp))

        Card(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 24.dp),
            shape = RoundedCornerShape(16.dp),
            colors = CardDefaults.cardColors(containerColor = Blanco),
            elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
        ) {
            Column(modifier = Modifier.padding(24.dp)) {
                Text("Informacion de cuenta", fontWeight = FontWeight.Bold, color = AzulPrimario, fontSize = 16.sp)
                Spacer(Modifier.height(16.dp))

                Text("Correo electronico", color = GrisMedio, fontSize = 12.sp)
                Text(usuario?.email ?: "Sin correo", color = GrisOscuro, fontSize = 15.sp, fontWeight = FontWeight.Medium)
                Spacer(Modifier.height(12.dp))
                Divider(color = GrisClaro)
                Spacer(Modifier.height(12.dp))

                Text("Estado del correo", color = GrisMedio, fontSize = 12.sp)
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Box(
                        modifier = Modifier
                            .size(10.dp)
                            .clip(CircleShape)
                            .background(if (usuario?.isEmailVerified == true) Verde else Rojo)
                    )
                    Spacer(Modifier.width(8.dp))
                    Text(
                        if (usuario?.isEmailVerified == true) "Correo verificado" else "Correo no verificado",
                        color = if (usuario?.isEmailVerified == true) Verde else Rojo,
                        fontSize = 15.sp,
                        fontWeight = FontWeight.Medium
                    )
                }
            }
        }

        Spacer(Modifier.height(24.dp))

        Button(
            onClick = {
                auth.signOut()
                navController.navigate(Rutas.LOGIN) {
                    popUpTo(0) { inclusive = true }
                }
            },
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 24.dp)
                .height(50.dp),
            shape = RoundedCornerShape(12.dp),
            colors = ButtonDefaults.buttonColors(containerColor = Rojo)
        ) {
            Text("Cerrar sesion", fontWeight = FontWeight.Bold, fontSize = 16.sp)
        }
    }
}