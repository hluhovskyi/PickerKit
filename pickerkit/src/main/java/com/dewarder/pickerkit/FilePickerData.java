package com.dewarder.pickerkit;

import java.io.File;

public final class FilePickerData implements PickerItem<File> {

    private final File mFile;

    private FilePickerData(File file) {
        mFile = file;
    }

    public static FilePickerData from(File file) {
        return new FilePickerData(file);
    }

    @Override
    public File getSource() {
        return mFile;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        FilePickerData pickerData = (FilePickerData) o;

        return mFile.equals(pickerData.mFile);
    }

    @Override
    public int hashCode() {
        return mFile.hashCode();
    }
}
