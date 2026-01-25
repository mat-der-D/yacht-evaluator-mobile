package com.example.yachtevaluator.domain.calculator

import com.example.yachtevaluator.domain.model.Category

object ScoreCalculator {

    fun calculateCategoryScore(category: Category, dice: List<Int>): Int {
        require(dice.size == 5) { "Dice must have exactly 5 elements" }
        require(dice.all { it in 1..6 }) { "Each die must be between 1 and 6" }

        return when (category) {
            Category.ACE -> calculateUpperSection(dice, 1)
            Category.DEUCE -> calculateUpperSection(dice, 2)
            Category.TREY -> calculateUpperSection(dice, 3)
            Category.FOUR -> calculateUpperSection(dice, 4)
            Category.FIVE -> calculateUpperSection(dice, 5)
            Category.SIX -> calculateUpperSection(dice, 6)
            Category.CHOICE -> calculateChoice(dice)
            Category.FOUR_OF_A_KIND -> calculateFourOfAKind(dice)
            Category.FULL_HOUSE -> calculateFullHouse(dice)
            Category.SMALL_STRAIGHT -> calculateSmallStraight(dice)
            Category.BIG_STRAIGHT -> calculateBigStraight(dice)
            Category.YACHT -> calculateYacht(dice)
        }
    }

    private fun getCounts(dice: List<Int>): IntArray {
        val counts = IntArray(6)
        for (face in dice) {
            counts[face - 1]++
        }
        return counts
    }

    private fun diceSum(dice: List<Int>): Int = dice.sum()

    private fun getMaxConsecutive(counts: IntArray): Int {
        var maxConsec = 0
        var currentConsec = 0

        for (count in counts) {
            if (count > 0) {
                currentConsec++
                maxConsec = maxOf(maxConsec, currentConsec)
            } else {
                currentConsec = 0
            }
        }

        return maxConsec
    }

    private fun calculateUpperSection(dice: List<Int>, targetFace: Int): Int {
        val counts = getCounts(dice)
        return counts[targetFace - 1] * targetFace
    }

    private fun calculateChoice(dice: List<Int>): Int = diceSum(dice)

    private fun calculateFourOfAKind(dice: List<Int>): Int {
        val counts = getCounts(dice)
        return if (counts.any { it >= 4 }) diceSum(dice) else 0
    }

    private fun calculateFullHouse(dice: List<Int>): Int {
        val counts = getCounts(dice)
        // Full house: no dice appears exactly once (must be 5-of-a-kind, 3+2, or 2+2+1 doesn't qualify)
        // Valid patterns: [5,0,0,0,0,0], [3,2,0,0,0,0], etc.
        val noSingleDice = counts.none { it == 1 }
        return if (noSingleDice) diceSum(dice) else 0
    }

    private fun calculateSmallStraight(dice: List<Int>): Int {
        val maxConsec = getMaxConsecutive(getCounts(dice))
        return if (maxConsec >= 4) 15 else 0
    }

    private fun calculateBigStraight(dice: List<Int>): Int {
        val maxConsec = getMaxConsecutive(getCounts(dice))
        return if (maxConsec == 5) 30 else 0
    }

    private fun calculateYacht(dice: List<Int>): Int {
        val counts = getCounts(dice)
        return if (counts.any { it == 5 }) 50 else 0
    }
}
