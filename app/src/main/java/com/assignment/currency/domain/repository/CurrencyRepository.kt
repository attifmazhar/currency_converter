package com.assignment.currency.domain.repository

import com.assignment.currency.data.local.entity.CurrencyResponse
import com.assignment.currency.domain.model.Resource
import kotlinx.coroutines.flow.Flow

interface CurrencyRepository {
    fun getCurrencyRatesList(): Flow<Resource<CurrencyResponse>>
}