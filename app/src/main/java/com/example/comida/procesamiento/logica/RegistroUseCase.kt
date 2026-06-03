package com.example.comida.procesamiento.logica

import android.util.Patterns
import com.example.comida.persistencia.repositorios.UsuarioRepositorio

class RegistroUseCase {
    private val repositorio = UsuarioRepositorio()

    suspend operator fun invoke(nombre: String, email: String, password: String): Result<Unit> {
        if (nombre.isBlank())
            return Result.failure(Exception("El nombre no puede estar vacío"))
        if (nombre.length < 3)
            return Result.failure(Exception("El nombre debe tener al menos 3 caracteres"))
        if (!nombre.all { it.isLetter() || it.isWhitespace() })
            return Result.failure(Exception("El nombre solo puede contener letras"))
        if (email.isBlank() || !Patterns.EMAIL_ADDRESS.matcher(email).matches())
            return Result.failure(Exception("Correo inválido"))
        if (password.length < 6)
            return Result.failure(Exception("La contraseña debe tener al menos 6 caracteres"))
        return repositorio.registrar(nombre, email, password)
    }
}