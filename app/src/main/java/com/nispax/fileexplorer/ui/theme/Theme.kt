package com.nispax.fileexplorer.ui.theme

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.runtime.Composable

private val NispaxColors = darkColorScheme(
    primary = NispaxBlue,
    onPrimary = NispaxText,
    background = NispaxBlack,
    onBackground = NispaxText,
    surface = NispaxSurface,
    onSurface = NispaxText,
    secondary = NispaxTitanium,
    onSurfaceVariant = NispaxMuted
)

@Composable
fun NispaxTheme(content: @Composable () -> Unit) {
    MaterialTheme(colorScheme = NispaxColors, content = content)
}
