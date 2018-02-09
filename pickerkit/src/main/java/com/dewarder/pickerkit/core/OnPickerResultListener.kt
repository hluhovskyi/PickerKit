package com.dewarder.pickerkit.core

interface OnPickerResultListener<in R : Result> {

    fun onResult(result: R)

    companion object {

        fun <R : Result> create(action: (R) -> Unit): OnPickerResultListener<R> =
                OnPickerResultListenerAdapter(action)

        private class OnPickerResultListenerAdapter<in R : Result>(
                val action: (R) -> Unit
        ) : OnPickerResultListener<R> {

            override fun onResult(result: R) {
                action(result)
            }
        }
    }
}