package com.dewarder.pickerkit.config;

import android.os.Parcel;
import android.os.Parcelable;
import android.support.annotation.NonNull;

public final class PickerConfig implements Parcelable {

    private static final PickerConfig DEFAULT_INSTANCE;

    private final PickerUIConfig uiConfig;
    private final PickerDataConfig dataConfig;

    static {
        DEFAULT_INSTANCE = new PickerConfig(
                PickerUIConfig.defaultInstance(),
                PickerDataConfig.defaultInstance());
    }

    private PickerConfig(PickerUIConfig uiConfig, PickerDataConfig dataConfig) {
        this.uiConfig = uiConfig;
        this.dataConfig = dataConfig;
    }

    private PickerConfig(Parcel in) {
        uiConfig = in.readParcelable(PickerUIConfig.class.getClassLoader());
        dataConfig = in.readParcelable(PickerDataConfig.class.getClassLoader());
    }

    public static PickerConfig defaultInstance() {
        return DEFAULT_INSTANCE;
    }

    @NonNull
    public PickerUIConfig getUiConfig() {
        return uiConfig;
    }

    @NonNull
    public PickerDataConfig getDataConfig() {
        return dataConfig;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeParcelable(uiConfig, flags);
        dest.writeParcelable(dataConfig, flags);
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
