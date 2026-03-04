package com.workoutapp.ui.theme

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.runtime.Composable

private val DarkColorScheme = darkColorScheme(
    primary            = Primary,
    onPrimary          = OnPrimary,
    primaryContainer   = PrimaryContainer,
    onPrimaryContainer = OnPrimaryContainer,
    secondary          = Secondary,
    onSecondary        = OnSecondary,
    secondaryContainer = SecondaryContainer,
    onSecondaryContainer = OnSecondaryContainer,
    tertiary           = Tertiary,
    onTertiary         = OnTertiary,
    tertiaryContainer  = TertiaryContainer,
    onTertiaryContainer = OnTertiaryContainer,
    error              = Error,
    onError            = OnError,
    background         = Background,
    onBackground       = OnSurface,
    surface            = Surface,
    onSurface          = OnSurfaceColor,
    surfaceVariant     = SurfaceVariant,
    onSurfaceVariant   = OnSurfaceVariant,
    outline            = Outline
)

@Composable
fun AtlasTheme(content: @Composable () -> Unit) {
    MaterialTheme(
        colorScheme = DarkColorScheme,
        typography = WorkoutTypography,
        content = content
    )
}

// Backward-compat alias so any remaining references compile
@Composable
fun WorkoutAppTheme(content: @Composable () -> Unit) = AtlasTheme(content)
