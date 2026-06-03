package com.example.comida.presentacion.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.comida.persistencia.modelos.Bebida
import com.example.comida.persistencia.modelos.EstadoUi
import com.example.comida.procesamiento.logica.CrearBebidaUseCase
import com.example.comida.persistencia.repositorios.BebidaRepositorio
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class BebidaViewModel : ViewModel() {
    private val crearUseCase = CrearBebidaUseCase()
    private val repositorio = BebidaRepositorio()

    private val _bebidas = MutableStateFlow<List<Bebida>>(emptyList())
    val bebidas: StateFlow<List<Bebida>> = _bebidas

    private val _estado = MutableStateFlow<EstadoUi>(EstadoUi.Inicial)
    val estado: StateFlow<EstadoUi> = _estado

    private val _mensajeDialogo = MutableStateFlow<String?>(null)
    val mensajeDialogo: StateFlow<String?> = _mensajeDialogo

    private val _esExito = MutableStateFlow(false)
    val esExito: StateFlow<Boolean> = _esExito

    fun cargarBebidas(userId: String) {
        viewModelScope.launch {
            _bebidas.value = repositorio.obtenerTodos(userId)
        }
    }

    fun guardarBebida(bebida: Bebida) {
        viewModelScope.launch {
            _estado.value = EstadoUi.Cargando
            val resultado = crearUseCase(bebida)
            resultado.fold(
                onSuccess = {
                    _estado.value = EstadoUi.Exito
                    _esExito.value = true
                    _mensajeDialogo.value = "Bebida agregada correctamente"
                    cargarBebidas(bebida.userId)
                },
                onFailure = {
                    _estado.value = EstadoUi.Error(it.message ?: "Error")
                    _esExito.value = false
                    _mensajeDialogo.value = it.message ?: "Ocurrio un error al guardar"
                }
            )
        }
    }

    fun eliminarBebida(userId: String, id: String) {
        viewModelScope.launch {
            repositorio.eliminar(userId, id)
            cargarBebidas(userId)
        }
    }

    fun limpiarMensaje() {
        _mensajeDialogo.value = null
        _estado.value = EstadoUi.Inicial
    }
}