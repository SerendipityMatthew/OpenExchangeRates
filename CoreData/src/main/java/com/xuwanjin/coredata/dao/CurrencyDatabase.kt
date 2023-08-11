package com.xuwanjin.coredata.dao

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.xuwanjin.coredata.CurrencyData
import com.xuwanjin.coredata.dao.converter.StringMapConverter


@Database(
    entities = [
        CurrencyData::class,
    ],
    version = 1,
    exportSchema = false
)

@TypeConverters(StringMapConverter::class)
abstract class CurrencyDatabase : RoomDatabase() {
    abstract fun currencyDao(): CurrencyDao
}
