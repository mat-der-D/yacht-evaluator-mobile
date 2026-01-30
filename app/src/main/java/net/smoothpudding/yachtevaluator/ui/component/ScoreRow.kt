package net.smoothpudding.yachtevaluator.ui.component

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import net.smoothpudding.yachtevaluator.R
import net.smoothpudding.yachtevaluator.domain.calculator.ScoreValidator
import net.smoothpudding.yachtevaluator.domain.model.Category
import net.smoothpudding.yachtevaluator.domain.model.GameMode
import net.smoothpudding.yachtevaluator.domain.model.RollCount
import net.smoothpudding.yachtevaluator.ui.theme.ConfirmedScoreBackground
import net.smoothpudding.yachtevaluator.ui.theme.PredictedScoreColor

@Composable
fun ScoreRow(
    category: Category,
    confirmedScore: Int?,
    predictedScore: Int?,
    gameMode: GameMode,
    rollCount: RollCount,
    refreshTrigger: Long,
    onConfirm: () -> Unit,
    onScoreClick: () -> Unit,
    onScoreUpdate: (Int?) -> Unit = {},
    modifier: Modifier = Modifier
) {
    val isConfirmed = confirmedScore != null
    val backgroundColor = MaterialTheme.colorScheme.surface
    val showConfirmButton = !isConfirmed && rollCount != RollCount.ZERO
    val isAnalysisMode = gameMode == GameMode.ANALYSIS

    // Local state for text input in analysis mode
    var inputText by remember(confirmedScore, refreshTrigger) { mutableStateOf(confirmedScore?.toString() ?: "") }
    var validationError by remember(confirmedScore, refreshTrigger) { mutableStateOf("") }

    // Validate input
    val validateAndUpdate = { text: String ->
        inputText = text
        validationError = ""

        if (text.isEmpty()) {
            onScoreUpdate(null)
        } else {
            val value = text.toIntOrNull()
            if (value == null || value < 0) {
                validationError = "無効な値"
            } else if (!ScoreValidator.isValidScore(category, value)) {
                validationError = "不正なスコア"
            } else {
                onScoreUpdate(value)
            }
        }
    }

    if (isAnalysisMode) {
        // Analysis mode: input field
        Column(
            modifier = modifier
                .fillMaxWidth()
                .background(backgroundColor)
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(38.dp)
                    .padding(horizontal = 16.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                // Category name
                val categoryName = stringResource(category.getDisplayNameResId())
                Text(
                    text = categoryName,
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurface,
                    modifier = Modifier.weight(1f)
                )

                // Input field
                Box(
                    modifier = Modifier
                        .weight(1f)
                        .padding(horizontal = 8.dp)
                        .height(32.dp)
                        .border(
                            1.dp,
                            if (validationError.isNotEmpty()) MaterialTheme.colorScheme.error else Color.Gray,
                            RoundedCornerShape(4.dp)
                        )
                        .background(Color.White, RoundedCornerShape(4.dp)),
                    contentAlignment = Alignment.Center
                ) {
                    BasicTextField(
                        value = inputText,
                        onValueChange = validateAndUpdate,
                        modifier = Modifier.fillMaxWidth(),
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                        textStyle = MaterialTheme.typography.bodyMedium.copy(textAlign = TextAlign.Center),
                        singleLine = true
                    )
                }

                // Confirm button
                Box(
                    modifier = Modifier.weight(1f),
                    contentAlignment = Alignment.CenterEnd
                ) {
                    if (showConfirmButton && validationError.isEmpty()) {
                        Button(
                            onClick = onConfirm,
                            colors = ButtonDefaults.buttonColors(
                                containerColor = MaterialTheme.colorScheme.primary,
                                contentColor = MaterialTheme.colorScheme.onPrimary
                            ),
                            contentPadding = androidx.compose.foundation.layout.PaddingValues(horizontal = 12.dp),
                            modifier = Modifier.height(28.dp)
                        ) {
                            Text(
                                text = "✓",
                                style = MaterialTheme.typography.titleMedium
                            )
                        }
                    }
                }
            }

            // Error message
            if (validationError.isNotEmpty()) {
                Text(
                    text = validationError,
                    style = MaterialTheme.typography.labelSmall,
                    color = MaterialTheme.colorScheme.error,
                    modifier = Modifier.padding(horizontal = 8.dp)
                )
            }
        }
    } else {
        // Play mode or confirmed score: display mode
        Row(
            modifier = modifier
                .fillMaxWidth()
                .height(38.dp)
                .background(backgroundColor)
                .padding(horizontal = 16.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            // Category name
            val categoryName = stringResource(category.getDisplayNameResId())
            Text(
                text = categoryName,
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurface,
                modifier = Modifier.weight(1f)
            )

            // Score display
            Box(
                modifier = Modifier
                    .weight(1f)
                    .then(
                        if (!isConfirmed && !isAnalysisMode) {
                            Modifier
                                .clip(RoundedCornerShape(4.dp))
                                .clickable(onClick = onScoreClick)
                        } else {
                            Modifier
                        }
                    )
                    .semantics {
                        contentDescription = "$categoryName: ${confirmedScore ?: predictedScore ?: 0} points"
                    },
                contentAlignment = Alignment.Center
            ) {
                if (isConfirmed) {
                    Text(
                        text = confirmedScore.toString(),
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onSurface,
                        textAlign = TextAlign.Center
                    )
                } else {
                    Text(
                        text = "(+${predictedScore ?: 0})",
                        style = MaterialTheme.typography.bodyMedium,
                        color = PredictedScoreColor,
                        textAlign = TextAlign.Center
                    )
                }
            }

            // Confirm button
            Box(
                modifier = Modifier.weight(1f),
                contentAlignment = Alignment.CenterEnd
            ) {
                if (showConfirmButton) {
                    Button(
                        onClick = onConfirm,
                        colors = ButtonDefaults.buttonColors(
                            containerColor = MaterialTheme.colorScheme.primary,
                            contentColor = MaterialTheme.colorScheme.onPrimary
                        ),
                        contentPadding = androidx.compose.foundation.layout.PaddingValues(horizontal = 12.dp),
                        modifier = Modifier.height(28.dp)
                    ) {
                        Text(
                            text = "✓",
                            style = MaterialTheme.typography.titleMedium
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun TotalRow(
    label: String,
    score: Int,
    modifier: Modifier = Modifier,
    isBonus: Boolean = false
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .height(38.dp)
            .background(ConfirmedScoreBackground)
            .padding(horizontal = 16.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(
            text = label,
            style = if (isBonus) MaterialTheme.typography.bodyMedium else MaterialTheme.typography.titleMedium,
            color = MaterialTheme.colorScheme.onSurface,
            modifier = Modifier.weight(1f)
        )

        Text(
            text = score.toString(),
            style = if (isBonus) MaterialTheme.typography.bodyMedium else MaterialTheme.typography.titleMedium,
            color = MaterialTheme.colorScheme.onSurface,
            textAlign = TextAlign.Center,
            modifier = Modifier.weight(1f)
        )

        Box(modifier = Modifier.weight(1f))
    }
}
