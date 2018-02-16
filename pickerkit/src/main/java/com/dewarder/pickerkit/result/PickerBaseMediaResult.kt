package com.dewarder.pickerkit.result

interface PickerBaseMediaResult<out T> : PickerResult {

    val selected: List<T>

    val unselected: List<T>
}
