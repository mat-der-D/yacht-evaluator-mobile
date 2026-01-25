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
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
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
                    text = stringResource(R.string.evaluation_results),
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

            Spacer(modifier = Modifier.height(16.dp))

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
                    LazyColumn(
                        verticalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        items(evaluationState.recommendations) { recommendation ->
                            RecommendationItem(
                                recommendation = recommendation,
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
    gameMode: GameMode,
    onApply: () -> Unit,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.primaryContainer
        )
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                when (recommendation) {
                    is Recommendation.Dice -> {
                        val diceText = recommendation.diceToHold.joinToString(" ") { die ->
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
                            text = "Hold: $diceText",
                            style = MaterialTheme.typography.bodyLarge,
                            color = MaterialTheme.colorScheme.onPrimaryContainer
                        )
                    }

                    is Recommendation.CategoryChoice -> {
                        Text(
                            text = "Select: ${recommendation.category.displayName}",
                            style = MaterialTheme.typography.bodyLarge,
                            color = MaterialTheme.colorScheme.onPrimaryContainer
                        )
                    }
                }

                Text(
                    text = String.format("EV: %.1f", recommendation.expectedValue),
                    style = MaterialTheme.typography.labelLarge,
                    color = MaterialTheme.colorScheme.onPrimaryContainer
                )
            }

            if (gameMode == GameMode.PLAY) {
                Spacer(modifier = Modifier.height(12.dp))
                HorizontalDivider(color = Outline.copy(alpha = 0.5f))
                Spacer(modifier = Modifier.height(12.dp))

                Button(
                    onClick = onApply,
                    modifier = Modifier.align(Alignment.End),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = MaterialTheme.colorScheme.primary,
                        contentColor = MaterialTheme.colorScheme.onPrimary
                    )
                ) {
                    Text(
                        text = when (recommendation) {
                            is Recommendation.Dice -> stringResource(R.string.apply)
                            is Recommendation.CategoryChoice -> stringResource(R.string.confirm)
                        },
                        style = MaterialTheme.typography.labelMedium
                    )
                }
            }
        }
    }
}
