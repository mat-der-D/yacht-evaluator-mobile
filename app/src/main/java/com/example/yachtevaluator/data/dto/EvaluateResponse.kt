package com.example.yachtevaluator.data.dto

import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class EvaluateResponse(
    val data: List<RecommendationDto>
)

@JsonClass(generateAdapter = true)
data class RecommendationDto(
    val choiceType: String,
    val diceToHold: List<Int>? = null,
    val category: String? = null,
    val expectedValue: Double
)
