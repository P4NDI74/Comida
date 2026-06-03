package com.example.comida.presentacion.viewmodels

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.comida.persistencia.modelos.EstadoUi
import com.example.comida.procesamiento.logica.RegistroUseCase
import kotlinx.coroutines.launch

class RegistroViewModel : ViewModel() {
    private val registroUseCase = RegistroUseCase()

    var estado by mutableStateOf<EstadoUi>(EstadoUi.Inicial)
        private set

    fun registrar(nombre: String, email: String, password: String) {
        viewModelScope.launch {
            estado = EstadoUi.Cargando
            val resultado = registroUseCase(nombre, email, password)
            estado = resultado.fold(
                onSuccess = { EstadoUi.Exito },
                onFailure = { EstadoUi.Error(it.message ?: "Error desconocido") }
            )
        }
    }

    fun resetEstado() { estado = EstadoUi.Inicial }
}