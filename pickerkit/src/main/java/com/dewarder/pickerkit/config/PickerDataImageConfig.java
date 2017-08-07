package com.dewarder.pickerkit.config;

import android.os.Parcel;
import android.os.Parcelable;
import android.support.annotation.NonNull;

import com.dewarder.pickerkit.model.PickerImage;

import java.util.Collections;
import java.util.List;

public final class PickerDataImageConfig implements Parcelable {

    private static final PickerDataImageConfig DEFAULT_INSTANCE;

    private final List<PickerImage> selected;

    static {
        DEFAULT_INSTANCE = new PickerDataImageConfig(
                Collections.emptyList());
    }

    private PickerDataImageConfig(List<PickerImage> selected) {
        this.selected = selected;
    }

    protected PickerDataImageConfig(Parcel in) {
        selected = in.createTypedArrayList(PickerImage.CREATOR);
    }

    public static PickerDataImageConfig defaultInstance() {
        return DEFAULT_INSTANCE;
    }

    @NonNull
    public List<PickerImage> getSelected() {
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

    public static final Creator<PickerDataImageConfig> CREATOR = new Creator<PickerDataImageConfig>() {
        @Override
        public PickerDataImageConfig createFromParcel(Parcel in) {
            return new PickerDataImageConfig(in);
        }

        @Override
        public PickerDataImageConfig[] newArray(int size) {
            return new PickerDataImageConfig[size];
        }
    };
}
