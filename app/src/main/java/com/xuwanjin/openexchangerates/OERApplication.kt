package com.xuwanjin.openexchangerates

import android.app.Application
import com.xuwanjin.datastore.DataStoreUtils

class OERApplication:Application() {
    override fun onCreate() {
        super.onCreate()
        DataStoreUtils.init(this)
    }
}