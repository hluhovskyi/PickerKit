package com.dewarder.pickerkit.gallery.model

import android.net.Uri
import android.os.Parcelable
import kotlinx.android.parcel.Parcelize
import java.io.File

@Parcelize
data class PickerVideo(
        override val source: Uri
) : PickerBaseMedia, Parcelable {

    companion object {

        fun fromUri(uri: Uri): PickerVideo = PickerVideo(uri)

        fun fromFile(file: File): PickerVideo = PickerVideo(Uri.fromFile(file))
    }
}
