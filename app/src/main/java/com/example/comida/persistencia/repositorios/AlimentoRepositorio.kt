package com.example.comida.persistencia.repositorios

import com.example.comida.persistencia.modelos.Alimento
import com.example.comida.red.ApiClient
import com.example.comida.red.FirebaseTokenProvider

class AlimentoRepositorio {

    suspend fun guardar(alimento: Alimento): Result<Unit> = runCatching {
        val token = FirebaseTokenProvider.obtenerTokenBearer()
        ApiClient.api.guardarAlimento(token, alimento)
        Unit
    }

    suspend fun obtenerTodos(userId: String): List<Alimento> =
        runCatching {
            val token = FirebaseTokenProvider.obtenerTokenBearer()
            ApiClient.api.obtenerAlimentos(token)
        }.getOrDefault(emptyList())

    suspend fun eliminar(userId: String, id: String): Result<Unit> = runCatching {
        val token = FirebaseTokenProvider.obtenerTokenBearer()
        val respuesta = ApiClient.api.eliminarAlimento(token, id)

        if (!respuesta.isSuccessful) {
            throw Exception("No se pudo eliminar el alimento")
        }

        Unit
    }
}