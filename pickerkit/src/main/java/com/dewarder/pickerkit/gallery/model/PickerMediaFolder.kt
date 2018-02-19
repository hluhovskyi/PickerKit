package com.dewarder.pickerkit.gallery.model

import android.net.Uri
import android.os.Parcelable
import com.dewarder.pickerkit.utils.withoutLastSegment
import kotlinx.android.parcel.Parcelize

@Parcelize
data class PickerMediaFolder private constructor(
        override val source: Uri,
        val children: List<PickerMedia>,
        val itemCount: Int
) : PickerBaseMedia, Parcelable {

    val name: String
        get() = source.lastPathSegment

    companion object {

        fun fromMedia(media: List<PickerMedia>): PickerMediaFolder {
            if (media.isEmpty()) {
                throw IllegalStateException("Media can't be empty")
            }

            val copy = media.toList()
            val source = copy.first().source

            return PickerMediaFolder(
                    source = source.withoutLastSegment(),
                    children = copy,
                    itemCount = copy.size
            )
        }
    }
}
