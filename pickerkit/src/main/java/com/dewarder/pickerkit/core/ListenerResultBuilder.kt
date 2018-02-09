package com.dewarder.pickerkit.core

import android.app.Activity
import android.arch.lifecycle.Lifecycle
import android.support.v4.app.Fragment
import java.io.Closeable

interface ListenerResultBuilder {

    fun <R : Result, L : OnPickerResultListener<R>> observe(
            picker: Picker<*, R>,
            listener: L
    ): ListenerResultBuilder

    fun <R : Result> observe(
            picker: Picker<*, R>,
            listener: (R) -> Unit
    ): ListenerResultBuilder

    fun observeAll(
            listener: OnPickerResultListener<Result>
    ): ListenerResultBuilder

    fun observeAll(
            listener: (Result) -> Unit
    ): ListenerResultBuilder

    fun attachToLifecycle(activity: Activity): ListenerResultBuilder

    fun attachToLifecycle(lifecycle: Lifecycle): ListenerResultBuilder

    fun build(): Closeable
}