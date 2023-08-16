package com.xuwanjin.coredata

import com.skydoves.sandwich.ApiResponse
import com.skydoves.sandwich.suspendOnSuccess
import com.xuwanjin.coredata.local.dao.CurrencyStore
import com.xuwanjin.coredata.remote.CurrencyDataApi
import com.xuwanjin.datastore.AppConstant
import com.xuwanjin.datastore.DataStoreUtils
import com.xuwanjin.model.CurrencyData
import kotlinx.coroutines.DelicateCoroutinesApi
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.async
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class CurrencyDataRepo @Inject constructor(
    private val currencyDataAPi: CurrencyDataApi,
    private val currencyStore: CurrencyStore,
) {
    @OptIn(DelicateCoroutinesApi::class)
    suspend fun getLatestCurrency(appId: String = AppConstant.OPEN_EXCHANGE_APP_ID): ApiResponse<CurrencyData> {
        val response = GlobalScope.async {
            currencyDataAPi.getLatestCurrency(appId = appId)
        }
        return response.await().apply {
            suspendOnSuccess {
                saveDataAndRecordTimestamp(this.data)
            }
        }
    }

    /**
     *  we should save the time that data have been fetched from server
     *  because this data would not updated every 30 minutes
     */
    private suspend fun saveDataAndRecordTimestamp(data: CurrencyData) {
        val result = updateCurrencyData(data)
        if (result > 0) {
            DataStoreUtils.setCurrencyUpdatedTime(System.currentTimeMillis().div(1000))
        }
    }

    suspend fun getCurrencyInDB(): Flow<CurrencyData?> {
        return currencyStore.getCurrencyInDB(baseCurrency = "USD")
    }

    suspend fun updateCurrencyData(currencyData: CurrencyData) =
        currencyStore.updateCurrencyData(currencyData)
}