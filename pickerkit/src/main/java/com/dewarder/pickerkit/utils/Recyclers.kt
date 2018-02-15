package com.dewarder.pickerkit.utils

import android.support.v7.widget.RecyclerView

object Recyclers {

    fun calculateSpanCount(recyclerView: RecyclerView, minItemSize: Int): Int {
        return recyclerView.width / minItemSize
    }

    fun calculateItemSize(
            recyclerView: RecyclerView,
            spanCount: Int,
            minItemSize: Int,
            itemSpacing: Int
    ): Int {
        val recyclerWidth = recyclerView.width
        return (minItemSize + (recyclerWidth - minItemSize * spanCount - itemSpacing * (spanCount + 1)).toFloat() / spanCount).toInt()
    }
}
