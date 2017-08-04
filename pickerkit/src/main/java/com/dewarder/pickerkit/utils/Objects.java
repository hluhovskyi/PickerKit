package com.dewarder.pickerkit.utils;

public final class Objects {

    private Objects() {
        throw new UnsupportedOperationException();
    }

    public static void requireNonNull(Object... objects) {
        for (Object obj : objects) {
            if (obj == null) {
                throw new NullPointerException();
            }
        }
    }
}
