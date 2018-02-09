package com.dewarder.pickerkit.core

interface ChooserStarter {

    fun only(vararg pickers: Picker<*, *>): ChooserStarter

    fun excluding(vararg picker: Picker<*, *>): ChooserStarter

    fun start()
}