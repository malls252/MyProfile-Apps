package com.example.myprofile.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

private val DarkColorScheme = darkColorScheme(
    primary = Color(0xFF1A237E),
    secondary = Color(0xFF0D47A1),
    tertiary = Color(0xFF42A5F5)
)

private val LightColorScheme = lightColorScheme(
    primary = Color(0xFF1A237E),
    secondary = Color(0xFF0D47A1),
    tertiary = Color(0xFF42A5F5)
)

@Composable
fun MyProfileTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    content: @Composable () -> Unit
) {
    val colorScheme = if (darkTheme) DarkColorScheme else LightColorScheme

    MaterialTheme(
        colorScheme = colorScheme,
        typography = Typography(),
        content = content
    )
}