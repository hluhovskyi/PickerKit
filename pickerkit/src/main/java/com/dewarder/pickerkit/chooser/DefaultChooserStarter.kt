package com.dewarder.pickerkit.chooser

import com.dewarder.pickerkit.ImmutablePoint
import com.dewarder.pickerkit.core.ChooserStarter
import com.dewarder.pickerkit.core.Picker

interface DefaultChooserStarter : ChooserStarter {

    fun revealPoint(point: ImmutablePoint): DefaultChooserStarter

    override fun only(vararg pickers: Picker<*, *>): DefaultChooserStarter

    override fun excluding(vararg picker: Picker<*, *>): DefaultChooserStarter

    override fun start()
}