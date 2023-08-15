package com.xuwanjin.currencyconvert

import androidx.test.core.app.ApplicationProvider.getApplicationContext
import com.xuwanjin.coredata.CurrencyDataRepo
import com.xuwanjin.coredata.dao.CurrencyStore
import com.xuwanjin.datastore.DataStoreUtils
import com.xuwanjin.testing.test.MainDispatcherRule
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

    }

    @Test
    fun noInputShow() = runTest {
        val viewModel = mainViewModel
        val convertUiStateJob = launch(coroutineRule.testDispatcher) {
            mainViewModel.currencyConvertUiState.collect()
        }
        viewModel.onBaseCurrencyChange("", "USD")
        assertEquals(
            viewModel.ratesBaseUSD,
            (viewModel.currencyConvertUiState.value as CurrencyConvertUiState.Success).currencyData?.ratesMap
        )
        convertUiStateJob.cancel()
    }
    @Test
    fun input1GetRates() = runTest {
        val viewModel = mainViewModel
        val convertUiStateJob = launch(coroutineRule.testDispatcher) {
            mainViewModel.currencyConvertUiState.collect()
        }
        viewModel.onBaseCurrencyChange("2", "USD")
        assertEquals(
            viewModel.ratesBaseUSD,
            (viewModel.currencyConvertUiState.value as CurrencyConvertUiState.Success).currencyData?.ratesMap
        )
        convertUiStateJob.cancel()
    }
}