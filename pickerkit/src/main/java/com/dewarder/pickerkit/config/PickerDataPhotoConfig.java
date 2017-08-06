package com.dewarder.pickerkit.config;

import android.os.Parcel;
import android.os.Parcelable;
import android.support.annotation.NonNull;

import com.dewarder.pickerkit.model.PickerPhoto;

import java.util.Collections;
import java.util.List;

public final class PickerDataPhotoConfig implements Parcelable {

    private static final PickerDataPhotoConfig DEFAULT_INSTANCE;

    private final List<PickerPhoto> selected;

    static {
        DEFAULT_INSTANCE = new PickerDataPhotoConfig(
                Collections.emptyList());
    }

    private PickerDataPhotoConfig(List<PickerPhoto> selected) {
        this.selected = selected;
    }

    protected PickerDataPhotoConfig(Parcel in) {
        selected = in.createTypedArrayList(PickerPhoto.CREATOR);
    }

    public static PickerDataPhotoConfig defaultInstance() {
        return DEFAULT_INSTANCE;
    }

    @NonNull
    public List<PickerPhoto> getSelected() {
        return selected;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeTypedList(selected);
    }

    public static final Creator<PickerDataPhotoConfig> CREATOR = new Creator<PickerDataPhotoConfig>() {
        @Override
        public PickerDataPhotoConfig createFromParcel(Parcel in) {
            return new PickerDataPhotoConfig(in);
        }

        @Override
        public PickerDataPhotoConfig[] newArray(int size) {
            return new PickerDataPhotoConfig[size];
        }
    };
}
