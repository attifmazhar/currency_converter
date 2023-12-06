package com.assignment.currency.ui.main_screen

import com.assignment.currency.data.local.entity.CurrencyRateEntity

sealed class MainScreenEvent {
    object ToCurrencySelect: MainScreenEvent()
    data class BottomSheetItemClicked(val value: CurrencyRateEntity): MainScreenEvent()
    data class AmountChanged(val value: Int): MainScreenEvent()
}
