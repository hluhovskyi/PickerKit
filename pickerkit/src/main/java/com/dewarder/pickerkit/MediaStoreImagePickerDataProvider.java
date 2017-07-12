package com.dewarder.pickerkit;

import android.content.ContentResolver;
import android.content.Context;
import android.database.Cursor;
import android.provider.MediaStore;

import java.io.File;
import java.util.ArrayList;

public final class MediaStoreImagePickerDataProvider implements PickerDataProvider<File> {

    private final ContentResolver mContentResolver;

    public MediaStoreImagePickerDataProvider(Context context) {
        mContentResolver = context.getContentResolver();
    }

    @Override
    public void request(Callback<File> callback) {
        Cursor cursor = Queries.newBuilder(mContentResolver)
                .uri(MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
                .projection(MediaStore.Images.Media._ID, MediaStore.Images.Media.DATE_TAKEN, MediaStore.Images.Media.DATA)
                .orderByDesc(MediaStore.Images.Media.DATE_TAKEN)
                .execute();

        ArrayList<File> files = new ArrayList<>();
        if (cursor.moveToFirst()) {
            int dataColumn = cursor.getColumnIndex(MediaStore.Images.Media.DATA);
            do {
                File file = new File(cursor.getString(dataColumn));
                files.add(file);
            } while (cursor.moveToNext());
        }
        callback.onNext(files);
        callback.onComplete();
        cursor.close();
    }
}
