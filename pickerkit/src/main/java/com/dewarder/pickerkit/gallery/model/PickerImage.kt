package com.dewarder.pickerkit.gallery.model

import android.net.Uri
import android.os.Parcelable
import kotlinx.android.parcel.Parcelize
import java.io.File

@Parcelize
data class PickerImage(
        override val source: Uri
) : PickerBaseMedia, Parcelable {

    companion object {

        fun fromUri(uri: Uri): PickerImage =
                PickerImage(uri)

        fun fromFile(file: File): PickerImage =
                PickerImage(Uri.fromFile(file))
    }
}
