package com.example.yachtevaluator.ui.theme

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.staticCompositionLocalOf
import com.example.yachtevaluator.domain.model.GameMode

data class ModeColors(
    val primary: androidx.compose.ui.graphics.Color,
    val primaryContainer: androidx.compose.ui.graphics.Color,
    val onPrimary: androidx.compose.ui.graphics.Color,
    val secondary: androidx.compose.ui.graphics.Color
)

val PlayModeColors = ModeColors(
    primary = PlayPrimary,
    primaryContainer = PlayPrimaryContainer,
    onPrimary = PlayOnPrimary,
    secondary = PlaySecondary
)

val AnalysisModeColors = ModeColors(
    primary = AnalysisPrimary,
    primaryContainer = AnalysisPrimaryContainer,
    onPrimary = AnalysisOnPrimary,
    secondary = AnalysisSecondary
)

val LocalModeColors = staticCompositionLocalOf { PlayModeColors }

private val LightColorScheme = lightColorScheme(
    primary = PlayPrimary,
    primaryContainer = PlayPrimaryContainer,
    onPrimary = PlayOnPrimary,
    secondary = PlaySecondary,
    background = Background,
    surface = Surface,
    onSurface = OnSurface,
    onSurfaceVariant = OnSurfaceVariant,
    outline = Outline,
    outlineVariant = OutlineVariant
)

@Composable
fun YachtEvaluatorTheme(
    gameMode: GameMode = GameMode.PLAY,
    content: @Composable () -> Unit
) {
    val modeColors = when (gameMode) {
        GameMode.PLAY -> PlayModeColors
        GameMode.ANALYSIS -> AnalysisModeColors
    }

    val colorScheme = LightColorScheme.copy(
        primary = modeColors.primary,
        primaryContainer = modeColors.primaryContainer,
        onPrimary = modeColors.onPrimary,
        secondary = modeColors.secondary
    )

    CompositionLocalProvider(LocalModeColors provides modeColors) {
        MaterialTheme(
            colorScheme = colorScheme,
            typography = Typography,
            content = content
        )
    }
}
