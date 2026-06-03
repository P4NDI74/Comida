package com.example.comida.presentacion.pantallas

import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.navigation.NavController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.example.comida.ui.theme.Blanco

data class ItemMenu(val ruta: String, val etiqueta: String, val icono: ImageVector)

@Composable
fun PantallaInicio(navController: NavController) {
    val navControllerInterno = rememberNavController()
    val items = listOf(
        ItemMenu("perfil", "Perfil", Icons.Default.Person),
        ItemMenu("alimentos", "Alimentos", Icons.Default.Restaurant),
        ItemMenu("bebidas", "Bebidas", Icons.Default.LocalDrink),
        ItemMenu("gastos", "Gastos", Icons.Default.Receipt),
        ItemMenu("sugerencias", "Sugerencias", Icons.Default.Lightbulb)
    )

    Scaffold(bottomBar = {
        NavigationBar(containerColor = MaterialTheme.colorScheme.primary) {
            val rutaActual by navControllerInterno.currentBackStackEntryAsState()
            items.forEach { item ->
                NavigationBarItem(
                    selected = rutaActual?.destination?.route == item.ruta,
                    onClick = {
                        navControllerInterno.navigate(item.ruta) {
                            launchSingleTop = true
                        }
                    },
                    icon = { Icon(item.icono, item.etiqueta) },
                    label = { Text(item.etiqueta) },
                    colors = NavigationBarItemDefaults.colors(
                        selectedIconColor = MaterialTheme.colorScheme.primary,
                        selectedTextColor = Blanco,
                        unselectedIconColor = Blanco.copy(alpha = 0.7f),
                        unselectedTextColor = Blanco.copy(alpha = 0.7f),
                        indicatorColor = Blanco
                    )
                )
            }
        }
    }) { padding ->
        NavHost(
            navControllerInterno,
            startDestination = "sugerencias",
            modifier = Modifier.padding(padding)
        ) {
            composable("perfil") { PantallaPerfil(navController) }
            composable("alimentos") { PantallaAlimentos() }
            composable("bebidas") { PantallaBebidas() }
            composable("gastos") { PantallaGastos() }
            composable("sugerencias") { PantallaSugerencias() }
        }
    }
}