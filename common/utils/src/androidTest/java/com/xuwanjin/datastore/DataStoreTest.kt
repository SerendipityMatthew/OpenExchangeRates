package com.xuwanjin.datastore

import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class DataStoreTest {

    @Before
    fun before() {
        DataStoreUtils.init(ApplicationProvider.getApplicationContext())
    }
    @Test
    fun testStringStore() = runTest {
        val inputString = "Matthew"
        val key = "name"
        DataStoreUtils.putSyncData(key, inputString)
        val value = DataStoreUtils.getSyncData(key,"")
        assertEquals(
            inputString,
            value
        )
    }

    @Test
    fun testIntStore() = runTest {
        val inputString = 12
        val key = "age"
        DataStoreUtils.putSyncData(key, inputString)
        val value = DataStoreUtils.getSyncData(key,0)
        assertEquals(
            inputString,
            value
        )
    }
    @Test
    fun testLongStore() = runTest {
        val inputString = 12L
        val key = "age"
        DataStoreUtils.putSyncData(key, inputString)
        val value = DataStoreUtils.getSyncData(key,0L)
        assertEquals(
            inputString,
            value
        )
    }
    @Test
    fun testFloatStore() = runTest {
        val inputString = 12.0f
        val key = "age"
        DataStoreUtils.putSyncData(key, inputString)
        val value = DataStoreUtils.getSyncData(key,0.0f)
        assertEquals(
            inputString,
            value
        )
    }
    @Test
    fun testBooleanStore() = runTest {
        val input = false
        val key = "is_girl"
        DataStoreUtils.putSyncData(key, input)
        val value = DataStoreUtils.getSyncData(key,false)
        assertFalse(value)
    }
}