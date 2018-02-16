package com.dewarder.pickerkit.gallery.model

import android.net.Uri
import android.os.Parcelable

interface PickerBaseMedia : Parcelable {

    val source: Uri
}
