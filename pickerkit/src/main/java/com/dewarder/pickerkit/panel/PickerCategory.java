package com.dewarder.pickerkit.panel;

import android.os.Parcel;
import android.os.Parcelable;
import android.support.annotation.ColorInt;
import android.support.annotation.DrawableRes;
import android.support.annotation.IdRes;

public final class PickerCategory implements Parcelable {

    private final int mId;
    private final String mName;
    private final int mColor;
    private final int mIcon;

    private PickerCategory(@IdRes int id, String name, @ColorInt int color, @DrawableRes int icon) {
        mId = id;
        mName = name;
        mColor = color;
        mIcon = icon;
    }

    private PickerCategory(Parcel in) {
        mId = in.readInt();
        mName = in.readString();
        mColor = in.readInt();
        mIcon = in.readInt();
    }

    public static PickerCategory of(@IdRes int id, String name, @ColorInt int color, @DrawableRes int icon) {
        return new PickerCategory(id, name, color, icon);
    }

    @IdRes
    public int getId() {
        return mId;
    }

    public String getName() {
        return mName;
    }

    @ColorInt
    public int getColor() {
        return mColor;
    }

    @DrawableRes
    public int getIcon() {
        return mIcon;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(mId);
        dest.writeString(mName);
        dest.writeInt(mColor);
        dest.writeInt(mIcon);
    }

    public static final Creator<PickerCategory> CREATOR = new Creator<PickerCategory>() {
        @Override
        public PickerCategory createFromParcel(Parcel in) {
            return new PickerCategory(in);
        }

        @Override
        public PickerCategory[] newArray(int size) {
            return new PickerCategory[size];
        }
    };
}
