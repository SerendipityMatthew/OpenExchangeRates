package com.xuwanjin.coredata.remote

import com.skydoves.sandwich.ApiResponse
import com.xuwanjin.model.CurrencyData
import retrofit2.http.GET
import retrofit2.http.Query

interface CurrencyDataApi {
    @GET("latest.json")
    suspend fun getLatestCurrency(@Query("app_id") appId: String): ApiResponse<CurrencyData>
}