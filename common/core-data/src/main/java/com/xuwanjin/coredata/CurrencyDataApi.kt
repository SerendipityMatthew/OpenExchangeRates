package com.xuwanjin.coredata

import com.skydoves.sandwich.ApiResponse
import retrofit2.http.GET
import retrofit2.http.Query

interface CurrencyDataApi {
    @GET("latest.json")
    suspend fun getLatestCurrency(@Query("app_id") appId: String): ApiResponse<com.xuwanjin.model.CurrencyData>
}