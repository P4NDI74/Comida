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
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.comida.persistencia.modelos.ConjuntoSugerencia
import com.example.comida.persistencia.modelos.Gasto
import com.example.comida.presentacion.viewmodels.GastoViewModel
import com.example.comida.presentacion.viewmodels.SugerenciaViewModel
import com.example.comida.ui.theme.*
import com.google.firebase.auth.FirebaseAuth

@Composable
fun PantallaSugerencias(
    viewModel: SugerenciaViewModel = viewModel(),
    gastoViewModel: GastoViewModel = viewModel()
) {
    val userId = FirebaseAuth.getInstance().currentUser?.uid ?: return
    val sugerencia by viewModel.sugerencia.collectAsState()
    var presupuesto by remember { mutableStateOf("") }
    var incluirComida by remember { mutableStateOf(true) }
    var incluirBebida by remember { mutableStateOf(true) }
    var conjuntoSeleccionado by remember { mutableStateOf<ConjuntoSugerencia?>(null) }
    var mostrarConfirmacion by remember { mutableStateOf(false) }
    var mostrarExito by remember { mutableStateOf(false) }

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
                Text("Sugerencias de compra", color = Blanco, fontSize = 22.sp, fontWeight = FontWeight.Bold)
                Spacer(Modifier.height(16.dp))

                OutlinedTextField(
                    value = presupuesto,
                    onValueChange = { if (it.length <= 7 && (it.toFloatOrNull() ?: 0f) <= 9999f) presupuesto = it },
                    label = { Text("Mi presupuesto (max \$9,999)", color = Blanco.copy(alpha = 0.8f)) },
                    singleLine = true,
                    modifier = Modifier.fillMaxWidth(),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = Blanco,
                        unfocusedBorderColor = Blanco.copy(alpha = 0.6f),
                        focusedTextColor = Blanco,
                        unfocusedTextColor = Blanco,
                        cursorColor = Blanco
                    )
                )
                Spacer(Modifier.height(12.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceEvenly
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Checkbox(
                            checked = incluirComida,
                            onCheckedChange = { incluirComida = it },
                            colors = CheckboxDefaults.colors(
                                checkedColor = Blanco,
                                checkmarkColor = AzulPrimario
                            )
                        )
                        Text("Comida", color = Blanco, fontWeight = FontWeight.Medium)
                    }
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Checkbox(
                            checked = incluirBebida,
                            onCheckedChange = { incluirBebida = it },
                            colors = CheckboxDefaults.colors(
                                checkedColor = Blanco,
                                checkmarkColor = AzulPrimario
                            )
                        )
                        Text("Bebida", color = Blanco, fontWeight = FontWeight.Medium)
                    }
                }
                Spacer(Modifier.height(12.dp))

                Button(
                    onClick = {
                        val monto = presupuesto.toFloatOrNull() ?: 0f
                        viewModel.generarSugerencias(userId, monto, incluirComida, incluirBebida)
                    },
                    modifier = Modifier.fillMaxWidth().height(48.dp),
                    shape = RoundedCornerShape(12.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = Blanco)
                ) {
                    Text("Ver sugerencias", color = AzulPrimario, fontWeight = FontWeight.Bold, fontSize = 15.sp)
                }
            }
        }

        sugerencia?.let { s ->
            if (s.conjuntos.isEmpty()) {
                Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    Text(
                        "No hay sugerencias disponibles con ese presupuesto.",
                        color = GrisMedio,
                        style = MaterialTheme.typography.bodyMedium
                    )
                }
            } else {
                LazyColumn(
                    modifier = Modifier.padding(16.dp),
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    items(s.conjuntos) { conjunto ->
                        Card(
                            modifier = Modifier.fillMaxWidth(),
                            shape = RoundedCornerShape(16.dp),
                            colors = CardDefaults.cardColors(containerColor = Blanco),
                            elevation = CardDefaults.cardElevation(defaultElevation = 3.dp)
                        ) {
                            Column(modifier = Modifier.padding(16.dp)) {
                                conjunto.alimento?.let {
                                    Row(
                                        modifier = Modifier.fillMaxWidth(),
                                        horizontalArrangement = Arrangement.SpaceBetween
                                    ) {
                                        Column {
                                            Text("Alimento", color = GrisMedio, fontSize = 11.sp)
                                            Text(it.nombre, fontWeight = FontWeight.Bold, color = GrisOscuro)
                                            if (it.lugar.isNotBlank())
                                                Text("Lugar: ${it.lugar}", color = GrisMedio, fontSize = 12.sp)
                                        }
                                        Text(
                                            "$${"%.2f".format(it.precio)}",
                                            color = AzulMedio,
                                            fontWeight = FontWeight.Bold,
                                            fontSize = 16.sp
                                        )
                                    }
                                }
                                if (conjunto.alimento != null && conjunto.bebida != null) {
                                    Divider(modifier = Modifier.padding(vertical = 8.dp), color = GrisClaro)
                                }
                                conjunto.bebida?.let {
                                    Row(
                                        modifier = Modifier.fillMaxWidth(),
                                        horizontalArrangement = Arrangement.SpaceBetween
                                    ) {
                                        Column {
                                            Text("Bebida", color = GrisMedio, fontSize = 11.sp)
                                            Text(it.nombre, fontWeight = FontWeight.Bold, color = GrisOscuro)
                                            if (it.lugar.isNotBlank())
                                                Text("Lugar: ${it.lugar}", color = GrisMedio, fontSize = 12.sp)
                                        }
                                        Text(
                                            "$${"%.2f".format(it.precio)}",
                                            color = AzulMedio,
                                            fontWeight = FontWeight.Bold,
                                            fontSize = 16.sp
                                        )
                                    }
                                }
                                Divider(modifier = Modifier.padding(vertical = 8.dp), color = GrisClaro)
                                Row(
                                    modifier = Modifier.fillMaxWidth(),
                                    horizontalArrangement = Arrangement.SpaceBetween,
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Text("Total", fontWeight = FontWeight.Bold, color = GrisOscuro)
                                    Text(
                                        "$${"%.2f".format(conjunto.costoTotal)}",
                                        color = AzulPrimario,
                                        fontWeight = FontWeight.Bold,
                                        fontSize = 20.sp
                                    )
                                }
                                Spacer(Modifier.height(12.dp))
                                Button(
                                    onClick = {
                                        conjuntoSeleccionado = conjunto
                                        mostrarConfirmacion = true
                                    },
                                    modifier = Modifier.fillMaxWidth().height(44.dp),
                                    shape = RoundedCornerShape(10.dp),
                                    colors = ButtonDefaults.buttonColors(containerColor = AzulPrimario)
                                ) {
                                    Text("Registrar como gasto", fontWeight = FontWeight.Bold)
                                }
                            }
                        }
                    }
                }
            }
        }
    }

    if (mostrarConfirmacion && conjuntoSeleccionado != null) {
        val conjunto = conjuntoSeleccionado!!
        val descripcion = listOfNotNull(conjunto.alimento?.nombre, conjunto.bebida?.nombre).joinToString(" + ")
        val lugar = conjunto.alimento?.lugar ?: conjunto.bebida?.lugar ?: ""

        AlertDialog(
            onDismissRequest = { mostrarConfirmacion = false },
            title = { Text("Confirmar gasto", color = AzulPrimario, fontWeight = FontWeight.Bold) },
            text = { Text("Deseas registrar '$descripcion' por $${"%.2f".format(conjunto.costoTotal)} como gasto?") },
            confirmButton = {
                Button(
                    onClick = {
                        gastoViewModel.guardarGasto(
                            Gasto(
                                userId = userId,
                                nombreNegocio = lugar.ifBlank { "Sin especificar" },
                                descripcion = descripcion,
                                costo = conjunto.costoTotal
                            )
                        )
                        mostrarConfirmacion = false
                        mostrarExito = true
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = AzulPrimario)
                ) { Text("Confirmar") }
            },
            dismissButton = {
                TextButton(onClick = { mostrarConfirmacion = false }) {
                    Text("Cancelar", color = GrisMedio)
                }
            }
        )
    }

    if (mostrarExito) {
        AlertDialog(
            onDismissRequest = { mostrarExito = false },
            title = { Text("Gasto registrado", color = Verde, fontWeight = FontWeight.Bold) },
            text = { Text("El gasto fue registrado correctamente en tu historial.") },
            confirmButton = {
                Button(
                    onClick = { mostrarExito = false },
                    colors = ButtonDefaults.buttonColors(containerColor = AzulPrimario)
                ) { Text("Aceptar") }
            }
        )
    }
}