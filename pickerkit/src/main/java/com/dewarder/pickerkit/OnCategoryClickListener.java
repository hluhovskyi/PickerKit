package com.dewarder.pickerkit;

public interface OnCategoryClickListener<T extends CategoryData<?, ?>> {

    void onCategoryClicked(T category);
}
