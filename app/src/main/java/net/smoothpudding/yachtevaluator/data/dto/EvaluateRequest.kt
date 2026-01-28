package net.smoothpudding.yachtevaluator.data.dto

import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class EvaluateRequest(
    val scoreSheet: ScoreSheetDto,
    val dice: List<Int>,
    val rollCount: Int  // Must be 1, 2, or 3 (never 0)
)

@JsonClass(generateAdapter = true)
data class ScoreSheetDto(
    val ace: Int?,
    val deuce: Int?,
    val trey: Int?,
    val four: Int?,
    val five: Int?,
    val six: Int?,
    val choice: Int?,
    val fourOfAKind: Int?,
    val fullHouse: Int?,
    val smallStraight: Int?,
    val bigStraight: Int?,
    val yacht: Int?
)
