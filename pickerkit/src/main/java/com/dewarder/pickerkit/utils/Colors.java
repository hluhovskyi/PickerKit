package com.dewarder.pickerkit.utils;

import android.content.Context;
import android.support.annotation.ColorInt;
import android.support.annotation.NonNull;
import android.support.v4.content.ContextCompat;

import com.dewarder.pickerkit.R;

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

    @ColorInt
    public static int accentColor(Context context) {
        return ContextCompat.getColor(context, R.color.colorAccent);
    }

    public static boolean isPresent(@ColorInt int color) {
        return color != NO_COLOR;
    }

    public static boolean isDefault(@ColorInt int color) {
        return color == DEFAULT_COLOR;
    }

    public static boolean isAbsentOrDefault(@ColorInt int color) {
        return !isPresent(color) || isDefault(color);
    }

    @ColorInt
    public static int orElse(@ColorInt int color, @ColorInt int elseColor) {
        return isAbsentOrDefault(color) ? elseColor : color;
    }

    @ColorInt
    public static int orElseAccent(@NonNull Context context, @ColorInt int color) {
        Objects.requireNonNull(context);
        return orElse(color, accentColor(context));
    }
}
