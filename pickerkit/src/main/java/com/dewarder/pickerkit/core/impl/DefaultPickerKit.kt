package com.dewarder.pickerkit.core.impl

import android.app.Activity
import android.app.Application
import android.arch.lifecycle.Lifecycle
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

    override fun listenResults(): ListenerResultBuilder = ListenerBuilder()

    override fun provideResults(): ProviderResultBuilder = ProviderBuilder()

    internal class ListenerBuilder(
            private val bus: Bus
    ) : ListenerResultBuilder {

        private val listenersAll = HashSet<OnPickerResultListener<Result>>()
        private val listenersByType = HashMap<Class<out Result>, HashSet<OnPickerResultListener<Result>>>()

        override fun <R : Result, L : OnPickerResultListener<R>> observe(
                picker: Picker<*, R>,
                listener: L
        ): ListenerResultBuilder = apply {
            val casted = listener as OnPickerResultListener<Result>

            val listeners = listenersByType[picker.resultType]
            if (listeners == null) {
                listenersByType[picker.resultType] = hashSetOf(casted)
            } else {
                listeners += casted
            }
        }

        override fun <R : Result> observe(
                picker: Picker<*, R>,
                listener: (R) -> Unit
        ): ListenerResultBuilder = apply {
            val wrapped = OnPickerResultListener.create(listener) as OnPickerResultListener<Result>

            val listeners = listenersByType[picker.resultType]
            if (listeners == null) {
                listenersByType[picker.resultType] = hashSetOf(wrapped)
            } else {
                listeners += wrapped
            }
        }

        override fun observeAll(
                listener: OnPickerResultListener<Result>
        ): ListenerResultBuilder = apply {
            listenersAll += listener
        }

        override fun observeAll(
                listener: (Result) -> Unit
        ): ListenerResultBuilder = apply {
            listenersAll += OnPickerResultListener.create(listener)
        }

        override fun attachToLifecycle(activity: Activity): ListenerResultBuilder = apply {
        }

        override fun attachToLifecycle(lifecycle: Lifecycle): ListenerResultBuilder = apply {
        }

        override fun build(): PickerCloseable {
            val listeners

            bus.subscribeOnType(
                    listeners = listenersByType
            )
            bus.subscribeOnAny(
                    listeners = listenersAll
            )
        }
    }

    internal class ProviderBuilder : ProviderResultBuilder {

    }
}