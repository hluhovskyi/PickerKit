package com.dewarder.pickerkit.model;

import android.net.Uri;
import android.os.Parcel;
import android.os.Parcelable;
import android.support.annotation.NonNull;

import com.dewarder.pickerkit.utils.Objects;

import java.io.File;

public final class PickerMedia implements PickerBaseMedia, Parcelable {

    private final Uri source;
    private final PickerMediaType mediaType;
    private final PickerImage image;
    private final PickerVideo video;

    private PickerMedia(Uri source, PickerMediaType mediaType) {
        this.source = source;
        this.mediaType = mediaType;
        this.image = mediaType == PickerMediaType.IMAGE ? PickerImage.fromUri(source) : null;
        this.video = mediaType == PickerMediaType.VIDEO ? PickerVideo.fromUri(source) : null;
    }

    private PickerMedia(Parcel in) {
        source = in.readParcelable(Uri.class.getClassLoader());
        mediaType = (PickerMediaType) in.readSerializable();
        image = in.readParcelable(PickerImage.class.getClassLoader());
        video = in.readParcelable(PickerVideo.class.getClassLoader());
    }

    public static PickerMedia image(@NonNull Uri uri) {
        Objects.requireNonNull(uri);
        return new PickerMedia(uri, PickerMediaType.IMAGE);
    }

    public static PickerMedia image(@NonNull File file) {
        Objects.requireNonNull(file);
        return image(Uri.fromFile(file));
    }

    public static PickerMedia video(@NonNull Uri uri) {
        Objects.requireNonNull(uri);
        return new PickerMedia(uri, PickerMediaType.VIDEO);
    }

    public static PickerMedia video(@NonNull File file) {
        Objects.requireNonNull(file);
        return video(Uri.fromFile(file));
    }

    @NonNull
    @Override
    public Uri getSource() {
        return source;
    }

    @NonNull
    public PickerMediaType getMediaType() {
        return mediaType;
    }

    public boolean isImage() {
        return mediaType == PickerMediaType.IMAGE;
    }

    @NonNull
    public PickerImage getImage() {
        if (image == null) {
            throw new IllegalStateException("There is no image in this media. Check by `isImage()` before");
        }
        return image;
    }

    public boolean isVideo() {
        return mediaType == PickerMediaType.VIDEO;
    }

    @NonNull
    public PickerVideo getVideo() {
        if (video == null) {
            throw new IllegalStateException("There is no video in this media. Check by `isVideo()` before");
        }
        return video;
    }

    @Override
    public int hashCode() {
        int result = source.hashCode();
        result = 31 * result + mediaType.hashCode();
        result = 31 * result + (image != null ? image.hashCode() : 0);
        result = 31 * result + (video != null ? video.hashCode() : 0);
        return result;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        PickerMedia that = (PickerMedia) o;

        if (!source.equals(that.source)) return false;
        if (mediaType != that.mediaType) return false;

        if (image != null ? !image.equals(that.image) : that.image != null) return false;
        return video != null ? video.equals(that.video) : that.video == null;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeParcelable(source, flags);
        dest.writeSerializable(mediaType);
        dest.writeParcelable(image, flags);
        dest.writeParcelable(video, flags);
    }

    public static final Creator<PickerMedia> CREATOR = new Creator<PickerMedia>() {
        @Override
        public PickerMedia createFromParcel(Parcel in) {
            return new PickerMedia(in);
        }

        @Override
        public PickerMedia[] newArray(int size) {
            return new PickerMedia[size];
        }
    };
}
