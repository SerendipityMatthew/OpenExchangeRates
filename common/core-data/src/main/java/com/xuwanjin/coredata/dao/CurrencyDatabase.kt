package com.xuwanjin.coredata.dao

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.xuwanjin.model.CurrencyData
import com.xuwanjin.model.converter.StringMapConverter


@Database(
    entities = [
        com.xuwanjin.model.CurrencyData::class,
    ],
    version = 1,
    exportSchema = false
)

@TypeConverters(com.xuwanjin.model.converter.StringMapConverter::class)
abstract class CurrencyDatabase : RoomDatabase() {
    abstract fun currencyDao(): CurrencyDao
}
