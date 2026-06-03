package com.example.comida.presentacion.pantallas

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.comida.presentacion.viewmodels.GastoViewModel
import com.example.comida.ui.theme.*
import com.google.firebase.auth.FirebaseAuth
import java.text.SimpleDateFormat
import java.util.*

@Composable
fun PantallaGastos(viewModel: GastoViewModel = viewModel()) {
    val userId = FirebaseAuth.getInstance().currentUser?.uid ?: return
    val gastos by viewModel.gastos.collectAsState()
    val totalGastado by viewModel.totalGastado.collectAsState()
    var mostrarConfirmacion by remember { mutableStateOf(false) }
    val formato = SimpleDateFormat("dd/MM/yy HH:mm", Locale.getDefault())

    LaunchedEffect(Unit) { viewModel.cargarGastos(userId) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(GrisFondo)
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .background(AzulPrimario)
                .padding(20.dp)
        ) {
            Column {
                Text(
                    "Historial de gastos",
                    color = Blanco,
                    fontSize = 22.sp,
                    fontWeight = FontWeight.Bold
                )
                Spacer(Modifier.height(12.dp))
                Card(
                    colors = CardDefaults.cardColors(containerColor = AzulMedio),
                    shape = RoundedCornerShape(12.dp)
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(16.dp),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Column {
                            Text("Total gastado", color = Blanco.copy(alpha = 0.8f), fontSize = 13.sp)
                            Text(
                                "$${"%.2f".format(totalGastado)}",
                                color = Blanco,
                                fontSize = 26.sp,
                                fontWeight = FontWeight.Bold
                            )
                        }
                        Column(horizontalAlignment = Alignment.End) {
                            Text("Registros", color = Blanco.copy(alpha = 0.8f), fontSize = 13.sp)
                            Text(
                                "${gastos.size}",
                                color = Blanco,
                                fontSize = 26.sp,
                                fontWeight = FontWeight.Bold
                            )
                        }
                    }
                }
            }
        }

        if (gastos.isNotEmpty()) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 8.dp),
                horizontalArrangement = Arrangement.End
            ) {
                OutlinedButton(
                    onClick = { mostrarConfirmacion = true },
                    colors = ButtonDefaults.outlinedButtonColors(contentColor = Rojo),
                    border = androidx.compose.foundation.BorderStroke(1.dp, Rojo)
                ) {
                    Text("Borrar todo")
                }
            }

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(AzulClaro)
                    .padding(horizontal = 16.dp, vertical = 8.dp),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text("Descripcion", fontWeight = FontWeight.Bold, color = AzulPrimario, fontSize = 12.sp, modifier = Modifier.weight(2f))
                Text("Lugar", fontWeight = FontWeight.Bold, color = AzulPrimario, fontSize = 12.sp, modifier = Modifier.weight(1.5f))
                Text("Total", fontWeight = FontWeight.Bold, color = AzulPrimario, fontSize = 12.sp, modifier = Modifier.weight(1f))
                Text("Fecha", fontWeight = FontWeight.Bold, color = AzulPrimario, fontSize = 12.sp, modifier = Modifier.weight(1.5f))
            }

            LazyColumn(modifier = Modifier.padding(horizontal = 8.dp)) {
                items(gastos) { gasto ->
                    Card(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 3.dp, horizontal = 4.dp),
                        colors = CardDefaults.cardColors(containerColor = AzulTarjeta),
                        shape = RoundedCornerShape(8.dp)
                    ) {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(horizontal = 12.dp, vertical = 10.dp),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(
                                gasto.descripcion,
                                style = MaterialTheme.typography.bodySmall,
                                color = GrisOscuro,
                                modifier = Modifier.weight(2f),
                                maxLines = 2
                            )
                            Text(
                                gasto.nombreNegocio,
                                style = MaterialTheme.typography.bodySmall,
                                color = GrisMedio,
                                modifier = Modifier.weight(1.5f),
                                maxLines = 1
                            )
                            Text(
                                "$${"%.0f".format(gasto.costo)}",
                                style = MaterialTheme.typography.bodySmall,
                                color = AzulMedio,
                                fontWeight = FontWeight.Bold,
                                modifier = Modifier.weight(1f)
                            )
                            Text(
                                formato.format(Date(gasto.fecha)),
                                style = MaterialTheme.typography.bodySmall,
                                color = GrisMedio,
                                fontSize = 10.sp,
                                modifier = Modifier.weight(1.5f)
                            )
                        }
                    }
                }
            }
        } else {
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    "Aun no tienes gastos registrados.\nRegistra uno desde Sugerencias.",
                    color = GrisMedio,
                    style = MaterialTheme.typography.bodyMedium
                )
            }
        }
    }

    if (mostrarConfirmacion) {
        AlertDialog(
            onDismissRequest = { mostrarConfirmacion = false },
            title = { Text("Borrar historial") },
            text = { Text("Esta accion eliminara todos los gastos registrados. No se puede deshacer.") },
            confirmButton = {
                Button(
                    onClick = {
                        viewModel.eliminarTodos(userId)
                        mostrarConfirmacion = false
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = Rojo)
                ) { Text("Borrar todo") }
            },
            dismissButton = {
                TextButton(onClick = { mostrarConfirmacion = false }) { Text("Cancelar") }
            }
        )
    }
}