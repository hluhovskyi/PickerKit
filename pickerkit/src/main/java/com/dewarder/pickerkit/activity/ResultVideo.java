package com.dewarder.pickerkit.activity;

import android.os.Parcel;

import com.dewarder.pickerkit.model.PickerImage;
import com.dewarder.pickerkit.model.PickerVideo;
import com.dewarder.pickerkit.result.PickerImageResult;
import com.dewarder.pickerkit.result.PickerVideoResult;

import java.util.Collections;
import java.util.List;

class ResultVideo implements PickerVideoResult {

    private final List<PickerVideo> selected;
    private final List<PickerVideo> unselected;
    private final boolean isSubmitted;
    private final boolean isCanceled;

    ResultVideo(List<PickerVideo> selected,
                List<PickerVideo> unselected,
                boolean isSubmitted,
                boolean isCanceled) {

        this.selected = Collections.unmodifiableList(selected);
        this.unselected = Collections.unmodifiableList(unselected);
        this.isSubmitted = isSubmitted;
        this.isCanceled = isCanceled;
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
    public List<PickerVideo> getSelected() {
        return selected;
    }

    @Override
    public List<PickerVideo> getUnselected() {
        return unselected;
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

    protected ResultVideo(Parcel in) {
        this.selected = in.createTypedArrayList(PickerVideo.CREATOR);
        this.unselected = in.createTypedArrayList(PickerVideo.CREATOR);
        this.isSubmitted = in.readByte() != 0;
        this.isCanceled = in.readByte() != 0;
    }

    public static final Creator<ResultVideo> CREATOR = new Creator<ResultVideo>() {
        @Override
        public ResultVideo createFromParcel(Parcel source) {
            return new ResultVideo(source);
        }

        @Override
        public ResultVideo[] newArray(int size) {
            return new ResultVideo[size];
        }
    };
}
