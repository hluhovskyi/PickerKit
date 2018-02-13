package com.dewarder.pickerkit.core

import android.app.Application
import com.dewarder.pickerkit.core.impl.ActivityRegistry
import com.dewarder.pickerkit.core.impl.Bus
import com.dewarder.pickerkit.core.impl.DefaultPickerKit

interface PickerKit {

    fun <S : PickerStarter> openPicker(picker: Picker<S, *>): S

    fun <C : ChooserStarter> openChooser(
            chooser: Chooser<C>
    ): C

    fun listenResults(): ListenerResultBuilder

    fun provideResults(): ProviderResultBuilder

    companion object {

        private var current: PickerKit? = null

        fun getInstance(): PickerKit = current!!

        fun init(application: Application) {
            current = DefaultPickerKit(
                    application = application,
                    activityRegistry = ActivityRegistry(),
                    bus = Bus()
            )
        }

        fun setDefaultInstance(pickerKit: PickerKit) {
            current = pickerKit
        }
    }
}