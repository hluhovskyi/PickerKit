package com.dewarder.pickerkit;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public final class SimpleDataController<T> implements PickerItemAdapter.DataController<T> {

    private final Set<T> picked = new HashSet<>();

    @Override
    public List<T> getPicked() {
        return new ArrayList<>(picked);
    }

    @Override
    public boolean isPicked(T item) {
        return picked.contains(item);
    }

    @Override
    public void onPick(T item) {
        picked.add(item);
    }

    @Override
    public void onUnpick(T item) {
        picked.remove(item);
    }

    @Override
    public void clearPicked() {
        picked.clear();
    }
}
