package com.example.yachtevaluator.domain.calculator

import com.example.yachtevaluator.domain.model.Category

object ScoreValidator {
    private val FULL_HOUSE_INVALID_VALUES = setOf(1, 2, 3, 4, 6, 29)

    fun isValidScore(category: Category, value: Int?): Boolean {
        if (value == null) return true
        if (value < 0) return false

        return when (category) {
            // Upper section: 0 to 5 * face value
            Category.ACE -> value in 0..5
            Category.DEUCE -> value in 0..10 && value % 2 == 0
            Category.TREY -> value in 0..15 && value % 3 == 0
            Category.FOUR -> value in 0..20 && value % 4 == 0
            Category.FIVE -> value in 0..25 && value % 5 == 0
            Category.SIX -> value in 0..30 && value % 6 == 0

            // Lower section
            Category.CHOICE -> value in 5..30  // Min: 5 ones, Max: 5 sixes
            Category.FOUR_OF_A_KIND -> value == 0 || value in 5..30
            Category.FULL_HOUSE -> value in 0..30 && value !in FULL_HOUSE_INVALID_VALUES
            Category.SMALL_STRAIGHT -> value == 0 || value == 15
            Category.BIG_STRAIGHT -> value == 0 || value == 30
            Category.YACHT -> value == 0 || value == 50
        }
    }

    fun getPossibleScores(category: Category): List<Int> {
        return when (category) {
            Category.ACE -> (0..5).toList()
            Category.DEUCE -> (0..5).map { it * 2 }
            Category.TREY -> (0..5).map { it * 3 }
            Category.FOUR -> (0..5).map { it * 4 }
            Category.FIVE -> (0..5).map { it * 5 }
            Category.SIX -> (0..5).map { it * 6 }
            Category.CHOICE -> (5..30).toList()
            Category.FOUR_OF_A_KIND -> listOf(0) + (5..30).toList()
            Category.FULL_HOUSE -> listOf(0) + (5..30).toList()
            Category.SMALL_STRAIGHT -> listOf(0, 15)
            Category.BIG_STRAIGHT -> listOf(0, 30)
            Category.YACHT -> listOf(0, 50)
        }
    }
}
