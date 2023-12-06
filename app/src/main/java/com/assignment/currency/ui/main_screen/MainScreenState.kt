package com.assignment.currency.ui.main_screen

import com.assignment.currency.data.local.entity.CurrencyRateEntity
import com.assignment.currency.domain.model.CurrencyRate

data class MainScreenState(
    val selectedCurrencyCode: String = "EUR",
    val selectedCurrencyValue: String = "1.00",
    val amountEntered: String = "1",
    val currenciesEUR:List<CurrencyRateEntity> = emptyList(),
    val currencyLocal:List<CurrencyRateEntity> = emptyList(),
    val error: String? = null
)
