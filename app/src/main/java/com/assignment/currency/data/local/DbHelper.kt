package com.assignment.currency.data.local

import androidx.lifecycle.LiveData
import com.assignment.currency.data.local.entity.CurrencyRateEntity
import com.assignment.currency.data.local.entity.CurrencyResponse
import com.assignment.currency.domain.model.CurrencyRate
import kotlinx.coroutines.flow.Flow

interface DbHelper {
    suspend fun insertCurrencies(currencies: CurrencyResponse)

    fun getAllCurrencies(): Flow<List<CurrencyRateEntity>>

}