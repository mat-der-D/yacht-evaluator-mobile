package com.example.yachtevaluator.data.repository

import com.example.yachtevaluator.data.api.EvaluationApi
import com.example.yachtevaluator.data.error.ErrorHandler
import com.example.yachtevaluator.domain.model.Category
import com.example.yachtevaluator.domain.model.RollCount
import com.example.yachtevaluator.domain.model.ScoreSheet
import com.example.yachtevaluator.presentation.state.Recommendation
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.RequestBody.Companion.toRequestBody
import org.json.JSONObject
import javax.inject.Inject

interface EvaluationRepository {
    suspend fun evaluate(
        scoreSheet: ScoreSheet,
        dice: List<Int>,
        rollCount: RollCount
    ): Result<List<Recommendation>>
}

class EvaluationRepositoryImpl @Inject constructor(
    private val api: EvaluationApi,
    private val errorHandler: ErrorHandler
) : EvaluationRepository {

    override suspend fun evaluate(
        scoreSheet: ScoreSheet,
        dice: List<Int>,
        rollCount: RollCount
    ): Result<List<Recommendation>> {
        return try {
            // Manually build JSON to include null values
            val scoreSheetJson = JSONObject().apply {
                put("ace", scoreSheet.ace ?: JSONObject.NULL)
                put("deuce", scoreSheet.deuce ?: JSONObject.NULL)
                put("trey", scoreSheet.trey ?: JSONObject.NULL)
                put("four", scoreSheet.four ?: JSONObject.NULL)
                put("five", scoreSheet.five ?: JSONObject.NULL)
                put("six", scoreSheet.six ?: JSONObject.NULL)
                put("choice", scoreSheet.choice ?: JSONObject.NULL)
                put("fourOfAKind", scoreSheet.fourOfAKind ?: JSONObject.NULL)
                put("fullHouse", scoreSheet.fullHouse ?: JSONObject.NULL)
                put("smallStraight", scoreSheet.smallStraight ?: JSONObject.NULL)
                put("bigStraight", scoreSheet.bigStraight ?: JSONObject.NULL)
                put("yacht", scoreSheet.yacht ?: JSONObject.NULL)
            }

            val diceArray = org.json.JSONArray(dice)

            val requestJson = JSONObject().apply {
                put("scoreSheet", scoreSheetJson)
                put("dice", diceArray)
                put("rollCount", rollCount.value)
            }

            val requestBody = requestJson.toString()
                .toRequestBody("application/json".toMediaType())

            val response = api.evaluate(requestBody)
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
            val errorMessage = errorHandler.getErrorMessage(e)
            Result.failure(Exception(errorMessage))
        }
    }
}
