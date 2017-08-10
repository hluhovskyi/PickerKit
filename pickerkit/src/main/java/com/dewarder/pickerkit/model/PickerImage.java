package com.dewarder.pickerkit.model;

import android.net.Uri;
import android.os.Parcel;
import android.os.Parcelable;
import android.support.annotation.NonNull;

import com.dewarder.pickerkit.utils.Objects;

import java.io.File;

public final class PickerImage implements PickerBaseMedia, Parcelable {

    private final Uri source;

    private PickerImage(Uri source) {
        this.source = source;
    }

    private PickerImage(Parcel in) {
        source = in.readParcelable(Uri.class.getClassLoader());
    }

    public static PickerImage fromUri(@NonNull Uri uri) {
        Objects.requireNonNull(uri);
        return new PickerImage(uri);
    }

    public static PickerImage fromFile(@NonNull File file) {
        Objects.requireNonNull(file);
        return new PickerImage(Uri.fromFile(file));
    }

    @NonNull
    @Override
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

    public static final Creator<PickerImage> CREATOR = new Creator<PickerImage>() {
        @Override
        public PickerImage createFromParcel(Parcel in) {
            return new PickerImage(in);
        }

        @Override
        public PickerImage[] newArray(int size) {
            return new PickerImage[size];
        }
    };
}
