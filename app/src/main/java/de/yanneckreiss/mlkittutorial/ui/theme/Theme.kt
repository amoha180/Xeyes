package de.yanneckreiss.mlkittutorial.ui.theme

import android.app.Activity
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Shapes
import androidx.compose.material.Typography
import androidx.compose.material.darkColors
import androidx.compose.material.lightColors
import androidx.compose.runtime.Composable
import androidx.compose.runtime.SideEffect
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.LocalView
import androidx.core.view.WindowCompat

private val LightColorPalette = lightColors(
    primary         = Teal,
    primaryVariant  = TealDark,
    secondary       = Cream,
    secondaryVariant= CreamDark,
    background      = WhiteSmoke,
    surface         = Color.White,
    onPrimary       = Color.White,
    onSecondary     = Brown,
    onBackground    = Color.Black,
    onSurface       = Color.Black
)

private val DarkColorPalette = darkColors(
    primary         = Teal,
    primaryVariant  = TealDark,
    secondary       = CreamDark,
    background      = Color.Black,
    surface         = Color.Black,
    onPrimary       = Color.White,
    onSecondary     = Brown,
    onBackground    = Color.White,
    onSurface       = Color.White
)

@Composable
fun MLKitTutorialTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    dynamicColor: Boolean = true,
    content: @Composable () -> Unit
) {
    // For Android 12+ you could use dynamicLightColorScheme / dynamicDarkColorScheme
    val colors = if (darkTheme) DarkColorPalette else LightColorPalette

    // Sync status bar color
    val view = LocalView.current
    if (!view.isInEditMode) {
        SideEffect {
            val window = (view.context as Activity).window
            window.statusBarColor = colors.primary.toArgb()
            WindowCompat.getInsetsController(window, view)
                .isAppearanceLightStatusBars = !darkTheme
        }
    }

    MaterialTheme(
        colors    = colors,
        typography= Typography(),  // keep your existing Typography.kt
        shapes    = Shapes(),      // keep your existing Shapes.kt
        content   = content
    )
}
