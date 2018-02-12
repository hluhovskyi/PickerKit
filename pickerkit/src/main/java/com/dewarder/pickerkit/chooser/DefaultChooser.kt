package com.dewarder.pickerkit.chooser

import android.content.Context
import com.dewarder.pickerkit.core.Chooser

object DefaultChooser : Chooser<DefaultChooserStarter> {

    override fun provideStarter(context: Context) =
            DefaultChooserStarter(context = context)
}