package com.xuwanjin.datastore

import android.text.format.DateFormat
import com.xuwanjin.datastore.AppConstant.CURRENCY_DATA_EXPIRED_TIME
import java.util.Calendar

object AppUtils {

    /**
     * timestamp convert to formatted date
     */
    fun timestampToLocalDate(timestampInSeconds: Long, format: String = "dd-MM-yyyy"): String {
        return try {
            val calendar = Calendar.getInstance()
            calendar.timeInMillis = timestampInSeconds * 1000
            DateFormat.format(format, calendar).toString()
        } catch (exception: Exception) {
            exception.printStackTrace()
            ""
        }
    }

    fun isCurrencyDataOutdated(timeoutInSeconds: Long = CURRENCY_DATA_EXPIRED_TIME): Boolean {
        val lastUpdated = DataStoreUtils.getLastUpdatedTime()
        val currentTime = System.currentTimeMillis().div(1000)
        return (currentTime - lastUpdated) > timeoutInSeconds
    }
}
