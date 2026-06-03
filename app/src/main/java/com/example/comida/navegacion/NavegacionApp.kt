package com.example.comida.navegacion

import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.comida.presentacion.pantallas.PantallaInicio
import com.example.comida.presentacion.pantallas.PantallaLogin
import com.example.comida.presentacion.pantallas.PantallaRegistro
import com.google.firebase.auth.FirebaseAuth

@Composable
fun NavegacionApp() {
    val navController = rememberNavController()
    val auth = FirebaseAuth.getInstance()
    val inicio = if (auth.currentUser != null && auth.currentUser!!.isEmailVerified)
        Rutas.INICIO else Rutas.LOGIN

    NavHost(navController = navController, startDestination = inicio) {
        composable(Rutas.LOGIN) { PantallaLogin(navController) }
        composable(Rutas.REGISTRO) { PantallaRegistro(navController) }
        composable(Rutas.INICIO) { PantallaInicio(navController) }
    }
}