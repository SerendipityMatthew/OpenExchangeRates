package com.xuwanjin.openexchangerates

import android.app.Application
import com.xuwanjin.datastore.DataStoreUtils
import dagger.hilt.android.HiltAndroidApp

@HiltAndroidApp
class OERApplication:Application() {
    override fun onCreate() {
        super.onCreate()
        DataStoreUtils.init(this)
    }
}