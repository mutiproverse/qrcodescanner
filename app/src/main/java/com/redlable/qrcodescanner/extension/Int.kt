package com.redlable.qrcodescanner.extension

fun Int?.orZero(): Int {
    return this ?: 0
}