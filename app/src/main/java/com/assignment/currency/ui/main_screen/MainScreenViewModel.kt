package com.assignment.currency.ui.main_screen

import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.assignment.currency.data.local.entity.CurrencyRateEntity
import com.assignment.currency.data.local.entity.toCurrencyRateEntityList
import com.assignment.currency.domain.model.Resource
import com.currency.data.DataManager
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MainScreenViewModel @Inject constructor(
    private val dataManager: DataManager
): ViewModel() {

    var state by mutableStateOf(MainScreenState())
    init {
        getCurrencyRatesList()
    }

    fun isTimeToRefresh(): Boolean {
        return dataManager.isTimeToRefresh()
    }

    fun onEvent(event: MainScreenEvent) {
        when(event) {
            is MainScreenEvent.BottomSheetItemClicked -> {

                state = state.copy(
                    selectedCurrencyCode = event.value.code,
                    selectedCurrencyValue = event.value.baseRate.toString(),
                    currencyLocal = emptyList()
                )
                calculateConversionRates()
            }
            is MainScreenEvent.AmountChanged -> {
                state = state.copy(
                    amountEntered = event.value.toString()
                )
                calculateConversionRates()
            }
            else -> {}
        }
    }


    private fun getCurrencyRatesList() {
        viewModelScope.launch {
            val list = dataManager.getAllCurrencies().collectLatest { result ->
                if (result.isNotEmpty()) {
                    state = state.copy(

                        currenciesEUR = result,
                        error = null,
                        selectedCurrencyCode = "EUR",
                        selectedCurrencyValue = "1",
                        amountEntered = "1"
                    )
                    calculateConversionRates()
                } else {
                    dataManager
                        .getCurrencyRatesList()
                        .collectLatest { result ->
                            state = when(result) {
                                is Resource.Success -> {
                                    Log.e("api-call", "success msg = "+ result.message)
                                    result.data?.let {
                                        dataManager.insertCurrencies(it)
                                    }
                                    state.copy(
                                        currenciesEUR = result.data?.toCurrencyRateEntityList() ?: emptyList(),
                                        error = null
                                    )
                                }

                                is Resource.Error -> {
                                    Log.e("api-call", "error msg = "+ result.message)
                                    state.copy(
                                        currenciesEUR = emptyList(),
                                        error = result.message
                                    )
                                }
                            }
                        }
                }
            }
        }
    }

    /**
     * This method is used to calculate currency conversion rates whenever user changes currency
     * selection from dropdown or when amount is changed in input field.
     */
    private fun calculateConversionRates() {
        viewModelScope.launch {

            val enteredAmount = state.amountEntered.toDouble()
            if (state.selectedCurrencyValue.toDouble() == 0.0) {
                return@launch
            }

            val multiplier: Double = enteredAmount / state.selectedCurrencyValue.toDouble()

            if (state.currencyLocal.isEmpty()) {
                createNewRateCalculationList(multiplier)
            } else {
                updateCalculationInExistingRateList(multiplier)
            }
        }
    }

    /**
     * For the first time we would have to create a new conversion rate list
     */
    private fun createNewRateCalculationList(multiplier: Double) {

        state.currenciesEUR.let { currencies ->
            state = state.copy(currencyLocal = currencies.map { CurrencyRateEntity(
                code = it.code,
                baseRate = it.rate,
                rate = it.recalculateRate(multiplier, it.rate)
            )})
        }
    }

    /**
     * just update values in existing models are being observable livedata it is
     *  reflected to UI without needing us to notify recyclerView adapter again and again
     */
    private fun updateCalculationInExistingRateList(multiplier: Double) {
       for (item in state.currencyLocal) {
            item.recalculateRate(multiplier)
        }
        state = state.copy(currencyLocal = state.currencyLocal)
    }



}