package com.dewarder.pickerkit;

import android.content.ContentResolver;
import android.content.Context;
import android.database.Cursor;
import android.provider.MediaStore;

import java.io.File;
import java.util.ArrayList;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class MediaStoreVideoPickerDataProvider implements PickerDataProvider<File> {

    private final ExecutorService mExecutor = Executors.newSingleThreadExecutor();
    private final Context mContext;
    private final ContentResolver mContentResolver;
    private final String[] mExtensions;

    private MediaStoreVideoPickerDataProvider(Context context, String[] extensions) {
        mContext = context;
        mContentResolver = context.getContentResolver();
        mExtensions = extensions;
    }

    @Override
    public void request(Callback<File> callback) {
        mExecutor.submit(() -> {
            Cursor cursor = Queries.newBuilder(mContentResolver)
                    .uri(MediaStore.Video.Media.EXTERNAL_CONTENT_URI)
                    .projection(MediaStore.Video.Media._ID, MediaStore.Video.Media.DATE_TAKEN, MediaStore.Video.Media.DATA)
                    .orderByDesc(MediaStore.Video.Media.DATE_TAKEN)
                    .execute();

            ArrayList<File> files = new ArrayList<>();
            if (cursor.moveToFirst()) {
                int dataColumn = cursor.getColumnIndex(MediaStore.Video.Media.DATA);
                do {
                    File file = new File(cursor.getString(dataColumn));
                    if (matchAnyExtension(file)) {
                        files.add(file);
                    }
                } while (cursor.moveToNext());
            }
            callback.onNext(files);
            callback.onComplete();
            cursor.close();
        });
    }

    private boolean matchAnyExtension(File file) {
        if (mExtensions == null) {
            return true;
        }
        for (String extension : mExtensions) {
            if (file.getName().endsWith(extension)) {
                return true;
            }
        }
        return false;
    }

    public static class Builder {

        private final Context mContext;
        private String[] mExtensions;

        public Builder(Context context) {
            mContext = context;
        }

        public Builder setExtensions(String... extensions) {
            mExtensions = extensions;
            return this;
        }

        public MediaStoreVideoPickerDataProvider build() {
            return new MediaStoreVideoPickerDataProvider(mContext, mExtensions);
        }
    }

}
