package com.example.comida.procesamiento.logica

import com.example.comida.persistencia.modelos.Gasto
import com.example.comida.persistencia.repositorios.GastoRepositorio

class CrearGastoUseCase {
    private val repositorio = GastoRepositorio()

    suspend operator fun invoke(gasto: Gasto): Result<Unit> {
        if (gasto.nombreNegocio.isBlank())
            return Result.failure(Exception("El nombre del negocio no puede estar vacío"))
        if (gasto.costo <= 0)
            return Result.failure(Exception("El costo debe ser mayor a 0"))
        return repositorio.guardar(gasto)
    }
}