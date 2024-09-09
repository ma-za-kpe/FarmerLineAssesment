package com.farmerline.mergedataforms.utils

import com.farmerline.mergedataforms.data.api.ConnectionManager
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class NetworkMonitor @Inject constructor(
    private val connectionManager: ConnectionManager
) {
    private val _networkStatus = MutableStateFlow(false)
    val networkStatus: StateFlow<Boolean> = _networkStatus.asStateFlow()

    init {
        connectionManager.registerNetworkCallback { isConnected ->
            _networkStatus.value = isConnected
        }
    }
}

//class DataSyncManager @Inject constructor(
//    private val connectionManager: ConnectionManager,
//    private val dataRepository: DataRepository
//) {
//    fun syncLargeData() {
//        if (connectionManager.isWifiConnected()) {
//            // Perform large data synchronizations
//            dataRepository.syncLargeDataSet()
//        } else {
//            // Maybe show a message to the user or schedule the sync for later
//            Log.i("DataSync", "Large data sync postponed - not on WiFi")
//        }
//    }
//}