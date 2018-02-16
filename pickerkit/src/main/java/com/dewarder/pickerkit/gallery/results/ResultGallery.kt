package com.dewarder.pickerkit.gallery.results

import com.dewarder.pickerkit.gallery.model.PickerMedia
import com.dewarder.pickerkit.gallery.model.PickerMediaType
import com.dewarder.pickerkit.result.PickerGalleryResult
import com.dewarder.pickerkit.result.PickerImageResult
import com.dewarder.pickerkit.result.PickerVideoResult
import kotlinx.android.parcel.Parcelize
import java.util.*

@Parcelize
data class ResultGallery(
        override val selected: List<PickerMedia>,
        override val unselected: List<PickerMedia>,
        override val isSubmitted: Boolean,
        override val isCanceled: Boolean
) : PickerGalleryResult {

    override val types: EnumSet<PickerMediaType>
        get() = EnumSet.noneOf(PickerMediaType::class.java)

    override fun getImageResult(): PickerImageResult =
            ResultImage(
                    selected.filter { it.isImage }.map { it.image },
                    unselected.filter { it.isImage }.map { it.image },
                    isSubmitted,
                    isCanceled
            )

    override fun getVideoResult(): PickerVideoResult =
            ResultVideo(
                    selected.filter { it.isVideo }.map { it.video },
                    unselected.filter { it.isVideo }.map { it.video },
                    isSubmitted,
                    isCanceled
            )

    override operator fun contains(type: PickerMediaType): Boolean = type in types

    companion object {

        internal fun of(checked: List<PickerMedia>, unchecked: List<PickerMedia>): ResultGallery =
                ResultGallery(
                        selected = checked,
                        unselected = unchecked,
                        isSubmitted = false,
                        isCanceled = false
                )

        internal fun submit(checked: List<PickerMedia>, unchecked: List<PickerMedia>): ResultGallery =
                ResultGallery(
                        selected = checked,
                        unselected = unchecked,
                        isSubmitted = true,
                        isCanceled = false
                )

        internal fun cancel(): ResultGallery =
                ResultGallery(
                        selected = emptyList(),
                        unselected = emptyList(),
                        isSubmitted = false,
                        isCanceled = true
                )
    }
}
