package com.dewarder.pickerkit.config;

import android.os.Parcel;
import android.os.Parcelable;
import android.support.annotation.NonNull;

public final class PickerDataConfig implements Parcelable {

    private static final PickerDataConfig DEFAULT_INSTANCE;

    private final PickerDataPhotoConfig photoConfig;
    private final PickerDataVideoConfig videoConfig;

    static {
        DEFAULT_INSTANCE = new PickerDataConfig(
                PickerDataPhotoConfig.defaultInstance(),
                PickerDataVideoConfig.defaultInstance());
    }

    private PickerDataConfig(PickerDataPhotoConfig photoConfig, PickerDataVideoConfig videoConfig) {
        this.photoConfig = photoConfig;
        this.videoConfig = videoConfig;
    }

    private PickerDataConfig(Parcel in) {
        photoConfig = in.readParcelable(PickerDataPhotoConfig.class.getClassLoader());
        videoConfig = in.readParcelable(PickerDataVideoConfig.class.getClassLoader());
    }

    public static PickerDataConfig defaultInstance() {
        return DEFAULT_INSTANCE;
    }

    @NonNull
    public PickerDataPhotoConfig getPhotoConfig() {
        return photoConfig;
    }

    @NonNull
    public PickerDataVideoConfig getVideoConfig() {
        return videoConfig;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeParcelable(photoConfig, flags);
        dest.writeParcelable(videoConfig, flags);
    }

    public static final Creator<PickerDataConfig> CREATOR = new Creator<PickerDataConfig>() {
        @Override
        public PickerDataConfig createFromParcel(Parcel in) {
            return new PickerDataConfig(in);
        }

        @Override
        public PickerDataConfig[] newArray(int size) {
            return new PickerDataConfig[size];
        }
    };
}
