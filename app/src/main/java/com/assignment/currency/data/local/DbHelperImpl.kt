package com.assignment.currency.data.local

import com.assignment.currency.data.local.entity.CurrencyRateEntity
import com.assignment.currency.data.local.entity.CurrencyResponse
import com.assignment.currency.data.local.entity.toCurrencyRateEntityList
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class DbHelperImpl @Inject constructor(private val appDatabase: CurrencyRateDatabase) : DbHelper {
    override suspend fun insertCurrencies(currencies: CurrencyResponse) {
        val list = currencies.toCurrencyRateEntityList()
        appDatabase.currencyRateDao.insertAll(list)
    }

    override fun getAllCurrencies(): Flow<List<CurrencyRateEntity>> {
        return appDatabase.currencyRateDao.getAllCurrencyRates()
    }

}
