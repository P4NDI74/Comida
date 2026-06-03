package com.example.comida.persistencia.modelos

data class Gasto(
    val id: String = "",
    val userId: String = "",
    val nombreNegocio: String = "",
    val descripcion: String = "",
    val costo: Float = 0f,
    val fecha: Long = System.currentTimeMillis()
)