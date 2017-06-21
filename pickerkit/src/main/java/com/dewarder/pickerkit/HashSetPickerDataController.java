package com.dewarder.pickerkit;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public final class HashSetPickerDataController implements PickerAdapter.Controller<PickerData> {

    private final Set<PickerData> mPicked = new HashSet<>();

    @Override
    public List<PickerData> getPicked() {
        return new ArrayList<>(mPicked);
    }

    @Override
    public boolean isPicked(PickerData item) {
        return mPicked.contains(item);
    }

    @Override
    public void onPick(PickerData item) {
        mPicked.add(item);
    }

    @Override
    public void onUnpick(PickerData item) {
        mPicked.remove(item);
    }

    @Override
    public void clearPicked() {
        mPicked.clear();
    }
}
