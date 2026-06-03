package com.example.comida.persistencia.modelos

data class Usuario(
    val id: String = "",
    val nombre: String = "",
    val email: String = "",
    val fechaRegistro: Long = System.currentTimeMillis()
)