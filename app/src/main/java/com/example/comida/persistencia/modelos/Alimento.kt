package com.example.comida.persistencia.modelos

data class Alimento(
    val id: String = "",
    val userId: String = "",
    val nombre: String = "",
    val precio: Float = 0f,
    val lugar: String = ""
) {
    fun estaEnPresupuesto(presupuesto: Float) = precio <= presupuesto
}