package com.example.yachtevaluator.data.api

import com.example.yachtevaluator.data.dto.EvaluateRequest
import com.example.yachtevaluator.data.dto.EvaluateResponse
import retrofit2.http.Body
import retrofit2.http.POST

interface EvaluationApi {

    @POST("/api/v1/evaluate")
    suspend fun evaluate(@Body request: EvaluateRequest): EvaluateResponse
}
