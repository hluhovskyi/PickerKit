package com.dewarder.pickerkit.provider

import com.dewarder.pickerkit.gallery.model.PickerImage
import com.dewarder.pickerkit.gallery.model.PickerMedia
import com.dewarder.pickerkit.gallery.model.PickerVideo

object PickerMediaProviderWrapper {

    fun wrapImage(provider: PickerDataProvider<PickerImage>): PickerDataProvider<PickerMedia> =
            PickerMediaImageWrapper.wrap(provider)

    fun wrapVideo(provider: PickerDataProvider<PickerVideo>): PickerDataProvider<PickerMedia> =
            PickerMediaVideoWrapper.wrap(provider)
}
