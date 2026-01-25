package com.example.yachtevaluator.domain.model

data class GameState(
    val mode: GameMode = GameMode.PLAY,
    val rollCount: RollCount = RollCount.ZERO,
    val dice: List<Int> = listOf(1, 1, 1, 1, 1),
    val lockedDice: List<Boolean> = listOf(false, false, false, false, false),
    val scoreSheet: ScoreSheet = ScoreSheet()
) {
    init {
        require(dice.size == 5) { "Dice must have exactly 5 elements" }
        require(dice.all { it in 1..6 }) { "Each die must be between 1 and 6" }
        require(lockedDice.size == 5) { "LockedDice must have exactly 5 elements" }
    }

    companion object {
        val INITIAL = GameState()
    }
}
