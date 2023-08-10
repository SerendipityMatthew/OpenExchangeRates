package com.xuwanjin.coredata

import com.google.gson.annotations.SerializedName

data class CurrencyData(
    @SerializedName(value = "disclaimer") val disclaimer: String = "",
    @SerializedName(value = "license") val license: String = "",
    @SerializedName(value = "timestamp") val timestamp: Long = 0,
    @SerializedName(value = "base") val base: String = "",
    @SerializedName(value = "rates") val ratesMap: Map<String, Float> = mutableMapOf(),
)