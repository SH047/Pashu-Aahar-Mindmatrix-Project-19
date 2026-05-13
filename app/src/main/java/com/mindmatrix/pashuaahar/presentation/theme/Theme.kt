package com.mindmatrix.pashuaahar.presentation.theme

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable

private val PashuColorScheme = lightColorScheme(
    primary = FieldGreen,
    onPrimary = Cream,
    primaryContainer = Meadow,
    onPrimaryContainer = Ink,
    secondary = Turmeric,
    onSecondary = Ink,
    tertiary = Sky,
    background = Cream,
    surface = Cream,
    surfaceVariant = SurfaceMuted,
    onBackground = Ink,
    onSurface = Ink,
    outline = LeafGreen.copy(alpha = 0.42f)
)

@Composable
fun PashuAaharTheme(content: @Composable () -> Unit) {
    MaterialTheme(
        colorScheme = PashuColorScheme,
        typography = PashuTypography,
        content = content
    )
}
