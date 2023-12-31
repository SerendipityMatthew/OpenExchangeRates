package com.xuwanjin.coredata.remote

import com.xuwanjin.network.NetworkModule
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import javax.inject.Singleton

@Module(includes = [NetworkModule::class])
@InstallIn(SingletonComponent::class)
object CurrencyDataModule {
    @Singleton
    @Provides
    fun provideCurrencyDataApi(retrofit: Retrofit): CurrencyDataApi {
        return retrofit.create(CurrencyDataApi::class.java)
    }
}
