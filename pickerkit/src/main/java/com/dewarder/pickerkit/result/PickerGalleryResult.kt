package com.dewarder.pickerkit.result

import com.dewarder.pickerkit.core.Result
import com.dewarder.pickerkit.gallery.model.PickerMedia
import com.dewarder.pickerkit.gallery.model.PickerMediaType
import java.util.*

interface PickerGalleryResult : PickerBaseMediaResult<PickerMedia>, Result {

    fun getImageResult(): PickerImageResult

    fun getVideoResult(): PickerVideoResult

    val types: EnumSet<PickerMediaType>

    operator fun contains(type: PickerMediaType): Boolean
}
