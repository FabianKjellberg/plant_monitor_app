package com.example.learning_android.ui.theme

import android.app.Activity
import android.os.Build
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.dynamicDarkColorScheme
import androidx.compose.material3.dynamicLightColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext

private val DarkColorScheme = darkColorScheme(
    primary = Color(0xFF66BB6A),
    onPrimary = Color.Black,

    secondary = Color(0xFF81C784),
    onSecondary = Color.Black,

    tertiary = Color(0xFF2E7D32),
    onTertiary = Color.White,

    background = Color(0xFF0F1A12),
    onBackground = Color(0xFFE8F5E9),

    surface = Color(0xFF1B2A20),
    onSurface = Color(0xFFE8F5E9),

    surfaceVariant = Color(0xFF243528),
    onSurfaceVariant = Color(0xFFC8E6C9),

    outline = Color(0xFF388E3C)
)

private val LightColorScheme = lightColorScheme(
    primary = Color(0xFF4CAF50),
    onPrimary = Color.White,

    secondary = Color(0xFF81C784),
    onSecondary = Color.Black,

    tertiary = Color(0xFF2E7D32),
    onTertiary = Color.White,

    background = Color(0xFFF3F6F3),
    onBackground = Color(0xFF1B5E20),

    surface = Color.White,
    onSurface = Color(0xFF1B5E20),

    surfaceVariant = Color(0xFFD1E7D2),
    onSurfaceVariant = Color(0xFF2E7D32),

    outline = Color(0xFFA5D6A7)
)

@Composable
fun Learning_androidTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    dynamicColor: Boolean = false,
    content: @Composable () -> Unit
) {
    val colorScheme = when {
        dynamicColor && Build.VERSION.SDK_INT >= Build.VERSION_CODES.S -> {
            val context = LocalContext.current
            if (darkTheme) dynamicDarkColorScheme(context) else dynamicLightColorScheme(context)
        }

        darkTheme -> DarkColorScheme
        else -> LightColorScheme
    }

    MaterialTheme(
        colorScheme = colorScheme,
        typography = Typography,
    ){
        Surface(
            color = MaterialTheme.colorScheme.background
        ) {
            content();
        }
    }
}