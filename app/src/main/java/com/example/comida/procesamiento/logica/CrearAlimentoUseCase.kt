package com.example.comida.procesamiento.logica

import com.example.comida.persistencia.modelos.Alimento
import com.example.comida.persistencia.repositorios.AlimentoRepositorio

class CrearAlimentoUseCase {
    private val repositorio = AlimentoRepositorio()

    suspend operator fun invoke(alimento: Alimento): Result<Unit> {
        if (alimento.nombre.isBlank())
            return Result.failure(Exception("El nombre no puede estar vacío"))
        if (alimento.precio <= 0f)
            return Result.failure(Exception("El precio debe ser mayor a 0"))
        if (alimento.precio > 9999f)
            return Result.failure(Exception("El precio no puede superar $9,999 pesos"))
        return repositorio.guardar(alimento)
    }
}
