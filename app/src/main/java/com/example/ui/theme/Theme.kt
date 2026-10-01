package com.example.ui.theme

import android.os.Build
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.dynamicDarkColorScheme
import androidx.compose.material3.dynamicLightColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext

private val DarkColorScheme = darkColorScheme(
    primary = GoldLight,
    onPrimary = DeepBrown,
    primaryContainer = BronzeBrown,
    onPrimaryContainer = GoldLight,
    secondary = GoldPrimary,
    onSecondary = Color.Black,
    background = DarkBackground,
    onBackground = Color(0xFFF3EBDD),
    surface = DarkSurface,
    onSurface = Color(0xFFF3EBDD),
    surfaceVariant = DarkSurfaceVariant,
    onSurfaceVariant = TextMutedDark,
    outline = DarkBorder,
    error = CoralAccent
)

private val LightColorScheme = lightColorScheme(
    primary = GoldPrimary,
    onPrimary = Color.White,
    primaryContainer = WarmSurfaceVariant,
    onPrimaryContainer = DeepBrown,
    secondary = BronzeBrown,
    onSecondary = Color.White,
    secondaryContainer = Color(0xFFE8DCC9),
    onSecondaryContainer = DeepBrown,
    background = CreamBackground,
    onBackground = DeepBrown,
    surface = WarmSurface,
    onSurface = DeepBrown,
    surfaceVariant = WarmSurfaceVariant,
    onSurfaceVariant = TextMutedLight,
    outline = WarmBorder,
    error = CoralAccent
)

@Composable
fun DoraFashionsTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    dynamicColor: Boolean = false, // Preserve brand jewelry colors
    content: @Composable () -> Unit,
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
        content = content
    )
}
