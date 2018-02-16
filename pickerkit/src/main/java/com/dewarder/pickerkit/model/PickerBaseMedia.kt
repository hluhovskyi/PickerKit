package com.dewarder.pickerkit.model

import android.net.Uri
import android.os.Parcelable

interface PickerBaseMedia : Parcelable {

    val source: Uri
}
