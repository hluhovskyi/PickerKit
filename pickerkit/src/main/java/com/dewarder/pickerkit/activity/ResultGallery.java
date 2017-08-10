package com.dewarder.pickerkit.activity;

import android.os.Parcel;

import com.annimon.stream.Stream;
import com.dewarder.pickerkit.model.PickerMedia;
import com.dewarder.pickerkit.model.PickerMediaType;
import com.dewarder.pickerkit.result.PickerGalleryResult;
import com.dewarder.pickerkit.result.PickerImageResult;
import com.dewarder.pickerkit.result.PickerVideoResult;

import java.util.Collections;
import java.util.EnumSet;
import java.util.List;

class ResultGallery implements PickerGalleryResult {

    private final List<PickerMedia> selected;
    private final List<PickerMedia> unselected;
    private final boolean isSubmitted;
    private final boolean isCanceled;

    private ResultGallery(List<PickerMedia> selected,
                          List<PickerMedia> unselected,
                          boolean isSubmitted,
                          boolean isCanceled) {

        this.selected = Collections.unmodifiableList(selected);
        this.unselected = Collections.unmodifiableList(unselected);
        this.isSubmitted = isSubmitted;
        this.isCanceled = isCanceled;
    }

    private ResultGallery(Parcel in) {
        this.selected = in.createTypedArrayList(PickerMedia.CREATOR);
        this.unselected = in.createTypedArrayList(PickerMedia.CREATOR);
        this.isSubmitted = in.readByte() != 0;
        this.isCanceled = in.readByte() != 0;
    }

    static ResultGallery of(List<PickerMedia> checked, List<PickerMedia> unchecked) {
        return new ResultGallery(checked, unchecked, false, false);
    }

    static ResultGallery submit(List<PickerMedia> checked, List<PickerMedia> unchecked) {
        return new ResultGallery(checked, unchecked, true, false);
    }

    static ResultGallery cancel() {
        return new ResultGallery(Collections.emptyList(), Collections.emptyList(), false, true);
    }

    @Override
    public boolean isSubmitted() {
        return isSubmitted;
    }

    @Override
    public boolean isCanceled() {
        return isCanceled;
    }

    @Override
    public List<PickerMedia> getSelected() {
        return selected;
    }

    @Override
    public List<PickerMedia> getUnselected() {
        return unselected;
    }

    @Override
    public PickerImageResult getImageResult() {
        return new ResultImage(
                Stream.of(selected).filter(PickerMedia::isImage).map(PickerMedia::getImage).toList(),
                Stream.of(unselected).filter(PickerMedia::isImage).map(PickerMedia::getImage).toList(),
                isSubmitted,
                isCanceled);
    }

    @Override
    public PickerVideoResult getVideoResult() {
        return new ResultVideo(
                Stream.of(selected).filter(PickerMedia::isVideo).map(PickerMedia::getVideo).toList(),
                Stream.of(unselected).filter(PickerMedia::isVideo).map(PickerMedia::getVideo).toList(),
                isSubmitted,
                isCanceled);
    }

    @Override
    public EnumSet<PickerMediaType> getTypes() {
        return null;
    }

    @Override
    public boolean containsType(PickerMediaType type) {
        return false;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeTypedList(this.selected);
        dest.writeTypedList(this.unselected);
        dest.writeByte(this.isSubmitted ? (byte) 1 : (byte) 0);
        dest.writeByte(this.isCanceled ? (byte) 1 : (byte) 0);
    }

    public static final Creator<ResultGallery> CREATOR = new Creator<ResultGallery>() {
        @Override
        public ResultGallery createFromParcel(Parcel source) {
            return new ResultGallery(source);
        }

        @Override
        public ResultGallery[] newArray(int size) {
            return new ResultGallery[size];
        }
    };
}
