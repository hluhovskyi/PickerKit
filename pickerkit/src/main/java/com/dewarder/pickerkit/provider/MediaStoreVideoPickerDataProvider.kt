package com.dewarder.pickerkit.provider

import android.content.ContentResolver
import android.content.Context
import android.provider.MediaStore
import com.dewarder.pickerkit.gallery.model.PickerVideo
import com.dewarder.pickerkit.utils.Queries
import java.io.File
import java.util.*
import java.util.concurrent.Executors

class MediaStoreVideoPickerDataProvider private constructor(context: Context, private val mExtensions: Array<String>?) : PickerDataProvider<PickerVideo> {

    private val mExecutor = Executors.newSingleThreadExecutor()
    private val mContentResolver: ContentResolver

    init {
        mContentResolver = context.contentResolver
    }

    override fun request(callback: PickerDataProvider.Callback<PickerVideo>) {
        mExecutor.submit {
            val cursor = Queries.newBuilder(mContentResolver)
                    .uri(MediaStore.Video.Media.EXTERNAL_CONTENT_URI)
                    .projection(MediaStore.Video.Media._ID, MediaStore.Video.Media.DATE_TAKEN, MediaStore.Video.Media.DATA)
                    .orderByDesc(MediaStore.Video.Media.DATE_TAKEN)
                    .execute()

            val videos = ArrayList<PickerVideo>()
            if (cursor.moveToFirst()) {
                val dataColumn = cursor.getColumnIndex(MediaStore.Video.Media.DATA)
                do {
                    val file = File(cursor.getString(dataColumn))
                    if (matchAnyExtension(file)) {
                        videos.add(PickerVideo.fromFile(file))
                    }
                } while (cursor.moveToNext())
            }
            callback.onNext(videos)
            callback.onComplete()
            cursor.close()
        }
    }

    private fun matchAnyExtension(file: File): Boolean {
        if (mExtensions == null) {
            return true
        }
        for (extension in mExtensions) {
            if (file.name.endsWith(extension)) {
                return true
            }
        }
        return false
    }

    class Builder(private val mContext: Context) {
        private var mExtensions: Array<String>? = null

        fun setExtensions(vararg extensions: String): Builder {
            mExtensions = arrayOf(*extensions)
            return this
        }

        fun build(): MediaStoreVideoPickerDataProvider {
            return MediaStoreVideoPickerDataProvider(mContext, mExtensions)
        }
    }

}
