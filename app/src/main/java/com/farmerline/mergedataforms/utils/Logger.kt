package com.farmerline.mergedataforms.utils

import timber.log.Timber

object Logger {
    private val logger = TimberLogging()

    fun init() {
        Timber.plant(logger)
    }

    fun d(message: String, t: Throwable? = null) = logger.d(t, message)

    fun i(message: String, t: Throwable? = null) = logger.i(t, message)

    fun e(t: Throwable? = null, message: String) = logger.e(t, message)

    fun wtf(t: Throwable? = null, message: String) = logger.wtf(t, message)
}


class TimberLogging : Timber.Tree() {
    override fun log(priority: Int, tag: String?, message: String, t: Throwable?) {
        when (priority) {
            android.util.Log.VERBOSE -> Timber.v(t, message)
            android.util.Log.DEBUG -> Timber.d(t, message)
            android.util.Log.INFO -> Timber.i(t, message)
            android.util.Log.WARN -> Timber.w(t, message)
            android.util.Log.ERROR -> Timber.e(t, message)
            android.util.Log.ASSERT -> Timber.wtf(t, message)
        }
    }
}