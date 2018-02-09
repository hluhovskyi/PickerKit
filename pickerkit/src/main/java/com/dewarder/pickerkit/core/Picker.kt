package com.dewarder.pickerkit.core

import android.content.Context

interface Picker<out S : PickerStarter, out R : Result> {

    val resultType: Class<out R>

    /**
     * Here should be always Activity
     */
    fun provideStarter(context: Context): S
}