package com.ayoo.consumer.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

// ===== COLOR DEFINITIONS =====
private val Pink500 = Color(0xFFFD0EC9)
private val Pink200 = Color(0xFFFF8CDB)
private val White = Color(0xFFFFFFFF)
private val Black = Color(0xFF000000)

// ===== LIGHT & DARK COLOR SCHEMES =====
private val LightColors = lightColorScheme(
    primary = Pink500,
    onPrimary = White,
    secondary = Pink200,
    onSecondary = Black,
    background = Color(0xFFFFF9FA),
    surface = White,
    onBackground = Black,
    onSurface = Black
)

private val DarkColors = darkColorScheme(
    primary = Pink500,
    onPrimary = White,
    secondary = Pink200,
    onSecondary = Black,
    background = Color(0xFF1C1B1F),
    surface = Color(0xFF1C1B1F),
    onBackground = White,
    onSurface = White
)

// ===== MAIN THEME WRAPPER =====
@Composable
fun AyooTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    content: @Composable () -> Unit
) {
    val colors = if (darkTheme) DarkColors else LightColors

    MaterialTheme(
        colorScheme = colors,
        typography = Typography(),
        shapes = Shapes(),
        content = content
    )
}
