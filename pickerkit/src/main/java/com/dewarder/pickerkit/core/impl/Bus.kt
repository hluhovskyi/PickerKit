package com.dewarder.pickerkit.core.impl

import com.dewarder.pickerkit.core.OnPickerResultListener
import com.dewarder.pickerkit.core.Result
import java.util.concurrent.ConcurrentHashMap

internal class Bus {

    private val listenersByType = ConcurrentHashMap<Class<out Result>, HashSet<OnPickerResultListener<Result>>>()
    private val listenersAny = HashSet<OnPickerResultListener<Result>>()

    //TODO: exception text
    fun post(result: Result) {
        val listeners = listenersByType.getOrElse(
                key = result::class.java,
                defaultValue = { throw IllegalStateException() }
        )

        listeners.forEach { listener ->
            listener.onResult(result)
        }

        listenersAny.forEach { listener ->
            listener.onResult(result)
        }
    }

    fun subscribeOnType(listeners: Map<Class<out Result>, Set<OnPickerResultListener<Result>>>) {
        listenersByType.putAll(listeners)
    }

    fun subscribeOnAny(listeners: Set<OnPickerResultListener<Result>>) {
        listenersAny += listeners
    }
}