package com.dewarder.pickerkit;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public final class HashSetDataController<T> implements PickerAdapter.DataController<T> {

    private final Set<T> mPicked = new HashSet<>();

    @Override
    public List<T> getPicked() {
        return new ArrayList<>(mPicked);
    }

    @Override
    public boolean isPicked(T item) {
        return mPicked.contains(item);
    }

    @Override
    public void onPick(T item) {
        mPicked.add(item);
    }

    @Override
    public void onUnpick(T item) {
        mPicked.remove(item);
    }

    @Override
    public void clearPicked() {
        mPicked.clear();
    }
}
