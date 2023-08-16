@file:Suppress("IllegalIdentifier")
package com.xuwanjin.currencyconvert

import androidx.test.core.app.ApplicationProvider.getApplicationContext
import com.xuwanjin.coredata.CurrencyDataRepo
import com.xuwanjin.coredata.dao.CurrencyStore
import com.xuwanjin.datastore.DataStoreUtils
import com.xuwanjin.testing.test.MainDispatcherRule
import com.xuwanjin.testing.test.MockCurrencyData
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import javax.inject.Inject

@HiltAndroidTest
class MainViewModelTest {
    companion object {
        const val TAG = "MainViewModelTest"
    }
    @Inject
    lateinit var currencyDataRepo: CurrencyDataRepo

    @Inject
    lateinit var currencyStore: CurrencyStore
    private lateinit var mainViewModel: MainViewModel

    @get:Rule
    var coroutineRule = MainDispatcherRule()


    @get:Rule(order = 0)
    var hiltAndroidRule = HiltAndroidRule(this)

    @Before
    fun before() {
        hiltAndroidRule.inject()
        DataStoreUtils.init(getApplicationContext())
        mainViewModel = MainViewModel(currencyDataRepo, currencyStore)
        mainViewModel.ratesBaseUSD = MockCurrencyData.fakeCurrencyMapBaseUSD

    }

    @Test
    fun initial_intent_test_noInputOneShowTheCurrencyBaseOnOneUSD()  = runTest {
        val viewModel = mainViewModel
        val convertUiStateJob = launch(coroutineRule.testDispatcher) {
            mainViewModel.currencyConvertUiState.collect()
        }
        viewModel.onBaseCurrencyChange("", "USD")
        val result = (viewModel.currencyConvertUiState.value as CurrencyConvertUiState.Success).currencyData?.ratesMap

        assertEquals(
            viewModel.ratesBaseUSD,
            result
        )
        convertUiStateJob.cancel()
    }
    @Test
    fun initial_intent_test_inputOneShowTheCurrencyBaseOnOneUSD() = runTest {
        val viewModel = mainViewModel
        val convertUiStateJob = launch(coroutineRule.testDispatcher) {
            mainViewModel.currencyConvertUiState.collect()
        }
        viewModel.onBaseCurrencyChange("1", "USD")
        val result = (viewModel.currencyConvertUiState.value as CurrencyConvertUiState.Success).currencyData?.ratesMap

        assertEquals(
            viewModel.ratesBaseUSD,
            result
        )
        convertUiStateJob.cancel()
    }

    @Test
    fun userInputShowTheCurrency() = runTest {
        val viewModel = mainViewModel
        val convertUiStateJob = launch(coroutineRule.testDispatcher) {
            mainViewModel.currencyConvertUiState.collect()
        }
        val newRatesMap = mainViewModel.ratesBaseUSD.toMutableMap()
        val currency2USD = newRatesMap.mapValues {
            it.value.times(2)
        }
        viewModel.onBaseCurrencyChange("2", "USD")
        val result = (viewModel.currencyConvertUiState.value as CurrencyConvertUiState.Success).currencyData?.ratesMap
        assertEquals(
            currency2USD,
            result
        )
        convertUiStateJob.cancel()
    }
    @Test
    fun userSelectedCurrencyTheCurrency() = runTest {
        val viewModel = mainViewModel
        val convertUiStateJob = launch(coroutineRule.testDispatcher) {
            mainViewModel.currencyConvertUiState.collect()
        }
        val newRatesMap = mainViewModel.ratesBaseUSD.toMutableMap()
        val userInput = 2
        val userSelectedCurrency = "CNY"
        val rate = newRatesMap[userSelectedCurrency]?:return@runTest
        val moneyInUSD = userInput.toFloat().div(rate)
        val converted = newRatesMap.mapValues {
            it.value.times(moneyInUSD)
        }
        viewModel.onBaseCurrencyChange(userInput.toString(), userSelectedCurrency)
        val result = (viewModel.currencyConvertUiState.value as CurrencyConvertUiState.Success).currencyData?.ratesMap
        assertEquals(
            converted,
            result
        )
        convertUiStateJob.cancel()
    }
}