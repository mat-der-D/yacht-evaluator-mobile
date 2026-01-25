package com.example.yachtevaluator.domain.model

enum class Category(val displayName: String, val isUpper: Boolean) {
    // Upper section
    ACE("Ace", true),
    DEUCE("Deuce", true),
    TREY("Trey", true),
    FOUR("Four", true),
    FIVE("Five", true),
    SIX("Six", true),

    // Lower section
    CHOICE("Choice", false),
    FOUR_OF_A_KIND("Four of a Kind", false),
    FULL_HOUSE("Full House", false),
    SMALL_STRAIGHT("S. Straight", false),
    BIG_STRAIGHT("B. Straight", false),
    YACHT("Yacht", false);

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
}
