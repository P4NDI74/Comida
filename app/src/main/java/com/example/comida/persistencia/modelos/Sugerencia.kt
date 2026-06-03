package com.example.comida.persistencia.modelos

data class ConjuntoSugerencia(
    val alimento: Alimento? = null,
    val bebida: Bebida? = null,
    val costoTotal: Float = 0f
)

data class Sugerencia(
    val conjuntos: List<ConjuntoSugerencia> = emptyList(),
    val alimentos: List<Alimento> = emptyList(),
    val bebidas: List<Bebida> = emptyList(),
    val costoTotal: Float = 0f,
    val presupuestoRestante: Float = 0f
) {
    fun dentroDePrresupuesto(presupuesto: Float) = costoTotal <= presupuesto
}