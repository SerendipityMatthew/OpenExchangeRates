package com.xuwanjin.coredata

import com.skydoves.sandwich.ApiResponse
import com.xuwanjin.datastore.AppConstant
import com.xuwanjin.model.CurrencyData
import javax.inject.Inject

class CurrencyDataRepo @Inject constructor(
    private val currencyDataAPi:CurrencyDataApi
){
    suspend fun getLatestCurrency(appId: String = AppConstant.OPEN_EXCHANGE_APP_ID): ApiResponse<CurrencyData> {
        return currencyDataAPi.getLatestCurrency(appId = appId)
    }
}