package com.example.comida.presentacion.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.comida.persistencia.modelos.Sugerencia
import com.example.comida.procesamiento.logica.GenerarSugerenciasUseCase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class SugerenciaViewModel : ViewModel() {
    private val generarUseCase = GenerarSugerenciasUseCase()

    private val _sugerencia = MutableStateFlow<Sugerencia?>(null)
    val sugerencia: StateFlow<Sugerencia?> = _sugerencia

    fun generarSugerencias(
        userId: String,
        presupuesto: Float,
        incluirComida: Boolean,
        incluirBebida: Boolean
    ) {
        viewModelScope.launch {
            _sugerencia.value = generarUseCase(userId, presupuesto, incluirComida, incluirBebida)
        }
    }

    fun limpiar() { _sugerencia.value = null }
}