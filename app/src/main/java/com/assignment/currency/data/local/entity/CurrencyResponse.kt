package com.assignment.currency.data.local.entity

data class CurrencyResponse(
    val base: String,
    val date: String,
    val success: Boolean,
    val timestamp: Int,
    val rates: Map<String, Double>,
    )


fun CurrencyResponse.toCurrencyRateEntityList(): List<CurrencyRateEntity> {

    val list = rates.map { CurrencyRateEntity(code = it.key, rate = it.value) }
    return arrayListOf<CurrencyRateEntity>().apply {
        add(CurrencyRateEntity(code = base, rate = 1.0, isMain = true))
        addAll(list)
    }
}
