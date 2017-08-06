package com.dewarder.pickerkit.utils;

import android.support.annotation.ColorInt;

public final class Colors {

    private static final int NO_COLOR = -1;
    private static final int DEFAULT_COLOR = 0;

    private Colors() {
        throw new UnsupportedOperationException();
    }

    @ColorInt
    public static int emptyColor() {
        return NO_COLOR;
    }

    @ColorInt
    public static int defaultColor() {
        return DEFAULT_COLOR;
    }


    public static boolean isPresent(@ColorInt int color) {
        return color != NO_COLOR;
    }

    public static boolean isDefault(@ColorInt int color) {
        return color == DEFAULT_COLOR;
    }

    public static boolean isPresentOrDefault(@ColorInt int color) {
        return isPresent(color) || isDefault(color);
    }
}
