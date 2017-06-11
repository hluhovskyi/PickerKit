package com.dewarder.pickerok;

import java.io.File;

public class PickerData implements PickerItem {

    private final File mFile;

    public PickerData(File file) {
        mFile = file;
    }

    public File getFile() {
        return mFile;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        PickerData pickerData = (PickerData) o;

        return mFile.equals(pickerData.mFile);
    }

    @Override
    public int hashCode() {
        return mFile.hashCode();
    }
}
