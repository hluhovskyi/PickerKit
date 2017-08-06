package com.dewarder.pickerkit;

import android.app.Activity;
import android.support.annotation.IdRes;
import android.support.annotation.NonNull;

import com.dewarder.pickerkit.config.PickerConfig;
import com.dewarder.pickerkit.utils.Objects;

public final class DefaultPickerOpener implements PickerOpener {

    private final int id;
    private final PickerOpenerAction action;

    private DefaultPickerOpener(int id, PickerOpenerAction action) {
        this.id = id;
        this.action = action;
    }

    public static DefaultPickerOpener of(@IdRes int id, @NonNull PickerOpenerAction action) {
        Objects.requireNonNull(action);
        return new DefaultPickerOpener(id, action);
    }

    @Override
    public boolean canOpen(@NonNull Activity activity, @IdRes int pickerId) {
        return pickerId == id;
    }

    @Override
    public void open(@NonNull Activity activity, @IdRes int pickerId, @NonNull PickerConfig config) {
        Objects.requireNonNull(activity, config);
        if (pickerId == id) {
            action.open(activity, config);
        }
    }
}
