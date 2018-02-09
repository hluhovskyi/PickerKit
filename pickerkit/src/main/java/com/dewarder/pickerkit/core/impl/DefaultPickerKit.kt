package com.dewarder.pickerkit.core.impl

import android.app.Activity
import android.app.Application
import android.arch.lifecycle.Lifecycle
import com.dewarder.pickerkit.core.*
import java.io.Closeable
import java.util.concurrent.ConcurrentHashMap

internal class DefaultPickerKit(
        application: Application
) : PickerKit {

    private val activityRegistry = ActivityRegistry()

    private val listeners = ConcurrentHashMap<Picker<*, *>, HashSet<Pair<OnPickerResultListener<*>>>()
    private val allListeners

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

    private inner class ListenerBuilder : ListenerResultBuilder {

        private val listenersAll = HashSet<OnPickerResultListener<*>>()
        private val listeners = HashMap<Picker<*, *>, OnPickerResultListener<*>>()

        override fun <R : Result, L : OnPickerResultListener<R>> observe(
                picker: Picker<*, R>,
                listener: L
        ): ListenerResultBuilder = apply {
            listeners[picker] = listener
        }

        override fun <R : Result> observe(
                picker: Picker<*, R>,
                listener: (R) -> Unit
        ): ListenerResultBuilder = apply {
            listeners[picker] = OnPickerResultListener.create(listener)
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

        override fun build(): Closeable {

        }
    }

    private inner class ProviderBuilder : ProviderResultBuilder {

    }
}