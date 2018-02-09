package com.dewarder.pickerkit.core.impl

import com.dewarder.pickerkit.core.OnPickerResultListener
import com.dewarder.pickerkit.core.Result

/**
 * Should be used on main thread only
 */
internal class StickyOnPickerResultListenerDecorator<in R : Result>(
        val listener: OnPickerResultListener<R>
) : GenericLifecycle, OnPickerResultListener<R> {

    private var isResumed: Boolean = false
    private var stickyValue: R? = null

    override fun onResume() {
        isResumed = true

        if (stickyValue != null) {
            listener.onResult(stickyValue!!)
            stickyValue = null
        }
    }

    override fun onPause() {
        isResumed = false
    }

    override fun onResult(result: R) {
        if (stickyValue != null) {
            throw IllegalStateException("You can't push more than one item")
        }

        if (isResumed) {
            listener.onResult(result)
        } else {
            stickyValue = result
        }
    }
}