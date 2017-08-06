package com.dewarder.pickerkit.model;

import android.net.Uri;
import android.os.Parcel;
import android.os.Parcelable;
import android.support.annotation.NonNull;

import com.dewarder.pickerkit.utils.Objects;

import java.io.File;

public final class PickerPhoto implements Parcelable {

    private final Uri source;

    private PickerPhoto(Uri source) {
        this.source = source;
    }

    private PickerPhoto(Parcel in) {
        source = in.readParcelable(Uri.class.getClassLoader());
    }

    public static PickerPhoto fromUri(@NonNull Uri uri) {
        Objects.requireNonNull(uri);
        return new PickerPhoto(uri);
    }

    public static PickerPhoto fromFile(@NonNull File file) {
        Objects.requireNonNull(file);
        return new PickerPhoto(Uri.fromFile(file));
    }

    @NonNull
    public Uri getSource() {
        return source;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeParcelable(source, flags);
    }

    public static final Creator<PickerPhoto> CREATOR = new Creator<PickerPhoto>() {
        @Override
        public PickerPhoto createFromParcel(Parcel in) {
            return new PickerPhoto(in);
        }

        @Override
        public PickerPhoto[] newArray(int size) {
            return new PickerPhoto[size];
        }
    };
}
