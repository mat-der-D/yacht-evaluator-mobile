package com.example.yachtevaluator.data.repository

import com.example.yachtevaluator.data.api.EvaluationApi
import com.example.yachtevaluator.data.dto.EvaluateRequest
import com.example.yachtevaluator.data.dto.ScoreSheetDto
import com.example.yachtevaluator.domain.model.Category
import com.example.yachtevaluator.domain.model.RollCount
import com.example.yachtevaluator.domain.model.ScoreSheet
import com.example.yachtevaluator.presentation.state.Recommendation
import javax.inject.Inject

interface EvaluationRepository {
    suspend fun evaluate(
        scoreSheet: ScoreSheet,
        dice: List<Int>,
        rollCount: RollCount
    ): Result<List<Recommendation>>
}

class EvaluationRepositoryImpl @Inject constructor(
    private val api: EvaluationApi
) : EvaluationRepository {

    override suspend fun evaluate(
        scoreSheet: ScoreSheet,
        dice: List<Int>,
        rollCount: RollCount
    ): Result<List<Recommendation>> {
        return try {
            val request = EvaluateRequest(
                scoreSheet = scoreSheet.toDto(),
                dice = dice,
                rollCount = rollCount.value
            )

            val response = api.evaluate(request)
            val recommendations = response.data.mapNotNull { dto ->
                when (dto.choiceType) {
                    "dice" -> {
                        dto.diceToHold?.let { diceToHold ->
                            Recommendation.Dice(
                                diceToHold = diceToHold,
                                expectedValue = dto.expectedValue
                            )
                        }
                    }
                    "category" -> {
                        dto.category?.let { categoryName ->
                            Category.fromApiName(categoryName)?.let { category ->
                                Recommendation.CategoryChoice(
                                    category = category,
                                    expectedValue = dto.expectedValue
                                )
                            }
                        }
                    }
                    else -> null
                }
            }

            Result.success(recommendations)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    private fun ScoreSheet.toDto(): ScoreSheetDto = ScoreSheetDto(
        ace = ace,
        deuce = deuce,
        trey = trey,
        four = four,
        five = five,
        six = six,
        choice = choice,
        fourOfAKind = fourOfAKind,
        fullHouse = fullHouse,
        smallStraight = smallStraight,
        bigStraight = bigStraight,
        yacht = yacht
    )
}
