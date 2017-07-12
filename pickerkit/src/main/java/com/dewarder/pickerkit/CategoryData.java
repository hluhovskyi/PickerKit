package com.dewarder.pickerkit;

import java.util.List;

public interface CategoryData<T, V> {

    public String getName();

    public T getSource();

    public List<V> getData();

    public int getItemCount();
}
