package com.xuwanjin.coredata

import com.skydoves.sandwich.ApiResponse
import com.xuwanjin.datastore.AppConstant
import javax.inject.Inject

class CurrencyDataRepo @Inject constructor(
    private val currencyDataAPi:CurrencyDataApi
){
    suspend fun getLatestCurrency(appId: String = AppConstant.OPEN_EXCHANGE_APP_ID): ApiResponse<com.xuwanjin.model.CurrencyData> {
        return currencyDataAPi.getLatestCurrency(appId = appId)
    }
}