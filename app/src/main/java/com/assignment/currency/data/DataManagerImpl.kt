package com.currency.data

import com.assignment.currency.data.local.DbHelper
import com.assignment.currency.data.local.entity.CurrencyRateEntity
import com.assignment.currency.data.local.entity.CurrencyResponse
import com.assignment.currency.domain.model.Resource
import com.assignment.currency.domain.repository.CurrencyRepository
import com.currency.data.local.prefs.PreferencesHelper
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class DataManagerImpl @Inject constructor(
    private val dbHelper: DbHelper,
    private val preferencesHelper: PreferencesHelper,
    private val apiHelper: CurrencyRepository
) : DataManager {

    override suspend fun insertCurrencies(currencies: CurrencyResponse) {
        dbHelper.insertCurrencies(currencies)
    }

    override fun getAllCurrencies(): Flow<List<CurrencyRateEntity>> {
        return dbHelper.getAllCurrencies()
    }


    override fun setLastTimestamp(timestamp: Long) {
        preferencesHelper.setLastTimestamp(timestamp)
    }

    override fun getLastTimestamp(): Long {
        return preferencesHelper.getLastTimestamp()
    }

    override fun setCurrencyBaseUnit(isoCode: String) {
        preferencesHelper.setCurrencyBaseUnit(isoCode)
    }

    override fun getCurrencyBaseUnit(): String? {
        return preferencesHelper.getCurrencyBaseUnit()
    }

    override fun getCurrencyRatesList(): Flow<Resource<CurrencyResponse>> {
        return apiHelper.getCurrencyRatesList()
    }

    override fun isTimeToRefresh(hours: Int): Boolean {
        val lastTimestamp = preferencesHelper.getLastTimestamp()
        val diffInMinutes = (System.currentTimeMillis() - lastTimestamp) / 60_000
        return diffInMinutes > (hours*60)
    }

}