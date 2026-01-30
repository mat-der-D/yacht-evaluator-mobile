package net.smoothpudding.yachtevaluator.ui.component

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.spring
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.detectVerticalDragGestures
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.unit.dp
import net.smoothpudding.yachtevaluator.R
import net.smoothpudding.yachtevaluator.domain.model.GameMode
import net.smoothpudding.yachtevaluator.presentation.state.EvaluationUiState
import net.smoothpudding.yachtevaluator.presentation.state.Recommendation
import net.smoothpudding.yachtevaluator.ui.theme.Outline

// Height fractions
private const val DEFAULT_HEIGHT_FRACTION = 0.45f  // ~4.5 items
private const val MIN_HEIGHT_FRACTION = 0.25f      // ~2.5 items minimum
private const val MAX_HEIGHT_FRACTION = 0.85f      // 85% screen max

// Visual dimensions
private val PANEL_CORNER_RADIUS = 16.dp
private val DRAG_HANDLE_WIDTH = 40.dp
private val DRAG_HANDLE_HEIGHT = 4.dp
private val DRAG_HANDLE_AREA_HEIGHT = 32.dp
private val PANEL_HORIZONTAL_PADDING = 16.dp
private val PANEL_TOP_PADDING = 8.dp
private val PANEL_BOTTOM_PADDING = 16.dp

// Backdrop
private const val BACKDROP_ALPHA = 0.5f

@Composable
fun EvaluationPanel(
    evaluationState: EvaluationUiState,
    gameMode: GameMode,
    currentScore: Int,
    onDismiss: () -> Unit,
    onApplyRecommendation: (Recommendation) -> Unit,
    modifier: Modifier = Modifier
) {
    // Local state for panel height
    var heightFraction by remember { mutableStateOf(DEFAULT_HEIGHT_FRACTION) }

    val animatedHeightFraction by animateFloatAsState(
        targetValue = heightFraction,
        animationSpec = spring(
            dampingRatio = Spring.DampingRatioMediumBouncy,
            stiffness = Spring.StiffnessMedium
        ),
        label = "Panel Height Animation"
    )

    AnimatedVisibility(
        visible = evaluationState !is EvaluationUiState.Idle,
        enter = slideInVertically(
            initialOffsetY = { it },
            animationSpec = spring(
                dampingRatio = Spring.DampingRatioMediumBouncy,
                stiffness = Spring.StiffnessMedium
            )
        ) + fadeIn(),
        exit = slideOutVertically(
            targetOffsetY = { it },
            animationSpec = spring(
                dampingRatio = Spring.DampingRatioNoBouncy,
                stiffness = Spring.StiffnessMedium
            )
        ) + fadeOut(),
        modifier = modifier
    ) {
        Box(modifier = Modifier.fillMaxSize()) {
            // Layer 1: Backdrop overlay
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(Color.Black.copy(alpha = BACKDROP_ALPHA))
                    .clickable(
                        onClick = onDismiss,
                        indication = null,
                        interactionSource = remember { MutableInteractionSource() }
                    )
            )

            // Layer 2: Panel container
            BoxWithConstraints(
                modifier = Modifier
                    .align(Alignment.BottomCenter)
                    .fillMaxWidth()
            ) {
                val maxHeightPx = constraints.maxHeight.toFloat()
                val panelHeight = maxHeight * animatedHeightFraction

                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(panelHeight)
                        .background(
                            color = MaterialTheme.colorScheme.surface,
                            shape = RoundedCornerShape(
                                topStart = PANEL_CORNER_RADIUS,
                                topEnd = PANEL_CORNER_RADIUS
                            )
                        )
                        .clickable(
                            onClick = { /* Consume click to prevent backdrop dismissal */ },
                            indication = null,
                            interactionSource = remember { MutableInteractionSource() }
                        )
                ) {
                    // Layer 3: Drag handle
                    DragHandle(
                        onDragDelta = { dragAmount ->
                            val deltaFraction = -dragAmount / maxHeightPx
                            heightFraction = (heightFraction + deltaFraction)
                                .coerceIn(MIN_HEIGHT_FRACTION, MAX_HEIGHT_FRACTION)
                        }
                    )

                    Spacer(modifier = Modifier.height(PANEL_TOP_PADDING))

                    // Header
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = PANEL_HORIZONTAL_PADDING),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = if (evaluationState is EvaluationUiState.Error)
                                stringResource(R.string.error_title)
                            else
                                stringResource(R.string.evaluation_title),
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

                    // Content area
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .weight(1f)
                            .padding(horizontal = PANEL_HORIZONTAL_PADDING)
                    ) {
                        when (evaluationState) {
                            is EvaluationUiState.Loading -> {
                                Box(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .weight(1f),
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
                                    text = stringResource(R.string.current_score_info, currentScore),
                                    style = MaterialTheme.typography.bodyMedium,
                                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                                    modifier = Modifier.padding(bottom = 16.dp)
                                )

                                val bestEV = evaluationState.recommendations.maxOfOrNull { it.expectedValue } ?: 0.0

                                LazyColumn(
                                    verticalArrangement = Arrangement.spacedBy(4.dp),
                                    modifier = Modifier.weight(1f)
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
                                        .weight(1f),
                                    contentAlignment = Alignment.Center
                                ) {
                                    Text(
                                        text = evaluationState.message,
                                        style = MaterialTheme.typography.bodyMedium,
                                        color = MaterialTheme.colorScheme.error
                                    )
                                }
                            }

                            is EvaluationUiState.Idle -> { /* Already handled in AnimatedVisibility */ }
                        }
                    }

                    Spacer(modifier = Modifier.height(PANEL_BOTTOM_PADDING))
                }
            }
        }
    }
}

@Composable
private fun DragHandle(
    onDragDelta: (Float) -> Unit,
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier
            .fillMaxWidth()
            .height(DRAG_HANDLE_AREA_HEIGHT)
            .pointerInput(Unit) {
                detectVerticalDragGestures { change, dragAmount ->
                    change.consume()
                    onDragDelta(dragAmount)
                }
            },
        contentAlignment = Alignment.Center
    ) {
        // Visual indicator (horizontal bar)
        Box(
            modifier = Modifier
                .width(DRAG_HANDLE_WIDTH)
                .height(DRAG_HANDLE_HEIGHT)
                .background(
                    MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.4f),
                    shape = RoundedCornerShape(2.dp)
                )
        )
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
    val diffMessage = if (diff == 0.0)
        stringResource(R.string.best)
    else
        stringResource(R.string.best_minus_format, diff)

    HorizontalDivider(color = Outline.copy(alpha = 1.0f), thickness = 1.5.dp)

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
                            text = stringResource(R.string.reroll_all),
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
                            text = stringResource(R.string.hold_dice_format, diceText),
                            style = MaterialTheme.typography.titleMedium,
                            color = MaterialTheme.colorScheme.onSurface
                        )
                    }
                }

                is Recommendation.CategoryChoice -> {
                    val categoryName = stringResource(recommendation.category.getDisplayNameResId())
                    Text(
                        text = stringResource(R.string.confirm_category_format, categoryName),
                        style = MaterialTheme.typography.titleMedium,
                        color = MaterialTheme.colorScheme.onSurface
                    )
                }
            }

            Text(
                text = stringResource(R.string.expected_value_format, roundedEV) + " " + diffMessage,
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
                contentPadding = androidx.compose.foundation.layout.PaddingValues(horizontal = 12.dp),
                modifier = Modifier.height(28.dp)
            ) {
                Text(
                    text = when (recommendation) {
                        is Recommendation.Dice -> "🔒"
                        is Recommendation.CategoryChoice -> "✓"
                    },
                    style = MaterialTheme.typography.titleMedium
                )
            }
        }
    }
}
