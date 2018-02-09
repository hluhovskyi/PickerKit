package com.dewarder.pickerkit.core

import android.content.Context

interface Picker<out S : PickerStarter, R : Result> {

    /**
     * Here should be always Activity
     */
    fun provideStarter(context: Context): S
}