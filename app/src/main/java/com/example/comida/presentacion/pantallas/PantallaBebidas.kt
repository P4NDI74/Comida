package com.example.comida.presentacion.pantallas

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.comida.persistencia.modelos.Bebida
import com.example.comida.presentacion.viewmodels.BebidaViewModel
import com.example.comida.ui.theme.*
import com.google.firebase.auth.FirebaseAuth

@Composable
fun PantallaBebidas(viewModel: BebidaViewModel = viewModel()) {
    val userId = FirebaseAuth.getInstance().currentUser?.uid ?: return
    val bebidas by viewModel.bebidas.collectAsState()
    val mensajeDialogo by viewModel.mensajeDialogo.collectAsState()
    val esExito by viewModel.esExito.collectAsState()
    var bebidaSeleccionada by remember { mutableStateOf<Bebida?>(null) }
    var mostrarDialogo by remember { mutableStateOf(false) }

    LaunchedEffect(Unit) { viewModel.cargarBebidas(userId) }

    Box(modifier = Modifier.fillMaxSize().background(GrisFondo)) {
        Column {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(AzulPrimario)
                    .padding(20.dp)
            ) {
                Column {
                    Text("Bebidas", color = Blanco, fontSize = 22.sp, fontWeight = FontWeight.Bold)
                    Text("${bebidas.size} registradas", color = Blanco.copy(alpha = 0.8f), fontSize = 13.sp)
                }
            }

            LazyColumn(
                modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                items(bebidas) { bebida ->
                    Card(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clickable {
                                bebidaSeleccionada = bebida
                                mostrarDialogo = true
                            },
                        shape = RoundedCornerShape(12.dp),
                        colors = CardDefaults.cardColors(containerColor = AzulTarjeta),
                        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
                    ) {
                        Row(
                            modifier = Modifier.padding(16.dp).fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Column(modifier = Modifier.weight(1f)) {
                                Text(bebida.nombre, fontWeight = FontWeight.Bold, color = GrisOscuro, fontSize = 16.sp)
                                Spacer(Modifier.height(4.dp))
                                Text(
                                    "$${"%.2f".format(bebida.precio)}",
                                    color = AzulMedio,
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 15.sp
                                )
                                if (bebida.lugar.isNotBlank()) {
                                    Spacer(Modifier.height(2.dp))
                                    Text("Lugar: ${bebida.lugar}", color = GrisMedio, fontSize = 12.sp)
                                }
                            }
                            IconButton(onClick = { viewModel.eliminarBebida(userId, bebida.id) }) {
                                Icon(Icons.Default.Delete, contentDescription = "Eliminar", tint = Rojo)
                            }
                        }
                    }
                }
            }
        }

        FloatingActionButton(
            onClick = {
                bebidaSeleccionada = null
                mostrarDialogo = true
            },
            modifier = Modifier
                .align(Alignment.BottomEnd)
                .padding(24.dp),
            containerColor = AzulPrimario
        ) {
            Icon(Icons.Default.Add, "Agregar", tint = Blanco)
        }
    }

    if (mostrarDialogo) {
        DialogoBebida(
            bebidaInicial = bebidaSeleccionada,
            onDismiss = { mostrarDialogo = false; bebidaSeleccionada = null },
            onGuardar = { nombre, precio, lugar ->
                val bebida = bebidaSeleccionada?.copy(nombre = nombre, precio = precio, lugar = lugar)
                    ?: Bebida(userId = userId, nombre = nombre, precio = precio, lugar = lugar)
                viewModel.guardarBebida(bebida)
                mostrarDialogo = false
                bebidaSeleccionada = null
            }
        )
    }

    mensajeDialogo?.let { mensaje ->
        AlertDialog(
            onDismissRequest = { viewModel.limpiarMensaje() },
            title = { Text(if (esExito) "Exito" else "Error", color = if (esExito) Verde else Rojo) },
            text = { Text(mensaje) },
            confirmButton = {
                Button(
                    onClick = { viewModel.limpiarMensaje() },
                    colors = ButtonDefaults.buttonColors(containerColor = AzulPrimario)
                ) { Text("Aceptar") }
            }
        )
    }
}

@Composable
fun DialogoBebida(
    bebidaInicial: Bebida? = null,
    onDismiss: () -> Unit,
    onGuardar: (String, Float, String) -> Unit
) {
    var nombre by remember { mutableStateOf(bebidaInicial?.nombre ?: "") }
    var precio by remember { mutableStateOf(bebidaInicial?.precio?.toString() ?: "") }
    var lugar by remember { mutableStateOf(bebidaInicial?.lugar ?: "") }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = {
            Text(
                if (bebidaInicial != null) "Editar bebida" else "Nueva bebida",
                color = AzulPrimario,
                fontWeight = FontWeight.Bold
            )
        },
        text = {
            Column {
                OutlinedTextField(
                    value = nombre,
                    onValueChange = { if (it.length <= 40) nombre = it },
                    label = { Text("Nombre") },
                    singleLine = true,
                    modifier = Modifier.fillMaxWidth(),
                    colors = OutlinedTextFieldDefaults.colors(focusedBorderColor = AzulPrimario, focusedLabelColor = AzulPrimario)
                )
                Spacer(Modifier.height(8.dp))
                OutlinedTextField(
                    value = precio,
                    onValueChange = { if (it.length <= 6 && (it.toFloatOrNull() ?: 0f) <= 9999f) precio = it },
                    label = { Text("Precio (max \$9,999)") },
                    singleLine = true,
                    modifier = Modifier.fillMaxWidth(),
                    colors = OutlinedTextFieldDefaults.colors(focusedBorderColor = AzulPrimario, focusedLabelColor = AzulPrimario)
                )
                Spacer(Modifier.height(8.dp))
                OutlinedTextField(
                    value = lugar,
                    onValueChange = { if (it.length <= 50) lugar = it },
                    label = { Text("Lugar de compra") },
                    singleLine = true,
                    modifier = Modifier.fillMaxWidth(),
                    colors = OutlinedTextFieldDefaults.colors(focusedBorderColor = AzulPrimario, focusedLabelColor = AzulPrimario)
                )
            }
        },
        confirmButton = {
            Button(
                onClick = { onGuardar(nombre, precio.toFloatOrNull() ?: 0f, lugar) },
                colors = ButtonDefaults.buttonColors(containerColor = AzulPrimario)
            ) { Text("Guardar") }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) { Text("Cancelar", color = GrisMedio) }
        }
    )
}