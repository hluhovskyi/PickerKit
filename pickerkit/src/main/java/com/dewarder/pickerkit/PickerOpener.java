package com.dewarder.pickerkit;

import android.app.Activity;
import android.support.annotation.IdRes;
import android.support.annotation.NonNull;

import com.dewarder.pickerkit.config.PickerConfig;

public interface PickerOpener {

    boolean canOpen(@NonNull Activity activity, @IdRes int pickerId);

    int open(@NonNull Activity activity, @IdRes int pickerId, @NonNull PickerConfig config);
}
