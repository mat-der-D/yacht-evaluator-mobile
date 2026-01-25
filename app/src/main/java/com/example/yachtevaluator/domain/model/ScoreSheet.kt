package com.example.yachtevaluator.domain.model

data class ScoreSheet(
    val ace: Int? = null,
    val deuce: Int? = null,
    val trey: Int? = null,
    val four: Int? = null,
    val five: Int? = null,
    val six: Int? = null,
    val choice: Int? = null,
    val fourOfAKind: Int? = null,
    val fullHouse: Int? = null,
    val smallStraight: Int? = null,
    val bigStraight: Int? = null,
    val yacht: Int? = null
) {
    fun get(category: Category): Int? = when (category) {
        Category.ACE -> ace
        Category.DEUCE -> deuce
        Category.TREY -> trey
        Category.FOUR -> four
        Category.FIVE -> five
        Category.SIX -> six
        Category.CHOICE -> choice
        Category.FOUR_OF_A_KIND -> fourOfAKind
        Category.FULL_HOUSE -> fullHouse
        Category.SMALL_STRAIGHT -> smallStraight
        Category.BIG_STRAIGHT -> bigStraight
        Category.YACHT -> yacht
    }

    fun set(category: Category, value: Int?): ScoreSheet = when (category) {
        Category.ACE -> copy(ace = value)
        Category.DEUCE -> copy(deuce = value)
        Category.TREY -> copy(trey = value)
        Category.FOUR -> copy(four = value)
        Category.FIVE -> copy(five = value)
        Category.SIX -> copy(six = value)
        Category.CHOICE -> copy(choice = value)
        Category.FOUR_OF_A_KIND -> copy(fourOfAKind = value)
        Category.FULL_HOUSE -> copy(fullHouse = value)
        Category.SMALL_STRAIGHT -> copy(smallStraight = value)
        Category.BIG_STRAIGHT -> copy(bigStraight = value)
        Category.YACHT -> copy(yacht = value)
    }

    val upperTotal: Int
        get() = listOfNotNull(ace, deuce, trey, four, five, six).sum()

    val bonus: Int
        get() = if (upperTotal >= 63) 35 else 0

    val lowerTotal: Int
        get() = listOfNotNull(choice, fourOfAKind, fullHouse, smallStraight, bigStraight, yacht).sum()

    val finalTotal: Int
        get() = upperTotal + bonus + lowerTotal

    val isComplete: Boolean
        get() = ace != null && deuce != null && trey != null &&
                four != null && five != null && six != null &&
                choice != null && fourOfAKind != null && fullHouse != null &&
                smallStraight != null && bigStraight != null && yacht != null

    fun availableCategories(): List<Category> = Category.entries.filter { get(it) == null }
}
