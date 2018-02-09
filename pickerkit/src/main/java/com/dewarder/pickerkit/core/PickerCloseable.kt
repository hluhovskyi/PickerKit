package com.dewarder.pickerkit.core

import java.io.Closeable

interface PickerCloseable : Closeable {

    override fun close()
}