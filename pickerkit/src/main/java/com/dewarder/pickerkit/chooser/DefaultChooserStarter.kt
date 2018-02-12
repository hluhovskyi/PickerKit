package com.dewarder.pickerkit.chooser

import android.app.Activity
import android.content.Context
import com.dewarder.pickerkit.activity.PanelPickerActivity
import com.dewarder.pickerkit.config.PanelPickerBuilder
import com.dewarder.pickerkit.config.PickerConfigBuilder
import com.dewarder.pickerkit.core.ChooserStarter
import com.dewarder.pickerkit.core.Picker

class DefaultChooserStarter(
        val context: Context
) : ChooserStarter {

    override fun only(vararg pickers: Picker<*, *>) = apply {

    }

    override fun excluding(vararg picker: Picker<*, *>) = apply {

    }

    override fun start() {
        PanelPickerActivity.start(
                context as Activity,
                PanelPickerBuilder().build()
        )
    }
}