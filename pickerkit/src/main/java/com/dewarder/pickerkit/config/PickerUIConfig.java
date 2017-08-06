package com.dewarder.pickerkit.config;

import android.os.Parcel;
import android.os.Parcelable;
import android.support.annotation.ColorInt;

import com.dewarder.pickerkit.utils.Colors;

public final class PickerUIConfig implements Parcelable {

    private static final PickerUIConfig DEFAULT_INSTANCE;

    private final int accentColor;

    static {
        DEFAULT_INSTANCE = new PickerUIConfig(
                Colors.defaultColor());
    }

    private PickerUIConfig(int accentColor) {
        this.accentColor = accentColor;
    }

    private PickerUIConfig(Parcel in) {
        accentColor = in.readInt();
    }

    public static PickerUIConfig defaultInstance() {
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

    public static final Creator<PickerUIConfig> CREATOR = new Creator<PickerUIConfig>() {
        @Override
        public PickerUIConfig createFromParcel(Parcel in) {
            return new PickerUIConfig(in);
        }

        @Override
        public PickerUIConfig[] newArray(int size) {
            return new PickerUIConfig[size];
        }
    };
}
