package com.xuwanjin.datastore

import android.text.format.DateFormat
import android.util.Log
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
}