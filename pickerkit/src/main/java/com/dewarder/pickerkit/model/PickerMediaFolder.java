package com.dewarder.pickerkit.model;

import android.net.Uri;
import android.os.Parcel;
import android.os.Parcelable;
import android.support.annotation.NonNull;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public final class PickerMediaFolder implements PickerBaseMedia, Parcelable {

    private final Uri source;
    private final List<PickerMedia> children;
    private final int itemCount;

    private PickerMediaFolder(Uri source, List<PickerMedia> children, int itemCount) {
        this.source = source;
        this.children = children;
        this.itemCount = itemCount;
    }

    @SuppressWarnings("unchecked")
    private PickerMediaFolder(Parcel in) {
        this.source = in.readParcelable(Uri.class.getClassLoader());
        this.children = in.createTypedArrayList(PickerMedia.CREATOR);
        this.itemCount = in.readInt();
    }

    @NonNull
    public static PickerMediaFolder fromMedia(@NonNull List<PickerMedia> media) {
        if (media.isEmpty()) {
            throw new IllegalStateException("Media can't be empty");
        }

        List<PickerMedia> copy = Collections.unmodifiableList(new ArrayList<>(media));
        Uri source = copy.get(0).getSource();
        String rawSource = source.toString();
        String lastSegment = source.getLastPathSegment();
        return new PickerMediaFolder(Uri.parse(rawSource.substring(rawSource.length() - lastSegment.length())), copy, copy.size());
    }

    @NonNull
    @Override
    public Uri getSource() {
        return source;
    }

    @NonNull
    public String getName() {
        return source.getLastPathSegment();
    }

    @NonNull
    public List<PickerMedia> getChildren() {
        return children;
    }

    public int getItemCount() {
        return itemCount;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeParcelable(source, flags);
        dest.writeTypedList(children);
        dest.writeInt(itemCount);
    }

    public static final Creator<PickerMediaFolder> CREATOR = new Creator<PickerMediaFolder>() {
        @Override
        public PickerMediaFolder createFromParcel(Parcel source) {
            return new PickerMediaFolder(source);
        }

        @Override
        public PickerMediaFolder[] newArray(int size) {
            return new PickerMediaFolder[size];
        }
    };
}
