package com.redlable.qrcodescanner.usecase

import com.redlable.qrcodescanner.BuildConfig
import io.sentry.Sentry

object Logger {
    var isEnabled = BuildConfig.ERROR_REPORTS_ENABLED_BY_DEFAULT

    fun log(error: Throwable) {
        if (isEnabled) {
            Sentry.captureException(error)
        }
    }
}