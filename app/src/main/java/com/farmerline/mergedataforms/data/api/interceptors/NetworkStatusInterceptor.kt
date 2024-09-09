package com.farmerline.mergedataforms.data.api.interceptors

import com.farmerline.mergedataforms.data.api.ConnectionManager
import okhttp3.Interceptor
import okhttp3.Response
import java.io.IOException
import javax.inject.Inject

class NetworkStatusInterceptor @Inject constructor(private val connectionManager: ConnectionManager) : Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
        return if (connectionManager.isConnected()) {
            chain.proceed(chain.request())
        } else {
            throw NetworkUnavailableException()
        }
    }
}

class NetworkUnavailableException : IOException("No network available, please check your WiFi or Data connection")