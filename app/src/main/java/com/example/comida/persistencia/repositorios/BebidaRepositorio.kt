package com.example.comida.persistencia.repositorios

import com.example.comida.persistencia.modelos.Bebida
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.tasks.await

class BebidaRepositorio {
    private val firestore = FirebaseFirestore.getInstance()

    private fun coleccion(userId: String) =
        firestore.collection("usuarios").document(userId).collection("bebidas")

    suspend fun guardar(bebida: Bebida): Result<Unit> = runCatching {
        val ref = if (bebida.id.isEmpty()) coleccion(bebida.userId).document()
        else coleccion(bebida.userId).document(bebida.id)
        ref.set(bebida.copy(id = ref.id)).await()
    }

    suspend fun obtenerTodos(userId: String): List<Bebida> =
        coleccion(userId).get().await().toObjects(Bebida::class.java)

    suspend fun eliminar(userId: String, id: String): Result<Unit> = runCatching {
        coleccion(userId).document(id).delete().await()
    }
}