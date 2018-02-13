package com.dewarder.pickerkit

import android.app.Activity
import android.content.Context
import com.dewarder.pickerkit.activity.PickerGalleryFolderActivity
import com.dewarder.pickerkit.core.Picker
import com.dewarder.pickerkit.core.PickerStarter
import com.dewarder.pickerkit.result.PickerGalleryResult

object ImagePicker : Picker<ImagePickerStarter, PickerGalleryResult> {

    override val resultType = PickerGalleryResult::class.java

    override fun provideStarter(context: Context): ImagePickerStarter =
            ImagePickerStarter(context)
}

class ImagePickerStarter(
        val context: Context
) : PickerStarter {

    override fun start() {
        PickerGalleryFolderActivity.open(context as Activity)
    }
}