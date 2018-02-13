package com.dewarder.pickerkit.core.impl

import com.dewarder.pickerkit.core.OnPickerResultListener
import com.dewarder.pickerkit.core.Result
import com.dewarder.pickerkit.utils.checkMainThread
import java.util.concurrent.ConcurrentHashMap

internal class Bus {

    private val listenersByType = ConcurrentHashMap<Class<out Result>, HashSet<OnPickerResultListener<Result>>>()
    private val listenersAny = HashSet<OnPickerResultListener<Result>>()

    fun post(result: Result) {
        checkMainThread()

        val resultClass = result::class.java
        val listeners = listenersByType.filterKeys { it.isAssignableFrom(resultClass) }
                .values
                .flatten()

        if (listeners.isEmpty() && listenersAny.isEmpty()) {
            throw IllegalStateException("No listeners corresponding to result ${resultClass.simpleName}")
        }

        listeners.forEach { listener ->
            listener.onResult(result)
        }

        listenersAny.forEach { listener ->
            listener.onResult(result)
        }
    }

    fun subscribeOnType(listeners: Map<Class<out Result>, Set<OnPickerResultListener<Result>>>) {
        listenersByType.putAll(
                listeners.mapValues { (_, values) ->
                    values.toHashSet()
                }
        )
    }

    fun subscribeOnAny(listeners: Set<OnPickerResultListener<Result>>) {
        listenersAny += listeners
    }
}