package com.dewarder.pickerkit.result

import android.os.Parcelable

interface PickerResult : Parcelable {

    val isSubmitted: Boolean

    val isCanceled: Boolean
}
