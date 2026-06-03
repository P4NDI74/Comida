package com.example.comida.procesamiento.logica

import android.util.Patterns
import com.example.comida.persistencia.repositorios.UsuarioRepositorio

class LoginUseCase {
    private val repositorio = UsuarioRepositorio()

    suspend operator fun invoke(email: String, password: String): Result<Unit> {
        if (email.isBlank() || !Patterns.EMAIL_ADDRESS.matcher(email).matches())
            return Result.failure(Exception("Correo inválido"))
        if (password.length < 6)
            return Result.failure(Exception("La contraseña debe tener al menos 6 caracteres"))
        val resultado = repositorio.login(email, password)
        if (resultado.isSuccess && !repositorio.correoVerificado())
            return Result.failure(Exception("Debes verificar tu correo antes de ingresar"))
        return resultado.map { }
    }
}