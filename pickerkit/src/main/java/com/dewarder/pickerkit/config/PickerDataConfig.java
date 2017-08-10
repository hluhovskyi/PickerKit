package com.dewarder.pickerkit.config;

import android.os.Parcel;
import android.os.Parcelable;
import android.support.annotation.IntRange;
import android.support.annotation.NonNull;

import com.dewarder.pickerkit.model.PickerImage;
import com.dewarder.pickerkit.model.PickerVideo;

import java.util.Collections;
import java.util.List;

public final class PickerDataConfig implements Parcelable {

    private static final PickerDataConfig DEFAULT_INSTANCE;

    private final int galleryLimit;
    private final List<PickerImage> selectedImages;
    private final List<PickerVideo> selectedVideos;

    static {
        DEFAULT_INSTANCE = new PickerDataConfig(
                Integer.MAX_VALUE,
                Collections.emptyList(),
                Collections.emptyList());
    }

    PickerDataConfig(int galleryLimit,
                     List<PickerImage> selectedImages,
                     List<PickerVideo> selectedVideos) {

        this.galleryLimit = galleryLimit;
        this.selectedImages = selectedImages;
        this.selectedVideos = selectedVideos;
    }

    private PickerDataConfig(Parcel in) {
        galleryLimit = in.readInt();
        selectedImages = in.createTypedArrayList(PickerImage.CREATOR);
        selectedVideos = in.createTypedArrayList(PickerVideo.CREATOR);
    }

    public static PickerDataConfig defaultInstance() {
        return DEFAULT_INSTANCE;
    }

    @IntRange(from = 0, to = Integer.MAX_VALUE)
    public int getGalleryLimit() {
        return galleryLimit;
    }

    @NonNull
    public List<PickerImage> getSelectedImages() {
        return selectedImages;
    }

    @NonNull
    public List<PickerVideo> getSelectedVideos() {
        return selectedVideos;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(galleryLimit);
        dest.writeTypedList(selectedImages);
        dest.writeTypedList(selectedVideos);
    }

    @Override
    public int describeContents() {
        return 0;
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
