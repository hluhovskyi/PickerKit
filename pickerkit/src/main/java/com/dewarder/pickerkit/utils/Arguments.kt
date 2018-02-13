package com.dewarder.pickerkit.utils

import android.app.Activity
import android.content.Intent
import android.os.Parcelable
import kotlin.properties.ReadOnlyProperty
import kotlin.reflect.KProperty

fun <T : Parcelable> Activity.argument(
        key: String? = null,
        default: T? = null
): ReadOnlyProperty<Activity, T> =
        Lazy { property ->
            val realKey = key ?: property.name
            intent?.getParcelableExtra(realKey)
                    ?: default
                    ?: throw IllegalStateException("Argument for key `$realKey` is absent. Pass value or set `default` one")
        }

fun Intent.putArgument(property: KProperty<*>, extra: Parcelable): Intent =
        this.putExtra(property.name, extra)

private class Lazy<out T : Parcelable>(
        val init: (KProperty<*>) -> T
) : ReadOnlyProperty<Any, T> {

    private object EMPTY

    private var value: Any? = EMPTY

    override fun getValue(thisRef: Any, property: KProperty<*>): T {
        if (value == EMPTY) {
            value = init(property)
        }
        return value as T
    }
}