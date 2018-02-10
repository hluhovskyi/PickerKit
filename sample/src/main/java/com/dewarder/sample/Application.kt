package com.dewarder.sample

import android.app.Application
import com.dewarder.pickerkit.core.PickerKit

class Application : Application() {

    override fun onCreate() {
        super.onCreate()
        PickerKit.init(this)
    }
}