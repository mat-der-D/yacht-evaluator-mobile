package net.smoothpudding.yachtevaluator.ui.component

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBars
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.unit.dp
import net.smoothpudding.yachtevaluator.R
import net.smoothpudding.yachtevaluator.domain.model.GameMode
import net.smoothpudding.yachtevaluator.ui.theme.AnalysisPrimary
import net.smoothpudding.yachtevaluator.ui.theme.PlayPrimary
import net.smoothpudding.yachtevaluator.ui.theme.SettingsPrimary

@Composable
fun ModeTabs(
    currentMode: GameMode,
    onModeChange: (GameMode) -> Unit,
    modifier: Modifier = Modifier
) {
    NavigationBar(
        modifier = modifier
            .windowInsetsPadding(WindowInsets.navigationBars)
            .height(64.dp),
        containerColor = MaterialTheme.colorScheme.surface,
        contentColor = MaterialTheme.colorScheme.onSurface,
        windowInsets = WindowInsets(0, 0, 0, 0)
    ) {
        NavigationBarItem(
            selected = currentMode == GameMode.PLAY,
            onClick = { onModeChange(GameMode.PLAY) },
            icon = {
                Text(
                    text = "\uD83C\uDFB2",
                    style = MaterialTheme.typography.titleLarge
                )
            },
            label = {
                Text(
                    text = stringResource(R.string.mode_play),
                    style = MaterialTheme.typography.labelMedium
                )
            },
            colors = NavigationBarItemDefaults.colors(
                selectedIconColor = PlayPrimary,
                selectedTextColor = PlayPrimary,
                unselectedIconColor = MaterialTheme.colorScheme.onSurfaceVariant,
                unselectedTextColor = MaterialTheme.colorScheme.onSurfaceVariant,
                indicatorColor = PlayPrimary.copy(alpha = 0.12f)
            ),
            modifier = Modifier.semantics {
                contentDescription = "Play mode tab"
            }
        )

        NavigationBarItem(
            selected = currentMode == GameMode.ANALYSIS,
            onClick = { onModeChange(GameMode.ANALYSIS) },
            icon = {
                Text(
                    text = "\uD83D\uDD0D",
                    style = MaterialTheme.typography.titleLarge
                )
            },
            label = {
                Text(
                    text = stringResource(R.string.mode_analysis),
                    style = MaterialTheme.typography.labelMedium
                )
            },
            colors = NavigationBarItemDefaults.colors(
                selectedIconColor = AnalysisPrimary,
                selectedTextColor = AnalysisPrimary,
                unselectedIconColor = MaterialTheme.colorScheme.onSurfaceVariant,
                unselectedTextColor = MaterialTheme.colorScheme.onSurfaceVariant,
                indicatorColor = AnalysisPrimary.copy(alpha = 0.12f)
            ),
            modifier = Modifier.semantics {
                contentDescription = "Analysis mode tab"
            }
        )

        NavigationBarItem(
            selected = currentMode == GameMode.SETTINGS,
            onClick = { onModeChange(GameMode.SETTINGS) },
            icon = {
                Text(
                    text = "⚙️",
                    style = MaterialTheme.typography.titleLarge
                )
            },
            label = {
                Text(
                    text = stringResource(R.string.mode_settings),
                    style = MaterialTheme.typography.labelMedium
                )
            },
            colors = NavigationBarItemDefaults.colors(
                selectedIconColor = SettingsPrimary,
                selectedTextColor = SettingsPrimary,
                unselectedIconColor = MaterialTheme.colorScheme.onSurfaceVariant,
                unselectedTextColor = MaterialTheme.colorScheme.onSurfaceVariant,
                indicatorColor = SettingsPrimary.copy(alpha = 0.12f)
            ),
            modifier = Modifier.semantics {
                contentDescription = "Settings mode tab"
            }
        )
    }
}
