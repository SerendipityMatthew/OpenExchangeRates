package com.xuwanjin.coredata.dao.converter

import androidx.room.TypeConverter
import com.google.gson.Gson
import com.google.gson.JsonElement
import com.google.gson.reflect.TypeToken
import java.lang.reflect.Type

object StringMapConverter {
    @TypeConverter
    @JvmStatic
    fun fromString(value: String?): Map<String, Float> {
        val mapType: Type = object : TypeToken<Map<String, Float>?>() {}.type
        return Gson().fromJson(value, mapType)
    }

    @TypeConverter
    @JvmStatic
    fun fromStringMap(map: Map<String, Float>?): String {
        val gson = Gson()
        return gson.toJson(map)
    }
}