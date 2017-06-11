package com.dewarder.pickerok;

import android.graphics.BitmapFactory;

import java.io.File;

public final class ImagePostFilter implements FilesystemScannerPickerDataProvider.PostFilter {

    private static final ImagePostFilter INSTANCE = new ImagePostFilter();

    public static ImagePostFilter getInstance() {
        return INSTANCE;
    }

    private ImagePostFilter() {
    }

    @Override
    public boolean test(File file) {
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        BitmapFactory.decodeFile(file.getPath(), options);
        return options.outWidth != -1 && options.outHeight != -1;
    }
}
