package com.dewarder.pickerok;

import android.content.ContentResolver;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.provider.MediaStore;

import java.io.File;
import java.util.ArrayList;

public class MediaStorePickerDataProvider implements PickerDataProvider<File> {

    private final ContentResolver mContentResolver;

    public MediaStorePickerDataProvider(Context context) {
        mContentResolver = context.getContentResolver();
    }

    @Override
    public void request(Callback<File> callback) {
        String[] projection = {
                MediaStore.Images.Media._ID,
                MediaStore.Images.Media.DATE_TAKEN,
                MediaStore.Images.Media.DATA
        };

        Uri images = MediaStore.Images.Media.EXTERNAL_CONTENT_URI;
        Cursor cur = mContentResolver.query(images, projection, null, null, MediaStore.Images.Media.DATE_TAKEN + " DESC");

        ArrayList<File> files = new ArrayList<>();
        if (cur.moveToFirst()) {
            int dataColumn = cur.getColumnIndex(MediaStore.Images.Media.DATA);
            do {
                files.add(new File(cur.getString(dataColumn)));
            } while (cur.moveToNext());
        }
        callback.onNext(files);
        callback.onComplete();
    }
}
