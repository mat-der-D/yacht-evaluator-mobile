package net.smoothpudding.yachtevaluator.ui.component

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import net.smoothpudding.yachtevaluator.R
import net.smoothpudding.yachtevaluator.domain.model.Category
import net.smoothpudding.yachtevaluator.domain.model.GameMode
import net.smoothpudding.yachtevaluator.domain.model.RollCount
import net.smoothpudding.yachtevaluator.domain.model.ScoreSheet
import net.smoothpudding.yachtevaluator.ui.theme.Outline

@Composable
fun ScoreTable(
    scoreSheet: ScoreSheet,
    predictedScores: Map<Category, Int>,
    gameMode: GameMode,
    rollCount: RollCount,
    refreshTrigger: Long,
    onConfirmScore: (Category) -> Unit,
    onScoreClick: (Category) -> Unit,
    onScoreUpdate: (Category, Int?) -> Unit = { _, _ -> },
    modifier: Modifier = Modifier
) {
    Column(modifier = modifier.fillMaxWidth()) {
        // Upper section
        Category.upperCategories.forEach { category ->
            ScoreRow(
                category = category,
                confirmedScore = scoreSheet.get(category),
                predictedScore = predictedScores[category],
                gameMode = gameMode,
                rollCount = rollCount,
                refreshTrigger = refreshTrigger,
                onConfirm = { onConfirmScore(category) },
                onScoreClick = { onScoreClick(category) },
                onScoreUpdate = { value -> onScoreUpdate(category, value) }
            )
            HorizontalDivider(color = Outline, thickness = 1.dp)
        }

        // Upper total
        TotalRow(
            label = stringResource(R.string.upper_total),
            score = scoreSheet.upperTotal
        )
        HorizontalDivider(color = Outline, thickness = 1.dp)

        // Bonus
        TotalRow(
            label = stringResource(R.string.bonus),
            score = scoreSheet.bonus,
            isBonus = true
        )
        HorizontalDivider(color = MaterialTheme.colorScheme.outline, thickness = 2.dp)

        // Lower section
        Category.lowerCategories.forEach { category ->
            ScoreRow(
                category = category,
                confirmedScore = scoreSheet.get(category),
                predictedScore = predictedScores[category],
                gameMode = gameMode,
                rollCount = rollCount,
                refreshTrigger = refreshTrigger,
                onConfirm = { onConfirmScore(category) },
                onScoreClick = { onScoreClick(category) },
                onScoreUpdate = { value -> onScoreUpdate(category, value) }
            )
            HorizontalDivider(color = Outline, thickness = 1.dp)
        }

        // Final total
        TotalRow(
            label = stringResource(R.string.total),
            score = scoreSheet.finalTotal
        )
    }
}
