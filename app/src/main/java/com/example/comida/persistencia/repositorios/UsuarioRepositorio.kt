package com.example.comida.persistencia.repositorios

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.firestore.FirebaseFirestore
import com.example.comida.persistencia.modelos.Usuario
import kotlinx.coroutines.tasks.await

class UsuarioRepositorio {
    private val auth = FirebaseAuth.getInstance()
    private val firestore = FirebaseFirestore.getInstance()

    suspend fun registrar(nombre: String, email: String, password: String): Result<Unit> = runCatching {
        val resultado = auth.createUserWithEmailAndPassword(email, password).await()
        resultado.user?.sendEmailVerification()?.await()
        val usuario = Usuario(id = resultado.user!!.uid, nombre = nombre, email = email)
        firestore.collection("usuarios").document(usuario.id).set(usuario).await()
    }

    suspend fun login(email: String, password: String): Result<FirebaseUser> = runCatching {
        val resultado = auth.signInWithEmailAndPassword(email, password).await()
        resultado.user ?: throw Exception("Usuario no encontrado")
    }

    fun correoVerificado() = auth.currentUser?.isEmailVerified ?: false
    fun usuarioActual() = auth.currentUser
    fun cerrarSesion() = auth.signOut()

    suspend fun reenviarVerificacion(): Result<Unit> = runCatching {
        auth.currentUser?.sendEmailVerification()?.await()
            ?: throw Exception("No hay sesión activa")
    }
}