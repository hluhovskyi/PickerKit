package com.dewarder.pickerkit;

import android.os.Parcel;
import android.os.Parcelable;

public final class ImmutablePoint implements Parcelable {

    private static final ImmutablePoint EMPTY = of(Integer.MIN_VALUE, Integer.MIN_VALUE);

    private final int x;
    private final int y;

    private ImmutablePoint(int x, int y) {
        this.x = x;
        this.y = y;
    }

    private ImmutablePoint(Parcel in) {
        x = in.readInt();
        y = in.readInt();
    }

    public static ImmutablePoint of(int x, int y) {
        return new ImmutablePoint(x, y);
    }

    public static ImmutablePoint empty() {
        return EMPTY;
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public boolean isEmpty() {
        return this == EMPTY;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(x);
        dest.writeInt(y);
    }

    public static final Creator<ImmutablePoint> CREATOR = new Creator<ImmutablePoint>() {
        @Override
        public ImmutablePoint createFromParcel(Parcel in) {
            return new ImmutablePoint(in);
        }

        @Override
        public ImmutablePoint[] newArray(int size) {
            return new ImmutablePoint[size];
        }
    };
}
