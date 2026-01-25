package com.example.yachtevaluator.ui.component

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRow
import androidx.compose.material3.TabRowDefaults
import androidx.compose.material3.TabRowDefaults.tabIndicatorOffset
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.unit.dp
import com.example.yachtevaluator.R
import com.example.yachtevaluator.domain.model.GameMode
import com.example.yachtevaluator.ui.theme.AnalysisPrimary
import com.example.yachtevaluator.ui.theme.PlayPrimary

@Composable
fun ModeTabs(
    currentMode: GameMode,
    onModeChange: (GameMode) -> Unit,
    modifier: Modifier = Modifier
) {
    val selectedIndex = when (currentMode) {
        GameMode.PLAY -> 0
        GameMode.ANALYSIS -> 1
    }

    TabRow(
        selectedTabIndex = selectedIndex,
        modifier = modifier
            .fillMaxWidth()
            .height(60.dp),
        containerColor = MaterialTheme.colorScheme.surface,
        contentColor = MaterialTheme.colorScheme.primary,
        indicator = { tabPositions ->
            TabRowDefaults.SecondaryIndicator(
                modifier = Modifier.tabIndicatorOffset(tabPositions[selectedIndex]),
                color = when (currentMode) {
                    GameMode.PLAY -> PlayPrimary
                    GameMode.ANALYSIS -> AnalysisPrimary
                }
            )
        }
    ) {
        Tab(
            selected = currentMode == GameMode.PLAY,
            onClick = { onModeChange(GameMode.PLAY) },
            modifier = Modifier
                .height(60.dp)
                .semantics {
                    contentDescription = "Play mode tab"
                }
        ) {
            Text(
                text = "\uD83C\uDFAE ${stringResource(R.string.mode_play)}",
                style = MaterialTheme.typography.titleMedium,
                color = if (currentMode == GameMode.PLAY) PlayPrimary else MaterialTheme.colorScheme.onSurfaceVariant,
                modifier = Modifier.semantics {
                    contentDescription = "Play mode"
                }
            )
        }

        Tab(
            selected = currentMode == GameMode.ANALYSIS,
            onClick = { onModeChange(GameMode.ANALYSIS) },
            modifier = Modifier
                .height(60.dp)
                .semantics {
                    contentDescription = "Analysis mode tab"
                }
        ) {
            Text(
                text = "\uD83D\uDD0D ${stringResource(R.string.mode_analysis)}",
                style = MaterialTheme.typography.titleMedium,
                color = if (currentMode == GameMode.ANALYSIS) AnalysisPrimary else MaterialTheme.colorScheme.onSurfaceVariant,
                modifier = Modifier.semantics {
                    contentDescription = "Analysis mode"
                }
            )
        }
    }
}
