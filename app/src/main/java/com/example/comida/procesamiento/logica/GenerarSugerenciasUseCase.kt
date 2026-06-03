package com.example.comida.procesamiento.logica

import com.example.comida.persistencia.modelos.Sugerencia
import com.example.comida.red.ApiClient
import com.example.comida.red.FirebaseTokenProvider
import com.example.comida.red.SugerenciaRequest

class GenerarSugerenciasUseCase {

    suspend operator fun invoke(
        userId: String,
        presupuesto: Float,
        incluirComida: Boolean,
        incluirBebida: Boolean
    ): Sugerencia {
        val token = FirebaseTokenProvider.obtenerTokenBearer()

        return ApiClient.api.generarSugerencias(
            token = token,
            request = SugerenciaRequest(
                presupuesto = presupuesto,
                incluirComida = incluirComida,
                incluirBebida = incluirBebida
            )
        )
    }
}