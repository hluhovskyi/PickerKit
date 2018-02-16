package com.dewarder.pickerkit.utils

import android.app.Activity
import android.content.Intent
import android.os.Parcelable
import kotlin.properties.ReadWriteProperty
import kotlin.reflect.KProperty

//For now same implementation but separate declaration
inline fun <reified T> Activity.argumentMutable(
        key: String? = null,
        default: T? = null
): ReadWriteProperty<Activity, T> = argument(key, default)

inline fun <reified T> Activity.argument(
        key: String? = null,
        default: T? = null
): ReadWriteProperty<Activity, T> =
        Lazy { property ->
            val realKey = key ?: property.name
            intent?.getArgument<T>(realKey)
                    ?: default
                    ?: throw IllegalStateException("Argument for key `$realKey` is absent. Pass value or set `default` one")
        }

fun Intent.putArgument(property: KProperty<*>, extra: Any): Intent = apply {
    val key = property.name
    when (extra) {
        is Parcelable -> putExtra(key, extra)
        is Int -> putExtra(key, extra)
        is String -> putExtra(key, extra)
        is ArrayList<*> -> putExtra(key, extra)
        else -> throw IllegalStateException("Unsupported argument type `${extra::class.java.simpleName}` for key $key")
    }
}

inline fun <reified T> Intent.getArgument(key: String): T {
    val target = T::class.java
    return when {
        Parcelable::class.java.isAssignableFrom(target) -> getParcelableExtra<Parcelable>(key) as T
        Integer::class.java.isAssignableFrom(target) -> getIntExtra(key, -1) as T
        String::class.java.isAssignableFrom(target) -> getStringExtra(key) as T
        ArrayList::class.java.isAssignableFrom(target) -> getSerializableExtra(key) as T
        else -> throw IllegalStateException("Unsupported argument type ${target.simpleName} for key $key")
    }
}

@Suppress("unchecked_cast")
class Lazy<T>(
        private val init: (KProperty<*>) -> T
) : ReadWriteProperty<Any, T> {

    private object EMPTY

    private var value: Any? = EMPTY

    override fun getValue(thisRef: Any, property: KProperty<*>): T {
        if (value == EMPTY) {
            value = init(property)
        }
        return value as T
    }

    override fun setValue(thisRef: Any, property: KProperty<*>, value: T) {
        this.value = value
    }
}