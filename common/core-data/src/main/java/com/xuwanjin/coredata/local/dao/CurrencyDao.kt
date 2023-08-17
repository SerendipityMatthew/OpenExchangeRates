package com.xuwanjin.coredata.local.dao

import androidx.room.Dao
import androidx.room.Query
import com.xuwanjin.model.CurrencyData
import kotlinx.coroutines.flow.Flow

/**
 * [Room] DAO for [CurrencyData] related operations.
 */
@Dao
abstract class CurrencyDao : BaseDao<CurrencyData> {
    @Query("SELECT * FROM CurrencyData WHERE base = :baseCurrency")
    abstract fun getCurrencyBaseInUSD(baseCurrency: String): Flow<CurrencyData?>

    @Query("SELECT * FROM CurrencyData")
    abstract fun getCurrencyAll(): Flow<List<CurrencyData>>

    @Query("DELETE FROM CurrencyData")
    abstract override suspend fun deleteAll(): Int
}
