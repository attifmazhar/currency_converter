package com.currency.data

import com.assignment.currency.data.local.DbHelper
import com.assignment.currency.domain.repository.CurrencyRepository
import com.currency.data.local.prefs.PreferencesHelper
import com.currency.utils.DEFAULT_REFRESH_INTERVAL_HOURS

interface DataManager : DbHelper, PreferencesHelper, CurrencyRepository {

    fun isTimeToRefresh(minutes: Int = DEFAULT_REFRESH_INTERVAL_HOURS): Boolean
}