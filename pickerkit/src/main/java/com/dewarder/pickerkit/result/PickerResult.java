package com.dewarder.pickerkit.result;

import android.os.Parcelable;

public interface PickerResult extends Parcelable {

    boolean isSubmitted();

    boolean isCanceled();
}
