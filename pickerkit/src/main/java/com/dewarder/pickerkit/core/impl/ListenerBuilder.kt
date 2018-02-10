package com.dewarder.pickerkit.core.impl

import android.app.Activity
import android.arch.lifecycle.GenericLifecycleObserver
import android.arch.lifecycle.Lifecycle
import com.dewarder.pickerkit.core.*

internal class ListenerBuilder(
        private val bus: Bus,
        private val activityRegistry: ActivityRegistry
) : ListenerResultBuilder {

    private val listenersAll = HashSet<OnPickerResultListener<Result>>()
    private val listenersByType = HashMap<Class<out Result>, HashSet<OnPickerResultListener<Result>>>()

    private var lifecycle: LifecycleRegistry? = null

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
        lifecycle.addObserver(GenericLifecycleObserver { source, event ->

        })
    }

    override fun build(): PickerCloseable {
        val lifecycle = lifecycle
        val listenersByType = if (lifecycle != null) {
            listenersByType.mapValues { (_, listeners) ->
                listeners.map {
                    LifecycleOnPickerResultListenerDecorator(
                            listener = it
                    )
                }.toSet()
            }
        } else {
            listenersByType.toMap() //Make copy
        }

        bus.subscribeOnType(
                listeners = listenersByType
        )
        bus.subscribeOnAny(
                listeners = listenersAll
        )

        return UnsubscribePickerCloseable(
                bus = bus,
                listenersAll = listenersAll,
                listenersByType = listenersByType
        )
    }

    private class UnsubscribePickerCloseable(
            val bus: Bus,
            val listenersAll: Set<OnPickerResultListener<Result>>,
            val listenersByType: Map<Class<out Result>, Set<OnPickerResultListener<Result>>>
    ) : PickerCloseable {

        override fun close() {
//TODO: Impl
        }
    }
}