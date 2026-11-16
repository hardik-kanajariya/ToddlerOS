package com.toddleros.core.theme

import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Shapes
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.unit.dp

private val LightColorScheme = lightColorScheme(
    primary = ToddlerPrimary,
    onPrimary = ToddlerPrimaryOn,
    primaryContainer = ToddlerPrimaryContainer,
    onPrimaryContainer = ToddlerPrimary,
    background = ToddlerPaper,
    onBackground = ToddlerInk,
    surface = ToddlerSurface,
    onSurface = ToddlerInk,
    surfaceVariant = ToddlerSurfaceElevated,
    onSurfaceVariant = ToddlerInkSecondary,
    outline = ToddlerBorder
)

val ToddlerShapes = Shapes(
    small = RoundedCornerShape(12.dp),
    medium = RoundedCornerShape(20.dp),
    large = RoundedCornerShape(28.dp),
    extraLarge = RoundedCornerShape(36.dp)
)

@Composable
fun ToddlerOSTheme(content: @Composable () -> Unit) {
    MaterialTheme(
        colorScheme = LightColorScheme,
        typography = ToddlerTypography,
        shapes = ToddlerShapes,
        content = content
    )
}
