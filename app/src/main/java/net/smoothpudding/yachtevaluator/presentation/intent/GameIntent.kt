package net.smoothpudding.yachtevaluator.presentation.intent

import net.smoothpudding.yachtevaluator.domain.model.Category
import net.smoothpudding.yachtevaluator.domain.model.GameMode
import net.smoothpudding.yachtevaluator.domain.model.RollCount
import net.smoothpudding.yachtevaluator.presentation.state.Recommendation

sealed interface GameIntent {
    data object RollDice : GameIntent
    data class ToggleDiceLock(val index: Int) : GameIntent
    data class ApplyDiceLock(val locks: List<Boolean>) : GameIntent
    data class IncrementDice(val index: Int) : GameIntent
    data class SetRollCount(val value: RollCount) : GameIntent
    data class UpdateScore(val category: Category, val value: Int?) : GameIntent
    data class ConfirmScore(val category: Category) : GameIntent
    data class ChangeMode(val mode: GameMode) : GameIntent
    data object ResetGame : GameIntent
    data object RequestEvaluation : GameIntent
    data class ApplyRecommendation(val recommendation: Recommendation) : GameIntent
    data object DismissEvaluation : GameIntent
}
