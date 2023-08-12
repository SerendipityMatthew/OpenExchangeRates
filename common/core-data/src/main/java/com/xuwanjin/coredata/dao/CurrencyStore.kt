package com.xuwanjin.coredata.dao

import com.xuwanjin.model.CurrencyData
import kotlinx.coroutines.flow.Flow

class CurrencyStore(
    private val currencyDao: CurrencyDao,
) {
    suspend fun getCurrencyBaseInUSD(): Flow<com.xuwanjin.model.CurrencyData?> {
        return currencyDao.getCurrencyBaseInUSD(baseCurrency = "USD")
    }

    suspend fun updateCurrencyData(currencyData: com.xuwanjin.model.CurrencyData) = currencyDao.insert(currencyData)

}