package com.dewarder.pickerkit;

public interface OnPickerItemCheckListener<T extends PickerItem> {

    void onPickerItemChecked(T item, boolean checked);
}
