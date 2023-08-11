package com.xuwanjin.currencyconvert

import android.app.Application
import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.skydoves.sandwich.message
import com.skydoves.sandwich.suspendOnError
import com.skydoves.sandwich.suspendOnException
import com.skydoves.sandwich.suspendOnSuccess
import com.xuwanjin.coredata.CurrencyData
import com.xuwanjin.coredata.CurrencyDataRepo
import com.xuwanjin.coredata.dao.CurrencyStore
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject
import kotlin.coroutines.CoroutineContext


sealed interface CurrencyConvertUiState {
    data object Loading : CurrencyConvertUiState
    data class Success(val currencyData: CurrencyData?) : CurrencyConvertUiState
}

@HiltViewModel
class MainViewModel @Inject constructor(
    private val currencyRepo: CurrencyDataRepo,
    private val currencyStore: CurrencyStore,
    private val application: Application
) : ViewModel(), CoroutineScope {
    companion object {
        const val TAG = "MainViewModel"
    }

    override val coroutineContext: CoroutineContext
        get() = Dispatchers.IO

    init {
        fetchCurrencies()
    }

    private fun fetchCurrencies() {
        fetchCurrenciesFromDB()
        fetchLatestCurrenciesFromNetwork()
    }


    /**
     * LinkedHashMap keep the order of data.
     */
    private var ratesBaseUSD = mutableMapOf<String, Float>()

    private var _currencyConvertUiState: MutableStateFlow<CurrencyData> =
        MutableStateFlow(
            CurrencyData()
        )
    val currencyConvertUiState: StateFlow<CurrencyConvertUiState> =
        _currencyConvertUiState.map(CurrencyConvertUiState::Success)
            .stateIn(
                scope = viewModelScope,
                started = SharingStarted.WhileSubscribed(5_000),
                initialValue = CurrencyConvertUiState.Loading
            )

    fun onBaseCurrencyChange(input: Double, baseCurrency: String) {
        viewModelScope.launch {
            if (baseCurrency.isBlank()) {
                return@launch
            }
            val data = _currencyConvertUiState.value
            // 1 USD， 143 JPY， 7 CNY
            // 2 USD   286 JPY  14 CNY
            // 10 CNY ->  XX USD ---> YY JPY
            val rate = ratesBaseUSD[baseCurrency]
            if (rate == null) {
                return@launch
            }
            val moneyInUSD = input.div(rate)
            val newRatesMap = ratesBaseUSD.toMutableMap()
            val converted = newRatesMap.mapValues {
                it.value.times(moneyInUSD).toFloat()
            }
            _currencyConvertUiState.value = data.copy(ratesMap = converted)
        }

    }

    private fun fetchCurrenciesFromDB() {
        viewModelScope.launch(coroutineContext) {
            currencyStore.getCurrencyBaseInUSD()
                .collect { data ->
                    _currencyConvertUiState.value = data
                    Log.d(TAG, "fetchCurrenciesFromDB: data = $data")
                }
            fetchLatestCurrenciesFromNetwork()
        }
    }

    private fun fetchLatestCurrenciesFromNetwork() {
        viewModelScope.launch {
            currencyRepo.getLatestCurrency()
                .suspendOnSuccess {
                    Log.d(TAG, "fetchLatestCurrenciesFromNetwork: 11111 ${this.response.message()}")
                    ratesBaseUSD = this.data.ratesMap.toMutableMap()
                    _currencyConvertUiState.value = this.data
                    currencyStore.updateCurrencyData(this.data)
                }
                .suspendOnError {
//                    Log.d(TAG, "fetchLatestCurrenciesFromNetwork: $this")
                }
                .suspendOnException {
                }
        }
    }

}