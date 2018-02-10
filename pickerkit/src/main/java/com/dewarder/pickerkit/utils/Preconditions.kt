package com.dewarder.pickerkit.utils

import android.os.Looper

fun checkMainThread() {
    if (Looper.myLooper() != Looper.getMainLooper()) {
        throw IllegalStateException(
            "Expected to be called on the main thread but was ${Thread.currentThread().name}")
    }
}