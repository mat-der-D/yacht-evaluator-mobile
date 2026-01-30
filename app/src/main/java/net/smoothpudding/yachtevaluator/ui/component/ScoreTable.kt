package net.smoothpudding.yachtevaluator.ui.component

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.Dp
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
    modifier: Modifier = Modifier,
    isCompactMode: Boolean = false,
    compactRowHeight: Dp? = null
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
                onScoreUpdate = { value -> onScoreUpdate(category, value) },
                isCompactMode = isCompactMode,
                compactRowHeight = compactRowHeight
            )
            HorizontalDivider(color = Outline, thickness = 1.dp)
        }

        // Upper total
        TotalRow(
            label = stringResource(R.string.upper_total),
            score = scoreSheet.upperTotal,
            isCompactMode = isCompactMode,
            compactRowHeight = compactRowHeight
        )
        HorizontalDivider(color = Outline, thickness = 1.dp)

        // Bonus
        TotalRow(
            label = stringResource(R.string.bonus),
            score = scoreSheet.bonus,
            isBonus = true,
            isCompactMode = isCompactMode,
            compactRowHeight = compactRowHeight
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
                onScoreUpdate = { value -> onScoreUpdate(category, value) },
                isCompactMode = isCompactMode,
                compactRowHeight = compactRowHeight
            )
            HorizontalDivider(color = Outline, thickness = 1.dp)
        }

        // Final total
        TotalRow(
            label = stringResource(R.string.total),
            score = scoreSheet.finalTotal,
            isCompactMode = isCompactMode,
            compactRowHeight = compactRowHeight
        )
    }
}
