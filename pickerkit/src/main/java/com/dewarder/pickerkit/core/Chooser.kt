package com.dewarder.pickerkit.core

import android.content.Context

interface Chooser<out C : ChooserStarter> {

    fun provideStarter(context: Context): C
}