package com.dewarder.pickerkit.config;

import android.os.Parcel;
import android.os.Parcelable;
import android.support.annotation.ColorInt;

import com.dewarder.pickerkit.utils.Colors;

public final class PickerConfig implements Parcelable {

    private static final PickerConfig DEFAULT_INSTANCE;

    private final int accentColor;

    static {
        DEFAULT_INSTANCE = new PickerConfig(
                Colors.defaultColor());
    }

    PickerConfig(int accentColor) {
        this.accentColor = accentColor;
    }

    private PickerConfig(Parcel in) {
        accentColor = in.readInt();
    }

    public static PickerConfig defaultInstance() {
        return DEFAULT_INSTANCE;
    }

    @ColorInt
    public int getAccentColor() {
        return accentColor;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(accentColor);
    }

    public static final Creator<PickerConfig> CREATOR = new Creator<PickerConfig>() {
        @Override
        public PickerConfig createFromParcel(Parcel in) {
            return new PickerConfig(in);
        }

        @Override
        public PickerConfig[] newArray(int size) {
            return new PickerConfig[size];
        }
    };
}
