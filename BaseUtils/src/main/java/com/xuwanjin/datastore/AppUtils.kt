package com.xuwanjin.datastore

import android.text.format.DateFormat
import android.util.Log
import java.util.Calendar


object AppUtils {
    fun timestampToLocalDate(timestampInSeconds: Long): String {
        return try {
            Log.d("Matthew", "timestampToLocalDate: timestampInSeconds = $timestampInSeconds")
            val calendar = Calendar.getInstance()
            calendar.timeInMillis = timestampInSeconds * 1000
            DateFormat.format("dd-MM-yyyy", calendar).toString()
        } catch (exception: Exception) {
            exception.printStackTrace()
            ""
        }
    }
}