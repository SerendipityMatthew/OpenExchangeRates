package com.xuwanjin.coredata.local.dao

import com.xuwanjin.model.CurrencyData
import kotlinx.coroutines.flow.Flow

class CurrencyStore(
    private val currencyDao: CurrencyDao,
) {
    suspend fun getCurrencyInDB(baseCurrency: String = "USD"): Flow<CurrencyData?> {
        return currencyDao.getCurrencyBaseInUSD(baseCurrency = baseCurrency)
    }

    suspend fun updateCurrencyData(currencyData: CurrencyData) = currencyDao.insert(currencyData)
}
