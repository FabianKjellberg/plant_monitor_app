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
    primary = Color(0xFF9FAC81),
    onPrimary = Color(0xFF2D3029),

    secondary = Color(0xFFBEDBD7),
    onSecondary = Color(0xFF2D3029),

    tertiary = Color(0xFFD9B368),
    onTertiary = Color(0xFF3E3624),

    background = Color(0xFF1B1C18),
    onBackground = Color(0xFFFFFBF5),

    surface = Color(0xFF2D3029),
    onSurface = Color(0xFFFFFBF5),

    surfaceVariant = Color(0xFF44483D),
    onSurfaceVariant = Color(0xFFFFFBF5),

    outline = Color(0xFF879675)
)

private val LightColorScheme = lightColorScheme(
    primary = Color(0xFF879675),
    onPrimary = Color(0xFFE8DFCE),

    secondary = Color(0xFFd9b368),
    onSecondary = Color.Black,

    tertiary = Color(0xFF2E7D32),
    onTertiary = Color(0xFFE8DFCE),

    background = Color(0xFFFFFBF5),
    onBackground = Color(0xFF5C5F51),

    surface = Color(0xFFE8DFCE),
    onSurface = Color(0xFF5C5F51),

    surfaceVariant = Color(0xFFe2e6d1),
    onSurfaceVariant = Color(0xFF5C5F51),

    outline = Color(0xFF7EA849)
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