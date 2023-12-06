package com.currency.data.local.prefs

interface PreferencesHelper {
    fun setLastTimestamp(timestamp: Long)
    fun getLastTimestamp(): Long
    fun setCurrencyBaseUnit(isoCode: String)
    fun getCurrencyBaseUnit(): String?
}