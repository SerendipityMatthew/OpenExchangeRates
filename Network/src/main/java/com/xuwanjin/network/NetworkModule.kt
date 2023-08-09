package com.xuwanjin.network

import android.content.Context
import com.google.gson.Gson
import com.skydoves.sandwich.adapters.ApiResponseCallAdapterFactory
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object NetworkModule {
    @Singleton
    @Provides
    fun provideRetrofit(okHttpClient: OkHttpClient): Retrofit {
        val hostUrl = NetworkConfig.BASE_HOST_URL
        return Retrofit.Builder().apply {
            client(okHttpClient)
            addConverterFactory(GsonConverterFactory.create())
            addCallAdapterFactory(ApiResponseCallAdapterFactory.create())
            baseUrl(hostUrl)
        }.build()
    }

    @Singleton
    @Provides
    fun provideOkHttp(requestInterceptor: RequestInterceptor): OkHttpClient {
        return OkHttpClient.Builder().apply {
            connectTimeout(NetworkConfig.HTTP_CONNECT_TIMEOUT, TimeUnit.SECONDS)
            readTimeout(NetworkConfig.HTTP_READ_TIMEOUT, TimeUnit.SECONDS)
            writeTimeout(NetworkConfig.HTTP_WRITE_TIMEOUT, TimeUnit.SECONDS)
            addInterceptor(requestInterceptor)
            if (BuildConfig.DEBUG) {
                addNetworkInterceptor(
                    HttpLoggingInterceptor(HttpLogger()).apply {
                        setLevel(HttpLoggingInterceptor.Level.BODY)
                    }
                )
            }
        }.build()
    }

    @Provides
    fun provideRequestInterceptor(): RequestInterceptor {
        return RequestInterceptor()
    }

    @Provides
    @Singleton
    fun provideGson(): Gson {
        return Gson()
    }

    @Provides
    @Singleton
    fun provideNetworkConnectivity(@ApplicationContext context: Context): NetworkConnectivity {
        return NetworkConnectivity(context)
    }
}