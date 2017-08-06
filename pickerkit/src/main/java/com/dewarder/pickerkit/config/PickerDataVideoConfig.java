package com.dewarder.pickerkit.config;

import android.os.Parcel;
import android.os.Parcelable;

import com.dewarder.pickerkit.model.PickerVideo;

import java.util.Collections;
import java.util.List;

public final class PickerDataVideoConfig implements Parcelable {

    private static final PickerDataVideoConfig DEFAULT_INSTANCE;

    private final List<PickerVideo> selected;

    static {
        DEFAULT_INSTANCE = new PickerDataVideoConfig(
                Collections.emptyList());
    }

    private PickerDataVideoConfig(List<PickerVideo> selected) {
        this.selected = selected;
    }

    private PickerDataVideoConfig(Parcel in) {
        selected = in.createTypedArrayList(PickerVideo.CREATOR);
    }

    public static PickerDataVideoConfig defaultInstance() {
        return DEFAULT_INSTANCE;
    }

    public List<PickerVideo> getSelected() {
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

    public static final Creator<PickerDataVideoConfig> CREATOR = new Creator<PickerDataVideoConfig>() {
        @Override
        public PickerDataVideoConfig createFromParcel(Parcel in) {
            return new PickerDataVideoConfig(in);
        }

        @Override
        public PickerDataVideoConfig[] newArray(int size) {
            return new PickerDataVideoConfig[size];
        }
    };
}
