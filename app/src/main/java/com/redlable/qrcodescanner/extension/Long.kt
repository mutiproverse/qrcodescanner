package com.redlable.qrcodescanner.extension

fun Long?.orZero(): Long {
    return this ?: 0L
}