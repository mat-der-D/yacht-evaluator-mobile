package com.example.yachtevaluator.presentation.viewmodel

import com.example.yachtevaluator.domain.calculator.ScoreCalculator
import com.example.yachtevaluator.domain.model.GameMode
import com.example.yachtevaluator.domain.model.GameState
import com.example.yachtevaluator.domain.model.RollCount
import com.example.yachtevaluator.presentation.intent.GameIntent
import com.example.yachtevaluator.presentation.state.Recommendation
import kotlin.random.Random

object GameReducer {

    fun reduce(state: GameState, intent: GameIntent): GameState {
        return when (intent) {
            is GameIntent.RollDice -> rollDice(state)
            is GameIntent.ToggleDiceLock -> toggleDiceLock(state, intent.index)
            is GameIntent.ApplyDiceLock -> applyDiceLock(state, intent.locks)
            is GameIntent.IncrementDice -> incrementDice(state, intent.index)
            is GameIntent.SetRollCount -> setRollCount(state, intent.value)
            is GameIntent.UpdateScore -> updateScore(state, intent)
            is GameIntent.ConfirmScore -> confirmScore(state, intent)
            is GameIntent.ChangeMode -> changeMode(state, intent.mode)
            is GameIntent.ResetGame -> resetGame(state)
            is GameIntent.ApplyRecommendation -> applyRecommendation(state, intent.recommendation)
            // These intents don't modify GameState directly
            is GameIntent.RequestEvaluation,
            is GameIntent.DismissEvaluation -> state
        }
    }

    private fun rollDice(state: GameState): GameState {
        if (!state.rollCount.canRoll()) return state

        val newDice = state.dice.mapIndexed { index, die ->
            if (state.lockedDice[index]) die else Random.nextInt(1, 7)
        }

        return state.copy(
            dice = newDice,
            rollCount = state.rollCount.next()
        )
    }

    private fun toggleDiceLock(state: GameState, index: Int): GameState {
        if (index !in 0..4) return state
        if (state.rollCount == RollCount.ZERO) return state

        val newLockedDice = state.lockedDice.toMutableList().apply {
            this[index] = !this[index]
        }

        return state.copy(lockedDice = newLockedDice)
    }

    private fun applyDiceLock(state: GameState, locks: List<Boolean>): GameState {
        if (locks.size != 5) return state

        return state.copy(lockedDice = locks)
    }

    private fun incrementDice(state: GameState, index: Int): GameState {
        if (index !in 0..4) return state
        if (state.mode != GameMode.ANALYSIS) return state

        val newDice = state.dice.toMutableList().apply {
            this[index] = (this[index] % 6) + 1
        }

        return state.copy(dice = newDice)
    }

    private fun setRollCount(state: GameState, value: RollCount): GameState {
        if (state.mode != GameMode.ANALYSIS) return state

        return state.copy(rollCount = value)
    }

    private fun updateScore(state: GameState, intent: GameIntent.UpdateScore): GameState {
        if (state.mode != GameMode.ANALYSIS) return state

        return state.copy(
            scoreSheet = state.scoreSheet.set(intent.category, intent.value)
        )
    }

    private fun confirmScore(state: GameState, intent: GameIntent.ConfirmScore): GameState {
        val category = intent.category
        if (state.scoreSheet.get(category) != null) return state
        if (state.rollCount == RollCount.ZERO) return state

        val score = ScoreCalculator.calculateCategoryScore(category, state.dice)
        val newScoreSheet = state.scoreSheet.set(category, score)

        return if (state.mode == GameMode.PLAY) {
            state.copy(
                scoreSheet = newScoreSheet,
                rollCount = RollCount.ZERO,
                dice = listOf(1, 1, 1, 1, 1),
                lockedDice = listOf(false, false, false, false, false)
            )
        } else {
            state.copy(scoreSheet = newScoreSheet)
        }
    }

    private fun changeMode(state: GameState, mode: GameMode): GameState {
        return state.copy(mode = mode)
    }

    private fun resetGame(state: GameState): GameState {
        return GameState.INITIAL.copy(mode = state.mode)
    }

    private fun applyRecommendation(state: GameState, recommendation: Recommendation): GameState {
        return when (recommendation) {
            is Recommendation.Dice -> {
                val diceToHoldSet = recommendation.diceToHold.toSet()
                val newLockedDice = state.dice.map { die -> die in diceToHoldSet }
                state.copy(lockedDice = newLockedDice)
            }
            is Recommendation.CategoryChoice -> {
                val score = ScoreCalculator.calculateCategoryScore(recommendation.category, state.dice)
                val newScoreSheet = state.scoreSheet.set(recommendation.category, score)

                if (state.mode == GameMode.PLAY) {
                    state.copy(
                        scoreSheet = newScoreSheet,
                        rollCount = RollCount.ZERO,
                        dice = listOf(1, 1, 1, 1, 1),
                        lockedDice = listOf(false, false, false, false, false)
                    )
                } else {
                    state.copy(scoreSheet = newScoreSheet)
                }
            }
        }
    }
}
