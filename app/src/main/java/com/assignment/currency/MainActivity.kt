package com.assignment.currency

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.material3.Surface
import androidx.work.WorkInfo
import androidx.work.WorkManager
import com.assignment.currency.presentation.main_screen.MainScreen
import com.assignment.currency.ui.main_screen.MainScreenViewModel
import com.assignment.currency.ui.theme.CurrencyAppTheme
import com.currency.utils.CURRENCY_WORK_MANAGER_TAG
import com.currency.utils.NetworkConnectionListener
import com.currency.utils.NetworkMonitoringHelper
import com.google.common.util.concurrent.ListenableFuture
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity(), NetworkConnectionListener {


    val viewModel: MainScreenViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            CurrencyAppTheme {
                Surface {
                    MainScreen(
                        state = viewModel.state,
                        onEvent = viewModel::onEvent
                    )
                }
            }
        }
        NetworkMonitoringHelper.newInstance(this, this).setLifecycleOwner(this)
    }


    private fun isWorkerRunning(tag: String = CURRENCY_WORK_MANAGER_TAG): Boolean {
        return try {
            val statuses: ListenableFuture<List<WorkInfo>> =
                WorkManager.getInstance(applicationContext).getWorkInfosForUniqueWork(tag)
            statuses.get().any { it.state == WorkInfo.State.RUNNING }
        } catch (e: Exception) {
            e.printStackTrace()
            false
        }
    }

    override fun onNetworkConnected() {

        val shouldRefreshRightNow =
            viewModel.isTimeToRefresh() && !isWorkerRunning() && application is CurrencyApp
        if (shouldRefreshRightNow) {
            (application as CurrencyApp).resetWorkManager()
        }
    }
}

