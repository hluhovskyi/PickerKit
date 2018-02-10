package com.dewarder.pickerkit.core.impl

import android.app.Application
import com.dewarder.pickerkit.core.*

internal class DefaultPickerKit(
        application: Application
) : PickerKit {

    private val activityRegistry = ActivityRegistry()
    private val bus = Bus()

    init {
        application.registerActivityLifecycleCallbacks(activityRegistry)
    }

    override fun <S : PickerStarter> openPicker(key: Picker<S, *>): S {
        return key.provideStarter(context = activityRegistry.getCurrentActivity())
    }

    override fun openChooser(): ChooserStarter = DefaultChooserStarter(
            activity = activityRegistry.getCurrentActivity()
    )

    override fun listenResults(): ListenerResultBuilder =
            ListenerBuilder(
                    bus = bus,
                    activityRegistry = activityRegistry
            )

    override fun provideResults(): ProviderResultBuilder =
            ProviderBuilder(
                    bus = bus
            )

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