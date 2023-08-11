package com.xuwanjin.coredata

import android.app.Application
import androidx.room.Room
import com.xuwanjin.coredata.dao.CurrencyDatabase
import com.xuwanjin.coredata.dao.CurrencyStore
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
            // This is not recommended for normal apps, but the goal of this sample isn't to
            // showcase all of Room.
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