package com.dewarder.pickerkit

import android.app.Activity
import android.content.Context
import com.dewarder.pickerkit.activity.PickerGalleryFolderActivity
import com.dewarder.pickerkit.activity.ResultGallery
import com.dewarder.pickerkit.core.Picker
import com.dewarder.pickerkit.core.PickerStarter

object ImagePicker : Picker<ImagePickerStarter, ResultGallery> {

    override val resultType = ResultGallery::class.java

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