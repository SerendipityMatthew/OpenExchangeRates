package com.xuwanjin.coredata.dao

import com.xuwanjin.coredata.CurrencyData
import kotlinx.coroutines.flow.Flow

class CurrencyStore(
    private val currencyDao: CurrencyDao,
) {
    suspend fun getCurrencyBaseInUSD(): Flow<CurrencyData> {
        return currencyDao.getCurrencyBaseInUSD(baseCurrency = "USD")
    }

    suspend fun updateCurrencyData(currencyData: CurrencyData) = currencyDao.insert(currencyData)

}