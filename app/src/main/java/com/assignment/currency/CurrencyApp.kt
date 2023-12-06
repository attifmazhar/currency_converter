package com.assignment.currency

import android.app.Application
import androidx.compose.runtime.mutableStateOf
import androidx.hilt.work.HiltWorkerFactory
import androidx.work.Configuration
import androidx.work.ExistingPeriodicWorkPolicy
import androidx.work.PeriodicWorkRequestBuilder
import androidx.work.WorkManager
import com.currency.utils.CURRENCY_WORK_MANAGER_TAG
import com.currency.utils.DEFAULT_REFRESH_INTERVAL_HOURS
import com.currency.worker.CurrencySyncWorker
import dagger.hilt.android.HiltAndroidApp
import java.util.concurrent.TimeUnit
import javax.inject.Inject

@HiltAndroidApp
class CurrencyApp: Application(), Configuration.Provider {
    val isLoadingData = mutableStateOf(false)

    @Inject
    lateinit var workerFactory: HiltWorkerFactory

    override fun onCreate() {
        super.onCreate()
        initWorkManager()
    }


    override fun getWorkManagerConfiguration() = Configuration.Builder()
        .setWorkerFactory(workerFactory)
        .setMinimumLoggingLevel(android.util.Log.DEBUG)
        .build()

    private fun initWorkManager(existingPeriodicWorkPolicy: ExistingPeriodicWorkPolicy = ExistingPeriodicWorkPolicy.KEEP) {
        val worker = PeriodicWorkRequestBuilder<CurrencySyncWorker>(
            DEFAULT_REFRESH_INTERVAL_HOURS.toLong(), TimeUnit.HOURS
        ).addTag(CURRENCY_WORK_MANAGER_TAG)
            .build()

        WorkManager.getInstance(applicationContext)
            .enqueueUniquePeriodicWork(
                CURRENCY_WORK_MANAGER_TAG,
                existingPeriodicWorkPolicy,
                worker
            )
    }

    fun resetWorkManager() {
        initWorkManager(ExistingPeriodicWorkPolicy.REPLACE)
    }
}