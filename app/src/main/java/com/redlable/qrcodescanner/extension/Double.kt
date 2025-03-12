package com.redlable.qrcodescanner.extension

fun Double?.orZero(): Double {
    return this ?: 0.0
}