package com.dewarder.pickerkit.gallery.model

import android.net.Uri
import android.os.Parcelable
import kotlinx.android.parcel.Parcelize
import java.io.File

@Parcelize
data class PickerMedia internal constructor(
        override val source: Uri,
        private val mediaType: PickerMediaType,
        private val imageOptional: PickerImage?,
        private val videoOptional: PickerVideo?
) : PickerBaseMedia, Parcelable {

    val image: PickerImage
        get() = imageOptional ?: throw IllegalStateException(
                "There is no image in this media. Check by `isImage()` before")

    val video: PickerVideo
        get() = videoOptional ?: throw IllegalStateException(
                "There is no video in this media. Check by `isVideo()` before")

    val isImage: Boolean
        get() = mediaType == PickerMediaType.IMAGE

    val isVideo: Boolean
        get() = mediaType == PickerMediaType.VIDEO

    companion object {

        fun image(uri: Uri): PickerMedia = PickerMedia(
                source = uri,
                mediaType = PickerMediaType.IMAGE,
                imageOptional = PickerImage.fromUri(uri),
                videoOptional = null
        )

        fun image(file: File): PickerMedia = image(Uri.fromFile(file))

        fun video(uri: Uri): PickerMedia = PickerMedia(
                source = uri,
                mediaType = PickerMediaType.VIDEO,
                imageOptional = null,
                videoOptional = PickerVideo.fromUri(uri)
        )

        fun video(file: File): PickerMedia = video(Uri.fromFile(file))
    }
}
