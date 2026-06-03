package com.example.comida.red

import com.example.comida.persistencia.modelos.Alimento
import com.example.comida.persistencia.modelos.Bebida
import com.example.comida.persistencia.modelos.Gasto
import com.example.comida.persistencia.modelos.Sugerencia
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.POST
import retrofit2.http.Path

data class SugerenciaRequest(
    val presupuesto: Float,
    val incluirComida: Boolean,
    val incluirBebida: Boolean
)

data class RegistrarPerfilRequest(
    val nombre: String
)

interface ComidaApi {

    @GET("api/auth/me")
    suspend fun obtenerUsuario(
        @Header("Authorization") token: String
    ): Response<Unit>

    @POST("api/auth/registrar-perfil")
    suspend fun registrarPerfil(
        @Header("Authorization") token: String,
        @Body request: RegistrarPerfilRequest
    ): Response<Unit>

    @GET("api/alimentos")
    suspend fun obtenerAlimentos(
        @Header("Authorization") token: String
    ): List<Alimento>

    @POST("api/alimentos")
    suspend fun guardarAlimento(
        @Header("Authorization") token: String,
        @Body alimento: Alimento
    ): Alimento

    @DELETE("api/alimentos/{id}")
    suspend fun eliminarAlimento(
        @Header("Authorization") token: String,
        @Path("id") id: String
    ): Response<Unit>

    @GET("api/bebidas")
    suspend fun obtenerBebidas(
        @Header("Authorization") token: String
    ): List<Bebida>

    @POST("api/bebidas")
    suspend fun guardarBebida(
        @Header("Authorization") token: String,
        @Body bebida: Bebida
    ): Bebida

    @DELETE("api/bebidas/{id}")
    suspend fun eliminarBebida(
        @Header("Authorization") token: String,
        @Path("id") id: String
    ): Response<Unit>

    @GET("api/gastos")
    suspend fun obtenerGastos(
        @Header("Authorization") token: String
    ): List<Gasto>

    @POST("api/gastos")
    suspend fun guardarGasto(
        @Header("Authorization") token: String,
        @Body gasto: Gasto
    ): Gasto

    @DELETE("api/gastos/{id}")
    suspend fun eliminarGasto(
        @Header("Authorization") token: String,
        @Path("id") id: String
    ): Response<Unit>

    @DELETE("api/gastos")
    suspend fun eliminarTodosGastos(
        @Header("Authorization") token: String
    ): Response<Unit>

    @POST("api/sugerencias")
    suspend fun generarSugerencias(
        @Header("Authorization") token: String,
        @Body request: SugerenciaRequest
    ): Sugerencia
}