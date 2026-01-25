package com.example.yachtevaluator.ui.component

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.unit.dp
import com.example.yachtevaluator.R
import com.example.yachtevaluator.domain.model.GameMode
import com.example.yachtevaluator.presentation.state.EvaluationUiState
import com.example.yachtevaluator.presentation.state.Recommendation
import com.example.yachtevaluator.ui.theme.Outline

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EvaluationPanel(
    evaluationState: EvaluationUiState,
    gameMode: GameMode,
    currentScore: Int,
    onDismiss: () -> Unit,
    onApplyRecommendation: (Recommendation) -> Unit,
    modifier: Modifier = Modifier
) {
    if (evaluationState is EvaluationUiState.Idle) return

    val sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)

    ModalBottomSheet(
        onDismissRequest = onDismiss,
        sheetState = sheetState,
        containerColor = MaterialTheme.colorScheme.surface,
        modifier = modifier
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        ) {
            // Header
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = if (evaluationState is EvaluationUiState.Error) "エラー" else "評価値",
                    style = MaterialTheme.typography.headlineMedium,
                    color = MaterialTheme.colorScheme.onSurface
                )
                IconButton(
                    onClick = onDismiss,
                    modifier = Modifier.semantics {
                        contentDescription = "Close evaluation panel"
                    }
                ) {
                    Icon(
                        imageVector = Icons.Default.Close,
                        contentDescription = stringResource(R.string.close),
                        tint = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            }

            Spacer(modifier = Modifier.height(8.dp))

            when (evaluationState) {
                is EvaluationUiState.Loading -> {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(200.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        CircularProgressIndicator(
                            color = MaterialTheme.colorScheme.primary
                        )
                    }
                }

                is EvaluationUiState.Success -> {
                    // Current score info
                    Text(
                        text = "現在の合計スコア: ${currentScore}点\n※ 期待値は最終スコアの見込みです",
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                        modifier = Modifier.padding(bottom = 16.dp)
                    )

                    val bestEV = evaluationState.recommendations.maxOfOrNull { it.expectedValue } ?: 0.0

                    LazyColumn(
                        verticalArrangement = Arrangement.spacedBy(4.dp)
                    ) {
                        items(evaluationState.recommendations) { recommendation ->
                            RecommendationItem(
                                recommendation = recommendation,
                                bestExpectedValue = bestEV,
                                gameMode = gameMode,
                                onApply = { onApplyRecommendation(recommendation) }
                            )
                        }
                    }
                }

                is EvaluationUiState.Error -> {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(200.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = evaluationState.message,
                            style = MaterialTheme.typography.bodyMedium,
                            color = MaterialTheme.colorScheme.error
                        )
                    }
                }

                is EvaluationUiState.Idle -> { /* Already handled above */ }
            }

            Spacer(modifier = Modifier.height(32.dp))
        }
    }
}

@Composable
private fun RecommendationItem(
    recommendation: Recommendation,
    bestExpectedValue: Double,
    gameMode: GameMode,
    onApply: () -> Unit,
    modifier: Modifier = Modifier
) {
    val roundedEV = (recommendation.expectedValue * 100).toInt() / 100.0
    val roundedBestEV = (bestExpectedValue * 100).toInt() / 100.0
    val diff = roundedBestEV - roundedEV
    val diffMessage = if (diff == 0.0) "(Best)" else String.format("(Best - %.2f)", diff)

    Row(
        modifier = modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp, horizontal = 4.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        // Left: Recommendation text
        Column(modifier = Modifier.weight(1f)) {
            when (recommendation) {
                is Recommendation.Dice -> {
                    if (recommendation.diceToHold.isEmpty()) {
                        Text(
                            text = "すべて振り直す",
                            style = MaterialTheme.typography.titleMedium,
                            color = MaterialTheme.colorScheme.onSurface
                        )
                    } else {
                        val diceText = recommendation.diceToHold.joinToString("") { die ->
                            when (die) {
                                1 -> "\u2680"
                                2 -> "\u2681"
                                3 -> "\u2682"
                                4 -> "\u2683"
                                5 -> "\u2684"
                                6 -> "\u2685"
                                else -> die.toString()
                            }
                        }
                        Text(
                            text = "$diceText を残す",
                            style = MaterialTheme.typography.titleMedium,
                            color = MaterialTheme.colorScheme.onSurface
                        )
                    }
                }

                is Recommendation.CategoryChoice -> {
                    val categoryName = stringResource(recommendation.category.getDisplayNameResId())
                    Text(
                        text = "${categoryName}確定",
                        style = MaterialTheme.typography.titleMedium,
                        color = MaterialTheme.colorScheme.onSurface
                    )
                }
            }

            Text(
                text = String.format("期待値 %.2f 点 %s", roundedEV, diffMessage),
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }

        // Right: Button
        val showButton = when {
            gameMode == GameMode.PLAY -> true
            gameMode == GameMode.ANALYSIS && recommendation is Recommendation.CategoryChoice -> true
            else -> false
        }

        if (showButton) {
            Button(
                onClick = onApply,
                colors = ButtonDefaults.buttonColors(
                    containerColor = MaterialTheme.colorScheme.primary,
                    contentColor = MaterialTheme.colorScheme.onPrimary
                ),
                contentPadding = androidx.compose.foundation.layout.PaddingValues(8.dp),
                modifier = Modifier.size(56.dp)
            ) {
                Text(
                    text = when (recommendation) {
                        is Recommendation.Dice -> "🔒"
                        is Recommendation.CategoryChoice -> "✓"
                    },
                    style = MaterialTheme.typography.headlineSmall
                )
            }
        }
    }

    HorizontalDivider(color = Outline.copy(alpha = 0.3f), thickness = 1.dp)
}
