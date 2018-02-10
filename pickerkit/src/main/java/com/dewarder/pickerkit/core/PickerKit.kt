package com.dewarder.pickerkit.core

import android.app.Application
import com.dewarder.pickerkit.core.impl.DefaultPickerKit

interface PickerKit {

    fun <S : PickerStarter> openPicker(key: Picker<S, *>): S

    fun openChooser(): ChooserStarter

    fun listenResults(): ListenerResultBuilder

    fun provideResults(): ProviderResultBuilder

    companion object {

        private var current: PickerKit? = null

        fun getInstance(): PickerKit = current!!

        fun init(application: Application) {
            current = DefaultPickerKit(
                    application = application
            )
        }

        fun setDefaultInstance(pickerKit: PickerKit) {
            current = pickerKit
        }
    }
}