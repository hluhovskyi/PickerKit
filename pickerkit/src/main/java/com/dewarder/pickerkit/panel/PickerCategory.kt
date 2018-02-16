package com.dewarder.pickerkit.panel

import android.os.Parcelable
import android.support.annotation.ColorInt
import android.support.annotation.DrawableRes
import android.support.annotation.IdRes
import kotlinx.android.parcel.Parcelize

@Parcelize
data class PickerCategory(
        @IdRes val id: Int,
        val name: String,
        @ColorInt val color: Int,
        @DrawableRes val icon: Int
) : Parcelable