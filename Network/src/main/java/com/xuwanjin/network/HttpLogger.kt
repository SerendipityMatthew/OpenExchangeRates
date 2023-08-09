package com.xuwanjin.network

import android.util.Log
import okhttp3.logging.HttpLoggingInterceptor

class HttpLogger: HttpLoggingInterceptor.Logger {
    override fun log(message: String) {
        Log.d("OER:HttpLog", message)
    }
}
