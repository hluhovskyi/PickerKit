package com.dewarder.pickerkit.gallery.results

import com.dewarder.pickerkit.gallery.model.PickerVideo
import com.dewarder.pickerkit.result.PickerVideoResult
import kotlinx.android.parcel.Parcelize

@Parcelize
data class ResultVideo internal constructor(
        override val selected: List<PickerVideo>,
        override val unselected: List<PickerVideo>,
        override val isSubmitted: Boolean,
        override val isCanceled: Boolean
) : PickerVideoResult