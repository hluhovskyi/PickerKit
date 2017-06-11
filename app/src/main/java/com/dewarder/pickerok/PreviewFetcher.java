package com.dewarder.pickerok;

import android.graphics.Bitmap;
import android.widget.ImageView;

public interface PreviewFetcher<T> {

    void fetchPreview(T from, Params params, ImageView target);

    class Params {

        private static final Params EMPTY = of(-1, -1);

        private final int mWidth;
        private final int mHeight;

        private Params(int width, int height) {
            mWidth = width;
            mHeight = height;
        }

        public static Params of(int width, int height) {
            return new Params(width, height);
        }

        public static Params empty() {
            return EMPTY;
        }

        public boolean isEmpty() {
            return this == empty();
        }

        public int getWidth() {
            return mWidth;
        }

        public int getHeight() {
            return mHeight;
        }
    }
}
