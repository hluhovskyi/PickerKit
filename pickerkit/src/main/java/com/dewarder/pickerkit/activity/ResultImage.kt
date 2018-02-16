package com.dewarder.pickerkit.activity

import com.dewarder.pickerkit.model.PickerImage
import com.dewarder.pickerkit.result.PickerImageResult
import kotlinx.android.parcel.Parcelize


@Parcelize
internal data class ResultImage(
        override val selected: List<PickerImage>,
        override val unselected: List<PickerImage>,
        override val isSubmitted: Boolean,
        override val isCanceled: Boolean
) : PickerImageResult