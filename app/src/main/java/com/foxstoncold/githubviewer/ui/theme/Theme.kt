package com.foxstoncold.githubviewer.ui.theme

import android.app.Activity
import android.os.Build
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.dynamicDarkColorScheme
import androidx.compose.material3.dynamicLightColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.SideEffect
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalView
import androidx.core.view.WindowCompat

@Composable
fun GitHubTheme(darkTheme: Boolean = isSystemInDarkTheme(), content: @Composable () -> Unit) {
    val lightColors = lightColorScheme(
        primary = Color(0xFF252525),
        secondary = Color(0xFFB5B6BD),
        background = Color(0xFFF4F5F9),
        surface = Color.White,
        tertiary = Color(0xFFFECC53),
        onTertiary = Color(0xFFF4D281),
        onPrimary = Color.White,
        onSecondary = Color(0xFFDFDFE1),
        onBackground = Color.Black,
        onSurface = Color(0xFFF3F4F8),
        surfaceContainer = Color(0xFFEEEEEE)
    )

    val darkColors = darkColorScheme(
        primary = Color(0xFF252525),
        secondary = Color(0xFF8899A6),
        background = Color(0xFF1E1E1E),
        surface = Color(0xFF2B2C2E),
        tertiary = Color(0xFFFECC53),
        onTertiary = Color(0xFFF4D281),
        onPrimary = Color.White,
        onSecondary = Color(0xFFDFDFE1),
        onBackground = Color.White,
        onSurface = Color(0xFF414244),
        surfaceContainer = Color(0xFF2B2C2E)
    )

    val colorScheme = if (darkTheme) darkColors else lightColors

    MaterialTheme(
        colorScheme = colorScheme,
        content = content
    )
}