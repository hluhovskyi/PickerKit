package com.dewarder.pickerkit.config;

import android.os.Parcel;
import android.os.Parcelable;
import android.support.annotation.NonNull;

public final class PickerDataConfig implements Parcelable {

    private static final PickerDataConfig DEFAULT_INSTANCE;

    private final PickerDataImageConfig imageConfig;
    private final PickerDataVideoConfig videoConfig;
    private final int galleryLimit;

    static {
        DEFAULT_INSTANCE = new PickerDataConfig(
                PickerDataImageConfig.defaultInstance(),
                PickerDataVideoConfig.defaultInstance(),
                Integer.MAX_VALUE);
    }

    private PickerDataConfig(PickerDataImageConfig imageConfig,
                             PickerDataVideoConfig videoConfig,
                             int galleryLimit) {

        this.imageConfig = imageConfig;
        this.videoConfig = videoConfig;
        this.galleryLimit = galleryLimit;
    }

    private PickerDataConfig(Parcel in) {
        imageConfig = in.readParcelable(PickerDataImageConfig.class.getClassLoader());
        videoConfig = in.readParcelable(PickerDataVideoConfig.class.getClassLoader());
        galleryLimit = in.readInt();
    }

    public static PickerDataConfig defaultInstance() {
        return DEFAULT_INSTANCE;
    }

    @NonNull
    public PickerDataImageConfig getImageConfig() {
        return imageConfig;
    }

    @NonNull
    public PickerDataVideoConfig getVideoConfig() {
        return videoConfig;
    }

    public int getGalleryLimit() {
        return galleryLimit;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeParcelable(imageConfig, flags);
        dest.writeParcelable(videoConfig, flags);
        dest.writeInt(galleryLimit);
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
