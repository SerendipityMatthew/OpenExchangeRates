package com.xuwanjin.currencyconvert

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.skydoves.sandwich.suspendOnError
import com.skydoves.sandwich.suspendOnException
import com.skydoves.sandwich.suspendOnSuccess
import com.xuwanjin.model.CurrencyData
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
) : ViewModel(), CoroutineScope {
    companion object {
        const val TAG = "MainViewModel"
    }

    override val coroutineContext: CoroutineContext
        get() = Dispatchers.IO

    init {
        fetchCurrencies()
    }

    /**
     *  the workflow for fetching data
     *   1. fetching data from Database, then update the UI (the result maybe null, outdated)
     *   2. at the same time, fetching data from network, then update the UI
     *      (we use the flow for fetching data from database, it will automatically update the data after the inserting.
     *        so ignore the timing issues. It's so happy that working with Flow)
     */
    private fun fetchCurrencies() {
        fetchCurrenciesFromDB()
        fetchLatestCurrenciesFromNetwork()
    }


    /**
     * LinkedHashMap: for keep the order of data.
     */
    private var ratesBaseUSD = mutableMapOf<String, Float>()

    private var _currencyConvertUiState: MutableStateFlow<CurrencyData> = MutableStateFlow(CurrencyData())
    val currencyConvertUiState: StateFlow<CurrencyConvertUiState> =
        _currencyConvertUiState.map(CurrencyConvertUiState::Success)
            .stateIn(
                scope = viewModelScope,
                started = SharingStarted.WhileSubscribed(5_000),
                initialValue = CurrencyConvertUiState.Loading
            )


    fun onBaseCurrencyChange(input: String, baseCurrency: String) {
        viewModelScope.launch {
            if (baseCurrency.isBlank()) {
                return@launch
            }
            /**
             *  show the base currency if the input is blank.
             */
            val modifiedValue = input.ifBlank {
                "1"
            }
            val data = _currencyConvertUiState.value

            /**   the basic logic for processing the exchange
             *    for example: 14 CNY convert to 2 USD , then convert to 286 JPY
             *    1 USD， 143 JPY， 7 CNY
             *    2 USD   286 JPY  14 CNY
             *    10 CNY ->  XX USD ---> YY JPY
             *
             */
            val rate = ratesBaseUSD[baseCurrency]
            if (rate == null) {
                return@launch
            }
            val moneyInUSD = modifiedValue.toFloat().div(rate)
            val newRatesMap = ratesBaseUSD.toMutableMap()
            val converted = newRatesMap.mapValues {
                it.value.times(moneyInUSD)
            }
            _currencyConvertUiState.value = data.copy(ratesMap = converted)
        }

    }

    /**
     *  using the data that stored in database,
     *      maybe we cannot connect to network.
     *      maybe we did not store any data in database.
     */
    private fun fetchCurrenciesFromDB() {
        viewModelScope.launch(coroutineContext) {
            currencyStore.getCurrencyBaseInUSD()
                .collect { data ->
                    data?.let {
                        _currencyConvertUiState.value = it
                        ratesBaseUSD = it.ratesMap.toMutableMap()
                    }
                }
        }
    }

    /**
     *  fetch the latest data from network
     *   then update the database.
     */
    private fun fetchLatestCurrenciesFromNetwork() {
        viewModelScope.launch {
            currencyRepo.getLatestCurrency()
                .suspendOnSuccess {
                    ratesBaseUSD = this.data.ratesMap.toMutableMap()
                    _currencyConvertUiState.value = this.data
                    currencyStore.updateCurrencyData(this.data)
                }
                .suspendOnError {

                }
                .suspendOnException {
                    
                }
        }
    }

}