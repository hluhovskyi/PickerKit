package com.dewarder.pickerkit.core.impl

import android.app.Activity
import com.dewarder.pickerkit.core.ChooserStarter
import com.dewarder.pickerkit.core.Picker

internal class DefaultChooserStarter(
        val activity: Activity
) : ChooserStarter {

    override fun only(vararg pickers: Picker<*, *>) = apply {

    }

    override fun excluding(vararg picker: Picker<*, *>) = apply {

    }

    override fun start() {

    }
}