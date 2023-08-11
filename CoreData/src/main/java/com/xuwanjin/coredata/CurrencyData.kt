package com.xuwanjin.coredata

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey
import androidx.room.TypeConverters
import com.google.gson.annotations.SerializedName
import com.xuwanjin.coredata.dao.converter.StringMapConverter
import javax.annotation.concurrent.Immutable

@Entity(
    tableName = "CurrencyData",
    indices = [
        Index("base", unique = true)
    ]
)
@Immutable
data class CurrencyData(
    @PrimaryKey @ColumnInfo(name = "base")   @SerializedName(value = "base") val base: String = "",
    @ColumnInfo(name = "disclaimer")  @SerializedName(value = "disclaimer") val disclaimer: String = "",
    @ColumnInfo(name = "license") @SerializedName(value = "license") val license: String = "",
    @ColumnInfo(name = "timestamp")  @SerializedName(value = "timestamp") val timestamp: Long = 0,
    @TypeConverters(StringMapConverter::class)
    @ColumnInfo(name = "rates") @SerializedName(value = "rates") val ratesMap: Map<String, Float> = mutableMapOf(),
){
    override fun toString(): String {
        return "CurrencyData(base='$base', disclaimer='$disclaimer', license='$license', timestamp=$timestamp, ratesMap=$ratesMap)"
    }
}