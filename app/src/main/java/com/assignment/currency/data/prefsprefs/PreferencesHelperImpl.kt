package com.currency.data.local.prefs

import android.content.Context
import android.content.SharedPreferences
import androidx.core.content.edit
import com.assignment.currency.di.PreferenceInfo
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject

class PreferencesHelperImpl @Inject constructor(
    @ApplicationContext val context: Context,
    @PreferenceInfo prefFileName: String
) : PreferencesHelper {
    private val mPrefs: SharedPreferences =
        context.getSharedPreferences(prefFileName, Context.MODE_PRIVATE)

    companion object {
        private const val PREF_LAST_TIMESTAMP = "PREF_LAST_TIMESTAMP"
        private const val PREF_BASE_CURRENCY_UNIT = "PREF_BASE_CURRENCY_UNIT"

    }

    override fun setLastTimestamp(timestamp: Long) {
        mPrefs.edit { putLong(PREF_LAST_TIMESTAMP, timestamp) }
    }

    override fun getLastTimestamp(): Long {
        return mPrefs.getLong(PREF_LAST_TIMESTAMP, 0L)
    }

    override fun setCurrencyBaseUnit(isoCode: String) {
        mPrefs.edit { putString(PREF_BASE_CURRENCY_UNIT, isoCode) }
    }

    override fun getCurrencyBaseUnit(): String? {
        return mPrefs.getString(PREF_BASE_CURRENCY_UNIT, null)
    }

}