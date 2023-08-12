package com.xuwanjin.coredata.dao

import androidx.room.Dao
import androidx.room.Query
import com.xuwanjin.coredata.CurrencyData
import kotlinx.coroutines.flow.Flow


/**
 * [Room] DAO for [CurrencyData] related operations.
 */
@Dao
abstract class CurrencyDao : BaseDao<CurrencyData> {
    @Query("SELECT * FROM CurrencyData WHERE base = :baseCurrency")
    abstract fun getCurrencyBaseInUSD(baseCurrency: String): Flow<CurrencyData?>

}