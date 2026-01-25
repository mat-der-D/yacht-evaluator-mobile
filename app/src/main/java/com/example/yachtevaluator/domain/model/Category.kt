package com.example.yachtevaluator.domain.model

import com.example.yachtevaluator.R

enum class Category(val isUpper: Boolean) {
    // Upper section
    ACE(true),
    DEUCE(true),
    TREY(true),
    FOUR(true),
    FIVE(true),
    SIX(true),

    // Lower section
    CHOICE(false),
    FOUR_OF_A_KIND(false),
    FULL_HOUSE(false),
    SMALL_STRAIGHT(false),
    BIG_STRAIGHT(false),
    YACHT(false);

    companion object {
        val upperCategories: List<Category> = entries.filter { it.isUpper }
        val lowerCategories: List<Category> = entries.filter { !it.isUpper }

        fun fromApiName(name: String): Category? = when (name) {
            "ace" -> ACE
            "deuce" -> DEUCE
            "trey" -> TREY
            "four" -> FOUR
            "five" -> FIVE
            "six" -> SIX
            "choice" -> CHOICE
            "fourOfAKind" -> FOUR_OF_A_KIND
            "fullHouse" -> FULL_HOUSE
            "smallStraight" -> SMALL_STRAIGHT
            "bigStraight" -> BIG_STRAIGHT
            "yacht" -> YACHT
            else -> null
        }
    }

    fun toApiName(): String = when (this) {
        ACE -> "ace"
        DEUCE -> "deuce"
        TREY -> "trey"
        FOUR -> "four"
        FIVE -> "five"
        SIX -> "six"
        CHOICE -> "choice"
        FOUR_OF_A_KIND -> "fourOfAKind"
        FULL_HOUSE -> "fullHouse"
        SMALL_STRAIGHT -> "smallStraight"
        BIG_STRAIGHT -> "bigStraight"
        YACHT -> "yacht"
    }

    fun getDisplayNameResId(): Int = when (this) {
        ACE -> R.string.category_ace
        DEUCE -> R.string.category_deuce
        TREY -> R.string.category_trey
        FOUR -> R.string.category_four
        FIVE -> R.string.category_five
        SIX -> R.string.category_six
        CHOICE -> R.string.category_choice
        FOUR_OF_A_KIND -> R.string.category_four_of_a_kind
        FULL_HOUSE -> R.string.category_full_house
        SMALL_STRAIGHT -> R.string.category_small_straight
        BIG_STRAIGHT -> R.string.category_big_straight
        YACHT -> R.string.category_yacht
    }
}
