package com.redlable.qrcodescanner.extension

fun Boolean?.orFalse(): Boolean {
    return this ?: false
}