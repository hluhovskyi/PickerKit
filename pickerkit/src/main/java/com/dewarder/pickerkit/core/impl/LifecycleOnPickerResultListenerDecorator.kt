package com.dewarder.pickerkit.core.impl

import com.dewarder.pickerkit.core.OnPickerResultListener
import com.dewarder.pickerkit.core.Result

/**
 * Should be used on main thread only
 */
internal class LifecycleOnPickerResultListenerDecorator<in R : Result>(
        val listener: OnPickerResultListener<R>
) : LifecycleRegistry, OnPickerResultListener<R> {

    private var isResumed: Boolean = false
    private var result: R? = null

    override fun onResume() {
        isResumed = true

        if (result != null) {
            listener.onResult(result!!)
            result = null
        }
    }

    override fun onPause() {
        isResumed = false
    }

    override fun onResult(result: R) {
        if (this.result != null) {
            throw IllegalStateException("You can't push more than one item")
        }

        if (isResumed) {
            listener.onResult(result)
        } else {
            this.result = result
        }
    }
}