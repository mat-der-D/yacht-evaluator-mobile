package net.smoothpudding.yachtevaluator.domain.model

enum class RollCount(val value: Int) {
    ZERO(0),
    ONE(1),
    TWO(2),
    THREE(3);

    fun remaining(): Int = 3 - value

    fun canRoll(): Boolean = value < 3

    fun next(): RollCount = when (this) {
        ZERO -> ONE
        ONE -> TWO
        TWO -> THREE
        THREE -> THREE
    }

    companion object {
        fun fromInt(value: Int): RollCount = when (value) {
            0 -> ZERO
            1 -> ONE
            2 -> TWO
            3 -> THREE
            else -> throw IllegalArgumentException("Invalid roll count: $value")
        }
    }
}
