package com.example.comida.persistencia.repositorios

import com.example.comida.persistencia.modelos.Gasto
import com.example.comida.red.ApiClient
import com.example.comida.red.FirebaseTokenProvider

class GastoRepositorio {

    suspend fun guardar(gasto: Gasto): Result<Unit> = runCatching {
        val token = FirebaseTokenProvider.obtenerTokenBearer()
        ApiClient.api.guardarGasto(token, gasto)
        Unit
    }

    suspend fun obtenerTodos(userId: String): List<Gasto> =
        runCatching {
            val token = FirebaseTokenProvider.obtenerTokenBearer()
            ApiClient.api.obtenerGastos(token)
        }.getOrDefault(emptyList())

    suspend fun eliminar(userId: String, id: String): Result<Unit> = runCatching {
        val token = FirebaseTokenProvider.obtenerTokenBearer()
        val respuesta = ApiClient.api.eliminarGasto(token, id)

        if (!respuesta.isSuccessful) {
            throw Exception("No se pudo eliminar el gasto")
        }

        Unit
    }

    suspend fun eliminarTodos(userId: String): Result<Unit> = runCatching {
        val token = FirebaseTokenProvider.obtenerTokenBearer()
        val respuesta = ApiClient.api.eliminarTodosGastos(token)

        if (!respuesta.isSuccessful) {
            throw Exception("No se pudieron eliminar los gastos")
        }

        Unit
    }
}