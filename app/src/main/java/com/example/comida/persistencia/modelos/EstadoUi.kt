package com.example.comida.persistencia.modelos

sealed class EstadoUi {
    object Inicial : EstadoUi()
    object Cargando : EstadoUi()
    object Exito : EstadoUi()
    data class Error(val mensaje: String) : EstadoUi()
}