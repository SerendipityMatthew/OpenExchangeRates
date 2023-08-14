package com.xuwanjin.coredata

import android.content.Context
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import androidx.test.runner.AndroidJUnit4
import com.xuwanjin.coredata.dao.CurrencyDao
import com.xuwanjin.coredata.dao.CurrencyDatabase
import com.xuwanjin.model.CurrencyData
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import java.io.IOException

@RunWith(AndroidJUnit4::class)
class CurrencyDaoTest {
    private lateinit var currencyDao: CurrencyDao
    private lateinit var database: CurrencyDatabase
    val fakeCurrencyDataBaseUSD = mutableMapOf(
        "AFN" to 84.566152f,
        "ALL" to 94.907192f,
        "AMD" to 389.872741f,
        "ANG" to 1.811557f,
        "AOA" to 825f,
        "ARS" to 286.8323f,
        "AUD" to 1.542663f,
        "AWG" to 1.8025f,
        "AZN" to 1.7f,
        "BAM" to 1.788242f,
        "BBD" to 2f,
        "BDT" to 110.021034f,
        "BGN" to 1.788f,
        "BHD" to 0.376857f,
        "BIF" to 2848.71829f,
        "BMD" to 1f,
        "BND" to 1.357026f,
        "BOB" to 6.946114f,
        "BRL" to 4.9522f,
        "BSD" to 1f,
        "BTC" to 0.000033917604f,
        "BTN" to 83.282719f,
        "BWP" to 13.564963f,
        "BYN" to 2.537225f,
        "BZD" to 2.026178f,
        "CAD" to 1.346628f,
        "CDF" to 2450f,
        "CHF" to 0.879307f,
        "CLF" to 0.031279f,
        "CLP" to 863.08f,
        "CNH" to 7.282046f,
        "CNY" to 7.2633f,
        "COP" to 4034.765f,
        "CRC" to 537.894081f,
        "CUC" to 1f,
        "CUP" to 25.75f,
        "CVE" to 100.822924f,
        "CZK" to 22.065312f,
        "DJF" to 178.972065f,
        "DKK" to 6.834752f,
        "DOP" to 57.044365f,
        "DZD" to 135.795457f,
        "EGP" to 30.901654f,
        "ERN" to 15f,
        "ETB" to 55.436505f,
        "EUR" to 0.917152f,
        "FJD" to 2.2518f,
        "FKP" to 0.789818f,
        "GBP" to 0.789818f,
        "GEL" to 2.608499f,
        "GGP" to 0.789818f,
        "GHS" to 11.232462f,
        "GIP" to 0.789818f,
        "GMD" to 60.383401f,
        "GNF" to 8639.39192f,
        "GTQ" to 7.906262f,
        "GYD" to 210.294379f,
        "HKD" to 7.820225f,
        "HNL" to 24.725245f,
        "HRK" to 6.909981f,
        "HTG" to 137.201478f,
        "HUF" to 353.234065f,
        "IDR" to 15346.920902f,
        "ILS" to 3.745549f,
        "IMP" to 0.789818f,
        "INR" to 83.159939f,
        "IQD" to 1316.544602f,
        "IRR" to 42250f,
        "ISK" to 132.16f,
        "JEP" to 0.789818f,
        "JMD" to 155.399106f,
        "JOD" to 0.708f,
        "JPY" to 145.44333333f,
        "KES" to 143.8f,
        "KGS" to 88.02f,
        "KHR" to 4155.535095f,
        "KMF" to 450.946458f,
        "KPW" to 900f,
        "KRW" to 1337.294509f,
        "KWD" to 0.307613f,
        "KYD" to 0.837619f,
        "KZT" to 448.997891f,
        "LAK" to 19538.230296f,
        "LBP" to 15088.086483f,
        "LKR" to 322.682666f,
        "LRD" to 186.663001f,
        "LSL" to 18.946035f,
        "LYD" to 4.820379f,
        "MAD" to 9.822575f,
        "MDL" to 17.574784f,
        "MGA" to 4530.483227f,
        "MKD" to 56.346079f,
        "MMK" to 2110.812822f,
        "MNT" to 3450f,
        "MOP" to 8.092787f,
        "MRU" to 38.090536f,
        "MUR" to 45.297059f,
        "MVR" to 15.383301f,
        "MWK" to 1090.885899f,
        "MXN" to 17.072f,
        "MYR" to 4.6125f,
        "MZN" to 63.830001f,
        "NAD" to 19.041754f,
        "NGN" to 773.252901f,
        "NIO" to 36.773917f,
        "NOK" to 10.473405f,
        "NPR" to 133.252257f,
        "NZD" to 1.672407f,
        "OMR" to 0.384974f,
        "PAB" to 1f,
        "PEN" to 3.686568f,
        "PGK" to 3.652739f,
        "PHP" to 56.903996f,
        "PKR" to 289.889792f,
        "PLN" to 4.085339f,
        "PYG" to 7303.793279f,
        "QAR" to 3.664793f,
        "RON" to 4.5318f,
        "RSD" to 107.551f,
        "RUB" to 101.299997f,
        "RWF" to 1185.975185f,
        "SAR" to 3.751526f,
        "SBD" to 8.36879f,
        "SCR" to 13.304812f,
        "SDG" to 601.5f,
        "SEK" to 10.825222f,
        "SGD" to 1.356688f,
        "SHP" to 0.789818f,
        "SLL" to 20969.5f,
        "SOS" to 572.460283f,
        "SRD" to 38.225f,
        "SSP" to 130.26f,
        "STD" to 22281.8f,
        "STN" to 22.401024f,
        "SVC" to 8.794957f,
        "SYP" to 2512.53f,
        "SZL" to 18.931151f,
        "THB" to 35.299833f,
        "TJS" to 11.02177f,
        "TMT" to 3.5f,
        "TND" to 3.07895f,
        "TOP" to 2.376914f,
        "TRY" to 27.053721f,
        "TTD" to 6.822837f,
        "TWD" to 31.971302f,
        "TZS" to 2506.422f,
        "UAH" to 37.122246f,
        "UGX" to 3749.635375f,
        "USD" to 1f,
        "UYU" to 37.906803f,
        "UZS" to 12137.589803f,
        "VES" to 31.24895f,
        "VND" to 23820.868431f,
        "VUV" to 118.722f,
        "WST" to 2.7185f,
        "XAF" to 601.612108f,
        "XAG" to 0.04426738f,
        "XAU" to 0.00052402f,
        "XCD" to 2.70255f,
        "XDR" to 0.751851f,
        "XOF" to 601.612108f,
        "XPD" to 0.00079327f,
        "XPF" to 109.445316f,
        "XPT" to 0.00111605f,
        "YER" to 250.232f,
        "ZAR" to 19.079921f,
        "ZMW" to 19.169139f,
        "ZWL" to 322f
    )

    /**
     *  fake one, we don't have real data for this
     */
    val fakeCurrencyDataBaseCNY = fakeCurrencyDataBaseUSD

    @Before
    fun createDb() {
        val context: Context = ApplicationProvider.getApplicationContext()
        // Using an in-memory database because the information stored here disappears when the
        // process is killed.
        database = Room.inMemoryDatabaseBuilder(context, CurrencyDatabase::class.java)
            // Allowing main thread queries, just for testing.
            .allowMainThreadQueries()
            .build()
        currencyDao = database.currencyDao()
    }

    @After
    @Throws(IOException::class)
    fun closeDb() {
        database.close()
    }

    @Test
    @Throws(Exception::class)
    fun getCurrencyBaseInUSD() = runBlocking {
        val currency1 = CurrencyData(
            "USD",
            license = "https://openexchangerates.org/license",
            timestamp = 1692025200,
            ratesMap = fakeCurrencyDataBaseUSD
        )
        currencyDao.insert(currency1)
        val usdCurrency = currencyDao.getCurrencyBaseInUSD("USD").first()
        assertEquals(usdCurrency, currency1)
    }

    @Test
    @Throws(Exception::class)
    fun getCurrencyAll() = runBlocking {
        val currency1 = CurrencyData(
            "USD",
            license = "https://openexchangerates.org/license",
            timestamp = 1692025200,
            ratesMap = fakeCurrencyDataBaseUSD
        )
        currencyDao.insert(currency1)
        val currency2 = CurrencyData(
            "CNY",
            license = "https://openexchangerates.org/license",
            timestamp = 1692025200,
            ratesMap = fakeCurrencyDataBaseCNY
        )
        currencyDao.insert(currency2)
        val allCurrencies = currencyDao.getCurrencyAll().first()
        assertEquals(allCurrencies[0], currency1)
        assertEquals(allCurrencies[1], currency2)
    }

    @Test
    @Throws(Exception::class)
    fun deleteAll() = runBlocking {
        val currency1 = CurrencyData(
            "USD",
            license = "https://openexchangerates.org/license",
            timestamp = 1692025200,
            ratesMap = fakeCurrencyDataBaseUSD
        )
        currencyDao.insert(currency1)
        val currency2 = CurrencyData(
            "CNY",
            license = "https://openexchangerates.org/license",
            timestamp = 1692025200,
            ratesMap = fakeCurrencyDataBaseCNY
        )
        currencyDao.insert(currency2)
        currencyDao.deleteAll()
        val allCurrencies = currencyDao.getCurrencyAll().first()
        assertTrue(allCurrencies.isEmpty())
    }
}