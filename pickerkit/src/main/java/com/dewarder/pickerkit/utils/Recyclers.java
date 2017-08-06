package com.dewarder.pickerkit.utils;

import android.support.v7.widget.RecyclerView;

public final class Recyclers {

    private Recyclers() {
        throw new UnsupportedOperationException();
    }

    public static int calculateSpanCount(RecyclerView recyclerView, int minItemSize) {
        return recyclerView.getWidth() / minItemSize;
    }

    public static int calculateItemSize(RecyclerView recyclerView, int spanCount, int minItemSize, int itemSpacing) {
        int recyclerWidth = recyclerView.getWidth();
        return (int) (minItemSize + (float) (recyclerWidth - minItemSize * spanCount - itemSpacing * (spanCount + 1)) / spanCount);
    }
}
