package com.dewarder.pickerkit.utils;

import android.support.annotation.NonNull;

import com.annimon.stream.Stream;
import com.annimon.stream.function.Consumer;

import java.lang.ref.WeakReference;
import java.util.Iterator;
import java.util.List;

public final class Lists {

    private Lists() {
        throw new UnsupportedOperationException();
    }

    public static <T> void addWeekReference(List<WeakReference<T>> list, T reference) {
        list.add(new WeakReference<>(reference));
    }

    public static <T> void removeWeekReference(List<WeakReference<T>> list, T reference) {
        for (Iterator<WeakReference<T>> iterator = list.iterator(); iterator.hasNext(); ) {
            WeakReference<T> ref = iterator.next();
            if (ref.get() == reference) {
                iterator.remove();
            }
        }
    }

    public static <T> void forEach(List<WeakReference<T>> list, @NonNull Consumer<T> consumer) {
        Stream.of(list)
                .map(WeakReference::get)
                .filter(value -> value != null)
                .forEach(consumer);
    }
}
