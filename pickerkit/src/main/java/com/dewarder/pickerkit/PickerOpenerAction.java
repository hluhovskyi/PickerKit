package com.dewarder.pickerkit;

import android.app.Activity;
import android.support.annotation.NonNull;

import com.dewarder.pickerkit.config.PickerConfig;

public interface PickerOpenerAction {

    void open(@NonNull Activity activity, @NonNull PickerConfig config);
}
