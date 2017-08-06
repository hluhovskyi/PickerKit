package com.dewarder.pickerkit;

import android.app.Activity;
import android.support.annotation.NonNull;

import com.dewarder.pickerkit.config.PickerConfig;

public interface PickerOpenerAction {

    int open(@NonNull Activity activity, @NonNull PickerConfig config);
}
