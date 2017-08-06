package com.dewarder.pickerkit.model;

import android.net.Uri;
import android.os.Parcel;
import android.os.Parcelable;
import android.support.annotation.NonNull;

import com.dewarder.pickerkit.utils.Objects;

import java.io.File;

public final class PickerVideo implements Parcelable {

    private final Uri source;

    private PickerVideo(Uri source) {
        this.source = source;
    }

    private PickerVideo(Parcel in) {
        source = in.readParcelable(Uri.class.getClassLoader());
    }

    public static PickerVideo fromUri(@NonNull Uri uri) {
        Objects.requireNonNull(uri);
        return new PickerVideo(uri);
    }

    public static PickerVideo fromFile(@NonNull File file) {
        Objects.requireNonNull(file);
        return new PickerVideo(Uri.fromFile(file));
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

    public static final Creator<PickerVideo> CREATOR = new Creator<PickerVideo>() {
        @Override
        public PickerVideo createFromParcel(Parcel in) {
            return new PickerVideo(in);
        }

        @Override
        public PickerVideo[] newArray(int size) {
            return new PickerVideo[size];
        }
    };
}
