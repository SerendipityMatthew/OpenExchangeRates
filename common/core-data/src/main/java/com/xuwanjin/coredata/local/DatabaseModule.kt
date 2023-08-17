package com.xuwanjin.coredata.local

import android.app.Application
import androidx.room.Room
import com.xuwanjin.coredata.local.dao.CurrencyDatabase
import com.xuwanjin.coredata.local.dao.CurrencyStore
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule {
    @Provides
    fun provideDatabaseModule(application: Application): CurrencyDatabase {
        return Room.databaseBuilder(application, CurrencyDatabase::class.java, "CurrencyData.db")
            .fallbackToDestructiveMigration()
            .build()
    }

    @Provides
    fun currencyDataStore(application: Application): CurrencyStore {
        return CurrencyStore(
            currencyDao = provideDatabaseModule(application).currencyDao(),
        )
    }
}
