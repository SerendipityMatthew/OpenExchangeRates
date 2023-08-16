package com.xuwanjin.coredata

import android.content.Context
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import androidx.test.runner.AndroidJUnit4
import com.xuwanjin.coredata.local.dao.CurrencyDao
import com.xuwanjin.coredata.local.dao.CurrencyDatabase
import com.xuwanjin.model.CurrencyData
import com.xuwanjin.testing.test.MockCurrencyData.fakeCurrencyMapBaseCNY
import com.xuwanjin.testing.test.MockCurrencyData.fakeCurrencyMapBaseUSD
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
            ratesMap = fakeCurrencyMapBaseUSD
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
            ratesMap = fakeCurrencyMapBaseUSD
        )
        currencyDao.insert(currency1)
        val currency2 = CurrencyData(
            "CNY",
            license = "https://openexchangerates.org/license",
            timestamp = 1692025200,
            ratesMap = fakeCurrencyMapBaseCNY
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
            ratesMap = fakeCurrencyMapBaseUSD
        )
        currencyDao.insert(currency1)
        val currency2 = CurrencyData(
            "CNY",
            license = "https://openexchangerates.org/license",
            timestamp = 1692025200,
            ratesMap = fakeCurrencyMapBaseCNY
        )
        currencyDao.insert(currency2)
        currencyDao.deleteAll()
        val allCurrencies = currencyDao.getCurrencyAll().first()
        assertTrue(allCurrencies.isEmpty())
    }
}