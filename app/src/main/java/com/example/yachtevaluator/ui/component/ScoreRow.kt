package com.example.yachtevaluator.ui.component

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.example.yachtevaluator.R
import com.example.yachtevaluator.domain.model.Category
import com.example.yachtevaluator.domain.model.GameMode
import com.example.yachtevaluator.domain.model.RollCount
import com.example.yachtevaluator.ui.theme.ConfirmedScoreBackground
import com.example.yachtevaluator.ui.theme.PredictedScoreColor

@Composable
fun ScoreRow(
    category: Category,
    confirmedScore: Int?,
    predictedScore: Int?,
    gameMode: GameMode,
    rollCount: RollCount,
    onConfirm: () -> Unit,
    onScoreClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val isConfirmed = confirmedScore != null
    val backgroundColor = if (isConfirmed) ConfirmedScoreBackground else MaterialTheme.colorScheme.surface
    val showConfirmButton = !isConfirmed && rollCount != RollCount.ZERO

    Row(
        modifier = modifier
            .fillMaxWidth()
            .height(48.dp)
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
                    if (!isConfirmed && gameMode == GameMode.ANALYSIS) {
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
                    modifier = Modifier.height(32.dp)
                ) {
                    Text(
                        text = stringResource(R.string.confirm),
                        style = MaterialTheme.typography.labelSmall
                    )
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
            .height(48.dp)
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
