package com.example.myapplication.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

private val DarkColorScheme = darkColorScheme(
    primary = SulpakRedLight,
    onPrimary = Color(0xFF690007),
    primaryContainer = Color(0xFF93000C),
    onPrimaryContainer = Color(0xFFFFDAD6),
    background = Color(0xFF151517),
    surface = Color(0xFF1D1D20),
    surfaceVariant = Color(0xFF303034),
    onSurface = Color(0xFFF1F0F3),
    onSurfaceVariant = Color(0xFFCAC6CA)
)

private val LightColorScheme = lightColorScheme(
    primary = SulpakRed,
    onPrimary = Color.White,
    primaryContainer = Color(0xFFFFE8E9),
    onPrimaryContainer = Color(0xFF970C12),
    secondaryContainer = Color(0xFFFFE8E9),
    onSecondaryContainer = SulpakRed,
    background = ShopBackground,
    surface = Color.White,
    surfaceVariant = Color(0xFFF0EFF2),
    onSurface = ShopInk,
    onSurfaceVariant = Color(0xFF626269),
    outlineVariant = Color(0xFFE5E3E6)
)

@Composable
fun MyApplicationTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    content: @Composable () -> Unit
) {
    MaterialTheme(
        colorScheme = if (darkTheme) DarkColorScheme else LightColorScheme,
        typography = Typography,
        content = content
    )
}
