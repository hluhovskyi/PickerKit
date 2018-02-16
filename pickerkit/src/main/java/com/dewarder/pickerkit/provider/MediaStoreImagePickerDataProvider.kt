package com.dewarder.pickerkit.provider

import android.content.Context
import android.provider.MediaStore
import com.dewarder.pickerkit.gallery.model.PickerImage
import com.dewarder.pickerkit.utils.Queries
import java.io.File

class MediaStoreImagePickerDataProvider private constructor(
        context: Context
) : PickerDataProvider<PickerImage> {

    private val contentResolver = context.contentResolver

    override fun request(callback: PickerDataProvider.Callback<PickerImage>) {
        val cursor = Queries.newBuilder(contentResolver)
                .uri(MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
                .projection(MediaStore.Images.Media._ID, MediaStore.Images.Media.DATE_TAKEN, MediaStore.Images.Media.DATA)
                .orderByDesc(MediaStore.Images.Media.DATE_TAKEN)
                .execute()

        val files = arrayListOf<PickerImage>()
        if (cursor.moveToFirst()) {
            val dataColumn = cursor.getColumnIndex(MediaStore.Images.Media.DATA)
            do {
                val file = File(cursor.getString(dataColumn))
                files.add(PickerImage.fromFile(file))
            } while (cursor.moveToNext())
        }
        callback.onNext(files)
        callback.onComplete()
        cursor.close()
    }

    companion object {

        fun of(context: Context): MediaStoreImagePickerDataProvider =
                MediaStoreImagePickerDataProvider(context)
    }
}
