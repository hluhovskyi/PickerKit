package com.dewarder.pickerkit

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class ImmutablePoint(
        val x: Int,
        val y: Int
) : Parcelable {

    val isEmpty: Boolean
        get() = this == EMPTY

    companion object {

        private val EMPTY = ImmutablePoint(
                x = Integer.MIN_VALUE,
                y = Integer.MIN_VALUE
        )

        fun empty(): ImmutablePoint = EMPTY
    }
}
