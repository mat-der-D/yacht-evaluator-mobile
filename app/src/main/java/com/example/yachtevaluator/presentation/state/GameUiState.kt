package com.example.yachtevaluator.presentation.state

import com.example.yachtevaluator.domain.model.Category
import com.example.yachtevaluator.domain.model.GameState

data class GameUiState(
    val gameState: GameState = GameState.INITIAL,
    val predictedScores: Map<Category, Int> = emptyMap(),
    val evaluationState: EvaluationUiState = EvaluationUiState.Idle
)
