package com.example.comida.ui.theme

import android.os.Build
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable

private val LightColorScheme = lightColorScheme(
    primary = AzulPrimario,
    onPrimary = Blanco,
    primaryContainer = AzulContenedor,
    onPrimaryContainer = AzulPrimario,
    secondary = GrisOscuro,
    onSecondary = Blanco,
    secondaryContainer = GrisClaro,
    onSecondaryContainer = GrisOscuro,
    background = GrisFondo,
    onBackground = GrisOscuro,
    surface = Blanco,
    onSurface = GrisOscuro,
    surfaceVariant = AzulClaro,
    onSurfaceVariant = GrisOscuro,
    error = Rojo,
    onError = Blanco
)

@Composable
fun ComidaTheme(content: @Composable () -> Unit) {
    MaterialTheme(
        colorScheme = LightColorScheme,
        typography = Typography,
        content = content
    )
}