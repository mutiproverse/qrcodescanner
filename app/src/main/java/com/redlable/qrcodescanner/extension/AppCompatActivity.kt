package com.redlable.qrcodescanner.extension

import androidx.appcompat.app.AppCompatActivity
import com.redlable.qrcodescanner.feature.common.dialog.ErrorDialogFragment

fun AppCompatActivity.showError(error: Throwable?) {
    val errorDialog =
        ErrorDialogFragment.newInstance(
            this,
            error
        )
    errorDialog.show(supportFragmentManager, "")
}