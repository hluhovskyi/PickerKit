package com.dewarder.pickerkit.config;

import android.support.annotation.ColorInt;

import com.dewarder.pickerkit.utils.Colors;

public final class PickerConfigBuilder {

    private int accentColor = Colors.emptyColor();

    public PickerConfigBuilder setAccentColor(@ColorInt int accentColor) {
        this.accentColor = accentColor;
        return this;
    }

    public PickerConfig build() {
        return new PickerConfig(accentColor);
    }
}
