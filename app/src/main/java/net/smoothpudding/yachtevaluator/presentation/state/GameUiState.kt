package net.smoothpudding.yachtevaluator.presentation.state

import net.smoothpudding.yachtevaluator.domain.model.Category
import net.smoothpudding.yachtevaluator.domain.model.GameState

data class GameUiState(
    val gameState: GameState = GameState.INITIAL,
    val predictedScores: Map<Category, Int> = emptyMap(),
    val evaluationState: EvaluationUiState = EvaluationUiState.Idle,
    val refreshTrigger: Long = 0L
)
