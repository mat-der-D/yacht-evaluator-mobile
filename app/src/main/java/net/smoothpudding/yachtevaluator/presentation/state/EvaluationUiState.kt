package net.smoothpudding.yachtevaluator.presentation.state

sealed interface EvaluationUiState {
    data object Idle : EvaluationUiState
    data object Loading : EvaluationUiState
    data class Success(
        val recommendations: List<Recommendation>
    ) : EvaluationUiState
    data class Error(val message: String) : EvaluationUiState
}
