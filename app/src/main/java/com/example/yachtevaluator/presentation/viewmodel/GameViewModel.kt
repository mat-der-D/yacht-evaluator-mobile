package com.example.yachtevaluator.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.yachtevaluator.data.repository.EvaluationRepository
import com.example.yachtevaluator.domain.calculator.ScoreCalculator
import com.example.yachtevaluator.domain.model.Category
import com.example.yachtevaluator.domain.model.RollCount
import com.example.yachtevaluator.presentation.intent.GameIntent
import com.example.yachtevaluator.presentation.state.EvaluationUiState
import com.example.yachtevaluator.presentation.state.GameUiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class GameViewModel @Inject constructor(
    private val evaluationRepository: EvaluationRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(GameUiState())
    val uiState: StateFlow<GameUiState> = _uiState.asStateFlow()

    init {
        updatePredictedScores()
    }

    fun onIntent(intent: GameIntent) {
        when (intent) {
            is GameIntent.RequestEvaluation -> requestEvaluation()
            is GameIntent.DismissEvaluation -> dismissEvaluation()
            else -> {
                val newGameState = GameReducer.reduce(_uiState.value.gameState, intent)
                _uiState.update { it.copy(gameState = newGameState) }
                updatePredictedScores()

                // Dismiss evaluation panel on certain actions
                if (intent is GameIntent.ConfirmScore ||
                    intent is GameIntent.ResetGame ||
                    intent is GameIntent.ApplyRecommendation
                ) {
                    dismissEvaluation()
                }
            }
        }
    }

    private fun updatePredictedScores() {
        val gameState = _uiState.value.gameState
        val dice = gameState.dice
        val scoreSheet = gameState.scoreSheet

        val predictions = Category.entries
            .filter { scoreSheet.get(it) == null }
            .associateWith { category ->
                ScoreCalculator.calculateCategoryScore(category, dice)
            }

        _uiState.update { it.copy(predictedScores = predictions) }
    }

    private fun requestEvaluation() {
        val gameState = _uiState.value.gameState

        // Can only evaluate after at least one roll
        if (gameState.rollCount == RollCount.ZERO) return

        _uiState.update { it.copy(evaluationState = EvaluationUiState.Loading) }

        viewModelScope.launch {
            val result = evaluationRepository.evaluate(
                scoreSheet = gameState.scoreSheet,
                dice = gameState.dice,
                rollCount = gameState.rollCount
            )

            result.fold(
                onSuccess = { recommendations ->
                    _uiState.update {
                        it.copy(evaluationState = EvaluationUiState.Success(recommendations))
                    }
                },
                onFailure = { error ->
                    _uiState.update {
                        it.copy(
                            evaluationState = EvaluationUiState.Error(
                                error.message ?: "Unknown error"
                            )
                        )
                    }
                }
            )
        }
    }

    private fun dismissEvaluation() {
        _uiState.update { it.copy(evaluationState = EvaluationUiState.Idle) }
    }
}
