package com.example.comida.red

import com.google.firebase.auth.FirebaseAuth
import kotlinx.coroutines.tasks.await

object FirebaseTokenProvider {

    suspend fun obtenerTokenBearer(): String {
        val usuario = FirebaseAuth.getInstance().currentUser
            ?: throw Exception("No hay sesión activa")

        val token = usuario.getIdToken(false).await().token
            ?: throw Exception("No se pudo obtener el token de Firebase")

        return "Bearer $token"
    }
}