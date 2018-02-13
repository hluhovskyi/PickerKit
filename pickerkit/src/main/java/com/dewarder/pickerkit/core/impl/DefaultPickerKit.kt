package com.dewarder.pickerkit.core.impl

import android.app.Application
import com.dewarder.pickerkit.core.*

internal class DefaultPickerKit(
        application: Application,
        private val activityRegistry: ActivityRegistry,
        private val bus: Bus
) : PickerKit {

    init {
        application.registerActivityLifecycleCallbacks(activityRegistry)
    }

    override fun <S : PickerStarter> openPicker(picker: Picker<S, *>): S =
            picker.provideStarter(context = activityRegistry.getCurrentActivity())

    override fun <C : ChooserStarter> openChooser(chooser: Chooser<C>): C =
            chooser.provideStarter(context = activityRegistry.getCurrentActivity())

    override fun listenResults(): ListenerResultBuilder =
            ListenerBuilder(
                    bus = bus,
                    activityRegistry = activityRegistry
            )

    override fun provideResults(): ProviderResultBuilder =
            ProviderBuilder(bus = bus)

    internal class ProviderBuilder(
            val bus: Bus
    ) : ProviderResultBuilder {

        override fun push(result: Result) = apply {
            bus.post(result)
        }

        override fun commit() {
        }
    }
}