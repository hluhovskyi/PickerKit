package com.dewarder.pickerkit;

import android.net.Uri;
import android.os.Parcel;
import android.os.Parcelable;

import java.util.List;

public final class Result implements Parcelable {

    private final List<Uri> mPicked;

    private Result(List<Uri> picked) {
        mPicked = picked;
    }

    private Result(Parcel in) {
        mPicked = in.createTypedArrayList(Uri.CREATOR);
    }

    public static Result from(List<Uri> mPicked) {
        return new Result(mPicked);
    }

    public List<Uri> getPicked() {
        return mPicked;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeTypedList(mPicked);
    }

    public static final Creator<Result> CREATOR = new Creator<Result>() {
        @Override
        public Result createFromParcel(Parcel in) {
            return new Result(in);
        }

        @Override
        public Result[] newArray(int size) {
            return new Result[size];
        }
    };

}
