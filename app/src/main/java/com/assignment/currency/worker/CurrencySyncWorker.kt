package com.currency.worker

import android.content.Context
import androidx.hilt.work.HiltWorker
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import com.assignment.currency.CurrencyApp
import com.assignment.currency.data.local.entity.CurrencyResponse
import com.currency.data.DataManager
import com.currency.utils.NetworkUtils
import com.currency.utils.printLogs
import dagger.assisted.Assisted
import dagger.assisted.AssistedInject
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.withContext

@HiltWorker
class CurrencySyncWorker @AssistedInject constructor(
    @Assisted val context: Context,
    @Assisted workerParams: WorkerParameters,
    private val dataManager: DataManager
) : CoroutineWorker(context, workerParams) {

    override suspend fun doWork(): Result {
        printLogs("currency_worker", "doWork")
        val result =
            if (NetworkUtils.isNetworkConnected(context)) fetchCurrencyData() else Result.retry()
        printLogs("currency_worker", "doWork: end")
        return result
    }

    /**
     * This method will hit 2 apis in parallel and combine their results to save
     * data in database
     */
    private suspend fun fetchCurrencyData(): Result {
        lateinit var result: Result
        withContext(Dispatchers.IO) {

            updateLoadingStatus(true)
            val responseCurrencyNames = async { dataManager.getCurrencyRatesList() }
            val response = responseCurrencyNames.await()
            response.collectLatest {

                printLogs("currency_worker", "in fetchCurrencyData isSuccessful "+ it.data)
                onResponseReceived(it.data!!)

                result = if (it.data.success) {
                    Result.success()
                } else {
                    printLogs("currency_worker", "response: request has null response")
                    Result.retry()
                }
            }
        }
        updateLoadingStatus(false)
        printLogs("currency_worker", "end fetchCurrencyData: result= $result")
        return result
    }

    /**
     * In this method we merge response of both apis and save into database for local usage
     */
    private suspend fun onResponseReceived(
        currencyRates: CurrencyResponse
    ) {
        dataManager.setCurrencyBaseUnit(currencyRates.base)
        dataManager.setLastTimestamp(System.currentTimeMillis())
        printLogs("currency_worker", "onResponseReceived: size= ${currencyRates.rates.size}")
        dataManager.insertCurrencies(currencyRates)
    }

    private fun updateLoadingStatus(isLoading: Boolean) {
        if (applicationContext is CurrencyApp) {
            (applicationContext as CurrencyApp).isLoadingData.value = isLoading
        }
    }

}