package com.dewarder.pickerkit.provider

interface PickerDataProvider<out T> {

    fun request(callback: Callback<T>)

    interface Callback<in T> {

        fun onNext(data: Collection<T>)

        fun onComplete()

        fun onError(throwable: Throwable)
    }
}
