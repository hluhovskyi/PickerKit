package com.dewarder.pickerkit.config;

import android.os.Parcel;
import android.os.Parcelable;

import com.dewarder.pickerkit.ImmutablePoint;
import com.dewarder.pickerkit.panel.PickerCategory;

import java.util.List;

public final class PanelPickerConfig implements Parcelable {

    private final ImmutablePoint startPoint;
    private final List<PickerCategory> categories;

    PanelPickerConfig(PanelPickerBuilder builder) {
        startPoint = builder.getStartPoint();
        categories = builder.getCategories();
    }

    private PanelPickerConfig(Parcel in) {
        startPoint = in.readParcelable(ImmutablePoint.class.getClassLoader());
        categories = in.createTypedArrayList(PickerCategory.CREATOR);
    }

    public ImmutablePoint getStartPoint() {
        return startPoint;
    }

    public List<PickerCategory> getCategories() {
        return categories;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeParcelable(startPoint, flags);
        dest.writeTypedList(categories);
    }

    public static final Creator<PanelPickerConfig> CREATOR = new Creator<PanelPickerConfig>() {
        @Override
        public PanelPickerConfig createFromParcel(Parcel in) {
            return new PanelPickerConfig(in);
        }

        @Override
        public PanelPickerConfig[] newArray(int size) {
            return new PanelPickerConfig[size];
        }
    };

}
