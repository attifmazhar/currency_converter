package com.assignment.currency.data.local.entity

import com.assignment.currency.domain.model.CurrencyRate

fun CurrencyRate.toCurrencyRateEntity(): CurrencyRateEntity {
    return CurrencyRateEntity(
        code = code,
        rate = rate
    )
}