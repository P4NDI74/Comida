package com.example.comida.procesamiento.logica

import com.example.comida.persistencia.modelos.Bebida
import com.example.comida.persistencia.repositorios.BebidaRepositorio

class CrearBebidaUseCase {
    private val repositorio = BebidaRepositorio()

    suspend operator fun invoke(bebida: Bebida): Result<Unit> {
        if (bebida.nombre.isBlank())
            return Result.failure(Exception("El nombre no puede estar vacío"))
        if (bebida.precio <= 0f)
            return Result.failure(Exception("El precio debe ser mayor a 0"))
        if (bebida.precio > 9999f)
            return Result.failure(Exception("El precio no puede superar $9,999 pesos"))
        return repositorio.guardar(bebida)
    }
}