package com.example.comida.presentacion.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.comida.persistencia.modelos.Gasto
import com.example.comida.persistencia.repositorios.GastoRepositorio
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class GastoViewModel : ViewModel() {
    private val repositorio = GastoRepositorio()

    private val _gastos = MutableStateFlow<List<Gasto>>(emptyList())
    val gastos: StateFlow<List<Gasto>> = _gastos

    private val _totalGastado = MutableStateFlow(0f)
    val totalGastado: StateFlow<Float> = _totalGastado

    fun cargarGastos(userId: String) {
        viewModelScope.launch {
            try {
                val lista = repositorio.obtenerTodos(userId)
                _gastos.value = lista.sortedByDescending { it.fecha }
                _totalGastado.value = lista.sumOf { it.costo.toDouble() }.toFloat()
            } catch (e: Exception) {
                _gastos.value = emptyList()
            }
        }
    }

    fun guardarGasto(gasto: Gasto) {
        viewModelScope.launch {
            repositorio.guardar(gasto)
            cargarGastos(gasto.userId)
        }
    }

    fun eliminarTodos(userId: String) {
        viewModelScope.launch {
            repositorio.eliminarTodos(userId)
            cargarGastos(userId)
        }
    }
}