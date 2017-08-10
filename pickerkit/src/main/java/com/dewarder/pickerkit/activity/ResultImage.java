package com.dewarder.pickerkit.activity;

import android.os.Parcel;

import com.dewarder.pickerkit.model.PickerImage;
import com.dewarder.pickerkit.result.PickerImageResult;

import java.util.Collections;
import java.util.List;

class ResultImage implements PickerImageResult {

    private final List<PickerImage> selected;
    private final List<PickerImage> unselected;
    private final boolean isSubmitted;
    private final boolean isCanceled;

    ResultImage(List<PickerImage> selected,
                List<PickerImage> unselected,
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
    public List<PickerImage> getSelected() {
        return selected;
    }

    @Override
    public List<PickerImage> getUnselected() {
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

    protected ResultImage(Parcel in) {
        this.selected = in.createTypedArrayList(PickerImage.CREATOR);
        this.unselected = in.createTypedArrayList(PickerImage.CREATOR);
        this.isSubmitted = in.readByte() != 0;
        this.isCanceled = in.readByte() != 0;
    }

    public static final Creator<ResultImage> CREATOR = new Creator<ResultImage>() {
        @Override
        public ResultImage createFromParcel(Parcel source) {
            return new ResultImage(source);
        }

        @Override
        public ResultImage[] newArray(int size) {
            return new ResultImage[size];
        }
    };
}
