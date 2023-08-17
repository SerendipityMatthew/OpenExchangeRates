package com.xuwanjin.datastore

import com.xuwanjin.datastore.AppConstant.CURRENCY_DATA_EXPIRED_TIME
import java.lang.IllegalArgumentException
import java.text.SimpleDateFormat
import java.util.Locale

object AppUtils {

    /**
     * timestamp convert to formatted date
     */
    fun timestampToLocalDate(
        timestampInSeconds: Long,
    ): String {
        val simpleDateFormat =
            SimpleDateFormat("dd MMMM yyyy, HH:mm:ss", Locale.getDefault())
        return try {
            simpleDateFormat.format(timestampInSeconds * 1000L)
        } catch (exception: IllegalArgumentException) {
            exception.printStackTrace()
            ""
        } catch (e: Exception) {
            e.printStackTrace()
            ""
        }
    }

    fun isCurrencyDataOutdated(timeoutInSeconds: Long = CURRENCY_DATA_EXPIRED_TIME): Boolean {
        val lastUpdated = DataStoreUtils.getLastUpdatedTime()
        val currentTime = System.currentTimeMillis().div(1000)
        return (currentTime - lastUpdated) > timeoutInSeconds
    }
}
