package com.ardat.comsubsaverapp.ui.theme

import android.os.Build
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.dynamicDarkColorScheme
import androidx.compose.material3.dynamicLightColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext

private val LightColorScheme = lightColorScheme(
    primary = Indigo,
    onPrimary = SurfaceLight,
    primaryContainer = IndigoLight,
    secondary = Emerald,
    onSecondary = SurfaceLight,
    secondaryContainer = EmeraldLight,
    tertiary = Amber,
    tertiaryContainer = AmberLight,
    error = ErrorRed,
    background = BackgroundLight,
    onBackground = OnSurfaceLight,
    surface = SurfaceLight,
    onSurface = OnSurfaceLight
)

private val DarkColorScheme = darkColorScheme(
    primary = IndigoLight,
    onPrimary = IndigoDark,
    primaryContainer = Indigo,
    secondary = EmeraldLight,
    onSecondary = EmeraldDark,
    secondaryContainer = Emerald,
    tertiary = AmberLight,
    tertiaryContainer = Amber,
    error = ErrorRedDark,
    background = BackgroundDark,
    onBackground = OnSurfaceDark,
    surface = SurfaceDark,
    onSurface = OnSurfaceDark
)

@Composable
fun SubSaverTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    dynamicColor: Boolean = true,
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
        content = content
    )
}
