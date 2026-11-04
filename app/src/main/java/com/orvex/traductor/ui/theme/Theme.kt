package com.orvex.traductor.ui.theme

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

private val TranslatorColors = darkColorScheme(
    primary = Color(0xFF20C8E8),
    secondary = Color(0xFF8EDDEC),
    background = Color(0xFF101827),
    surface = Color(0xFF1C2B3E),
    onPrimary = Color(0xFF06131C),
    onBackground = Color.White,
    onSurface = Color.White
)

@Composable
fun TraductorTheme(content: @Composable () -> Unit) {
    MaterialTheme(
        colorScheme = TranslatorColors,
        content = content
    )
}
