package com.dewarder.pickerkit.core

interface PickerKit {

    fun <S : PickerStarter> openPicker(key: Picker<S, *>): S

    fun openChooser(): ChooserStarter

    fun listenResults(): ListenerResultBuilder

    fun provideResults(): ProviderResultBuilder

    companion object {

        fun getInstance(): PickerKit {
            TODO()
        }

        fun setDefaultInstance(pickerKit: PickerKit) {

        }
    }
}