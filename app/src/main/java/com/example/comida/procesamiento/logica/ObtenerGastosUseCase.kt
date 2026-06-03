package com.example.comida.procesamiento.logica

import com.example.comida.persistencia.modelos.Gasto
import com.example.comida.persistencia.repositorios.GastoRepositorio

class ObtenerGastosUseCase {
    private val repositorio = GastoRepositorio()

    suspend operator fun invoke(userId: String): List<Gasto> =
        repositorio.obtenerTodos(userId)
}