package com.currency.utils

import android.content.Context
import android.net.ConnectivityManager
import android.net.ConnectivityManager.NetworkCallback
import android.net.Network
import android.net.NetworkCapabilities
import android.net.NetworkRequest
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import androidx.lifecycle.LifecycleOwner

/**
 * This class is LifeCycle aware Network Monitoring helper.
 */
class NetworkMonitoringHelper constructor(
    context: Context,
    private var connectionListener: NetworkConnectionListener?
) : NetworkCallback(), LifecycleEventObserver {
    private val networkRequest: NetworkRequest = NetworkRequest.Builder()
        .addTransportType(NetworkCapabilities.TRANSPORT_CELLULAR)
        .addTransportType(NetworkCapabilities.TRANSPORT_WIFI)
        .build()
    private var lifecycle: Lifecycle? = null
    private val connectivityManager: ConnectivityManager =
        context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager

    /**
     * Sets the lifecycle owner for this view. This means you don't need
     * to call [.resume], [.pause] at all.
     *
     * @param owner the owner activity or fragment
     */
    fun setLifecycleOwner(owner: LifecycleOwner) {
        lifecycle?.removeObserver(this)
        lifecycle = owner.lifecycle.also { it.addObserver(this) }
    }

    private fun onResume() {
        connectivityManager.registerNetworkCallback(networkRequest, this)
    }

    private fun onPause() {
        connectivityManager.unregisterNetworkCallback(this)
    }

    private fun onDestroy() {
        connectionListener = null
    }

    override fun onAvailable(network: Network) {
        super.onAvailable(network)
        connectionListener?.onNetworkConnected()
    }

    override fun onStateChanged(source: LifecycleOwner, event: Lifecycle.Event) {

        when (event) {
            Lifecycle.Event.ON_RESUME -> {
                onResume()
            }
            Lifecycle.Event.ON_PAUSE -> {
                onPause()
            }
            Lifecycle.Event.ON_DESTROY -> {
                onDestroy()
            }
            else -> {}
        }
    }

    companion object {
        fun newInstance(
            context: Context,
            connectionListener: NetworkConnectionListener
        ): NetworkMonitoringHelper {
            return NetworkMonitoringHelper(context, connectionListener)
        }
    }

}

interface NetworkConnectionListener {
    fun onNetworkConnected()
}