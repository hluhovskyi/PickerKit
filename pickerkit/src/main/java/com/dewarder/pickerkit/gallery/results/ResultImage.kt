package com.dewarder.pickerkit.gallery.results

import com.dewarder.pickerkit.gallery.model.PickerImage
import com.dewarder.pickerkit.result.PickerImageResult
import kotlinx.android.parcel.Parcelize


@Parcelize
internal data class ResultImage(
        override val selected: List<PickerImage>,
        override val unselected: List<PickerImage>,
        override val isSubmitted: Boolean,
        override val isCanceled: Boolean
) : PickerImageResult