package net.smoothpudding.yachtevaluator.ui.theme

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.staticCompositionLocalOf
import net.smoothpudding.yachtevaluator.domain.model.GameMode

data class ModeColors(
    val primary: androidx.compose.ui.graphics.Color,
    val primaryContainer: androidx.compose.ui.graphics.Color,
    val onPrimary: androidx.compose.ui.graphics.Color,
    val secondary: androidx.compose.ui.graphics.Color,
    val background: androidx.compose.ui.graphics.Color
)

val PlayModeColors = ModeColors(
    primary = PlayPrimary,
    primaryContainer = PlayPrimaryContainer,
    onPrimary = PlayOnPrimary,
    secondary = PlaySecondary,
    background = PlayBackground
)

val AnalysisModeColors = ModeColors(
    primary = AnalysisPrimary,
    primaryContainer = AnalysisPrimaryContainer,
    onPrimary = AnalysisOnPrimary,
    secondary = AnalysisSecondary,
    background = AnalysisBackground
)

val SettingsModeColors = ModeColors(
    primary = SettingsPrimary,
    primaryContainer = SettingsPrimaryContainer,
    onPrimary = SettingsOnPrimary,
    secondary = SettingsSecondary,
    background = SettingsBackground
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
        GameMode.SETTINGS -> SettingsModeColors
    }

    val colorScheme = LightColorScheme.copy(
        primary = modeColors.primary,
        primaryContainer = modeColors.primaryContainer,
        onPrimary = modeColors.onPrimary,
        secondary = modeColors.secondary,
        background = modeColors.background
    )

    CompositionLocalProvider(LocalModeColors provides modeColors) {
        MaterialTheme(
            colorScheme = colorScheme,
            typography = Typography,
            content = content
        )
    }
}
