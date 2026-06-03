package com.example.comida.presentacion.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.comida.persistencia.modelos.Alimento
import com.example.comida.persistencia.modelos.EstadoUi
import com.example.comida.procesamiento.logica.CrearAlimentoUseCase
import com.example.comida.persistencia.repositorios.AlimentoRepositorio
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class AlimentoViewModel : ViewModel() {
    private val crearUseCase = CrearAlimentoUseCase()
    private val repositorio = AlimentoRepositorio()

    private val _alimentos = MutableStateFlow<List<Alimento>>(emptyList())
    val alimentos: StateFlow<List<Alimento>> = _alimentos

    private val _estado = MutableStateFlow<EstadoUi>(EstadoUi.Inicial)
    val estado: StateFlow<EstadoUi> = _estado

    private val _mensajeDialogo = MutableStateFlow<String?>(null)
    val mensajeDialogo: StateFlow<String?> = _mensajeDialogo

    private val _esExito = MutableStateFlow(false)
    val esExito: StateFlow<Boolean> = _esExito

    fun cargarAlimentos(userId: String) {
        viewModelScope.launch {
            _alimentos.value = repositorio.obtenerTodos(userId)
        }
    }

    fun guardarAlimento(alimento: Alimento) {
        viewModelScope.launch {
            _estado.value = EstadoUi.Cargando
            val resultado = crearUseCase(alimento)
            resultado.fold(
                onSuccess = {
                    _estado.value = EstadoUi.Exito
                    _esExito.value = true
                    _mensajeDialogo.value = "Alimento agregado correctamente"
                    cargarAlimentos(alimento.userId)
                },
                onFailure = {
                    _estado.value = EstadoUi.Error(it.message ?: "Error")
                    _esExito.value = false
                    _mensajeDialogo.value = it.message ?: "Ocurrio un error al guardar"
                }
            )
        }
    }

    fun eliminarAlimento(userId: String, id: String) {
        viewModelScope.launch {
            repositorio.eliminar(userId, id)
            cargarAlimentos(userId)
        }
    }

    fun limpiarMensaje() {
        _mensajeDialogo.value = null
        _estado.value = EstadoUi.Inicial
    }
}