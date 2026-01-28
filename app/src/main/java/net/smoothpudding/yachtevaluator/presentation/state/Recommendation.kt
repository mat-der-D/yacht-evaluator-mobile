package net.smoothpudding.yachtevaluator.presentation.state

import net.smoothpudding.yachtevaluator.domain.model.Category

sealed interface Recommendation {
    val expectedValue: Double

    data class Dice(
        val diceToHold: List<Int>,
        override val expectedValue: Double
    ) : Recommendation

    data class CategoryChoice(
        val category: Category,
        override val expectedValue: Double
    ) : Recommendation
}
