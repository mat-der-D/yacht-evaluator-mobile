package net.smoothpudding.yachtevaluator.data.api

import net.smoothpudding.yachtevaluator.data.dto.EvaluateResponse
import okhttp3.RequestBody
import retrofit2.http.Body
import retrofit2.http.POST

interface EvaluationApi {

    @POST("/api/v1/evaluate")
    suspend fun evaluate(@Body requestBody: RequestBody): EvaluateResponse
}
