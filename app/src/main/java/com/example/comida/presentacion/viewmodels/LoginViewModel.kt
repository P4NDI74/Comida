package com.example.comida.presentacion.viewmodels

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.comida.persistencia.modelos.EstadoUi
import com.example.comida.procesamiento.logica.LoginUseCase
import kotlinx.coroutines.launch

class LoginViewModel : ViewModel() {
    private val loginUseCase = LoginUseCase()

    var estado by mutableStateOf<EstadoUi>(EstadoUi.Inicial)
        private set

    fun login(email: String, password: String) {
        viewModelScope.launch {
            estado = EstadoUi.Cargando
            val resultado = loginUseCase(email, password)
            estado = resultado.fold(
                onSuccess = { EstadoUi.Exito },
                onFailure = { EstadoUi.Error(it.message ?: "Error desconocido") }
            )
        }
    }

    fun resetEstado() { estado = EstadoUi.Inicial }
}